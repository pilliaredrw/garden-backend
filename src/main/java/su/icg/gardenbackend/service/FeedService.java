package su.icg.gardenbackend.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import su.icg.gardenbackend.entity.Feed;
import su.icg.gardenbackend.mapper.FeedMapper;

import java.net.URL;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class FeedService {

    @Value("${rss.blog.url}")
    private String blogRssUrl; // 通过 @Value 注解得到配置文件中 URL 的内容

    @Autowired
    private FeedMapper feedMapper; // 注入 Mapper，用来操作数据库

    // 动作 1: 触发抓取并入库 (Sync)
    public void syncBlogRss() {
        try {
            log.info("开始同步博客数据...");
            URL feedUrl = new URL(blogRssUrl);
            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(new XmlReader(feedUrl));
            log.info("解析得到内容:"+feed.toString());

            for (SyndEntry entry : feed.getEntries()) {
                // 1. 防重判断：数据库里有没有这条链接？
                String link = entry.getLink();
                // LambdaQueryWrapper 是 MP 的神器，相当于写 SQL: SELECT count(*) FROM t_feed WHERE url = 'link'
                Long count = feedMapper.selectCount(new LambdaQueryWrapper<Feed>().eq(Feed::getUrl, link));

                if (count > 0) {
                    continue; // 数据库有了，跳过
                }

                // 2. 组装 Entity 对象
                Feed feedEntity = new Feed();
                feedEntity.setTitle(entry.getTitle());
                feedEntity.setUrl(link);
                feedEntity.setContent(entry.getDescription() != null ? entry.getDescription().getValue() : "");
                feedEntity.setSourceType("BLOG");

                // 时间转换 (Date -> LocalDateTime)
                Date pubDate = entry.getPublishedDate();
                if (pubDate != null) {
                    feedEntity.setPublishTime(pubDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
                }

                // 3. 入库
                feedMapper.insert(feedEntity);
                log.info("入库新文章: {}", entry.getTitle());
            }
        } catch (Exception e) {
            log.error("同步失败", e);
        }
    }

    // 动作 2: 给前端查数据 (Query)
    public List<Feed> getAllFeeds() {
        // 相当于 SQL: SELECT * FROM t_feed ORDER BY publish_time DESC
        return feedMapper.selectList(
                new LambdaQueryWrapper<Feed>().orderByDesc(Feed::getPublishTime)
        );
    }
}