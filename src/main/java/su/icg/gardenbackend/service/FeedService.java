package su.icg.gardenbackend.service;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import su.icg.gardenbackend.model.FeedItem;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Slf4j // 引入日志工具，不用再手动写 LoggerFactory...
@Service
public class FeedService
{
    // application.properties 读取配置到 blogRssUrl
    @Value("${rss.blog.url}")
    private String blogRssUrl; // 接受 URL 字面值

    public List<FeedItem> fetchRss()
    {
        List<FeedItem> items = new ArrayList<>();

        try
        {
            // 1. 记录日志开始抓数据
            log.info("开始抓取 RSS: {}", blogRssUrl);

            // 2. 建立连接并解析
            URL feedUrl = new URL(blogRssUrl); // 创建一个 URL 连接
            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(new XmlReader(feedUrl)); // XmlReader 处理编码问题 (UTF-8/GBK)

            // 3. 把 Rome 的格式 (SyndEntry) 转换为 Model 格式 (FeedItem)
            for (SyndEntry entry : feed.getEntries())
            {
                FeedItem item = new FeedItem();
                item.setTitle(entry.getTitle());
                item.setLink(entry.getLink());
                // RSS 的时间可能是 PublishedDate 也可能是 UpdatedDate，做个防御
                item.setDate(entry.getPublishedDate() != null ? entry.getPublishedDate().toString() : "未知时间");

                items.add(item);
            }
            log.info("抓取成功，共获取 {} 条数据", items.size());

        }
        catch (Exception e)
        {
            log.error("抓取 RSS 失败: {}", e.getMessage(), e);
            // 这里我们选择返回一个空列表，或者也可以抛出自定义异常给前端
        }

        return items;
    }
}