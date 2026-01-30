package su.icg.gardenbackend.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rometools.rome.feed.synd.SyndContent;
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
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class FeedService
{

    @Value("${rss.blog.url}")
    private String blogRssUrl; // 通过 @Value 注解得到配置文件中 URL 的内容

    @Autowired
    private FeedMapper feedMapper; // 注入 Mapper，用来操作数据库

    private SyndFeed getRssByUrl(String url)
    {
        try {
            log.info("[INFO]开始抓取RSS");
            URL feedUrl = new URL(blogRssUrl);
            SyndFeed feed = (new SyndFeedInput()).build(new XmlReader((feedUrl)));
            log.info("解析得到内容:" + feed.toString());
            return feed;
        } catch (Exception e) {
            log.info(("抓取失败"));
            return null;
        }
    }

    private int saveFeedEntries(SyndFeed rssFeed) {
        // 1. 基础校验
        // todo: 到时候做一个先去数据库提取文章的 contest 和 desc 字段作校验，如果改变就再次存入

        if (rssFeed == null || rssFeed.getEntries() == null || rssFeed.getEntries().isEmpty()) {
            log.warn("RSS Feed 为空或无条目，跳过处理");
            return 0;
        }

        int successCount = 0; // 计数器

        // 2. 遍历每一篇文章
        for (SyndEntry entry : rssFeed.getEntries()) {
            try {
                // --- A. 防重判断 (核心逻辑) ---
                String link = entry.getLink();
                // 如果 link 为空，无法定位，直接跳过
                if (link == null || link.trim().isEmpty()) {
                    log.debug("链接为空无法定位，跳过");
                    continue;
                }

                // 查询数据库是否已存在该链接
                Long count = feedMapper.selectCount(new LambdaQueryWrapper<Feed>().eq(Feed::getUrl, link));
                if (count > 0) {
                     log.debug("文章已存在，跳过: {}", entry.getTitle());
                    continue; // 【关键】数据库有了，跳过本次循环，处理下一条
                }

                // --- B. 内容解析 (三级降级策略) ---
                String finalContent = null;

                // 策略1: 尝试获取标准 Content 列表 (通常对应 <content:encoded>)
                if (entry.getContents() != null && !entry.getContents().isEmpty()) {
                    for (SyndContent content : entry.getContents()) {
                        if (content.getValue() != null && !content.getValue().isEmpty()) {
                            finalContent = content.getValue();
                            break; // 找到一个就退出
                        }
                    }
                }

                // 策略2: 如果没拿到，尝试获取 Description (通常对应 <description>)
                if (finalContent == null && entry.getDescription() != null) {
                    finalContent = entry.getDescription().getValue();
                }

                // 策略3: 如果还是空，标记错误或设为空串
                if (finalContent == null) {
                    finalContent = "解析内容为空";
                }

                // --- C. 组装实体对象 ---
                Feed feedEntity = new Feed();
                feedEntity.setTitle(entry.getTitle());
                feedEntity.setUrl(link);
                feedEntity.setContent(finalContent);
                feedEntity.setSourceType("BLOG");

                // 时间转换 (注意判空)
                Date pubDate = entry.getPublishedDate();
                // 如果 RSS 没有发布时间，则使用当前时间，或者是抓取到的 updatedDate
                if (pubDate == null) {
                    pubDate = entry.getUpdatedDate() != null ? entry.getUpdatedDate() : new Date();
                }
                feedEntity.setPublishTime(pubDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());

                // 补充：创建时间通常由数据库自动生成，或手动设为当前
                feedEntity.setCreateTime(LocalDateTime.now());

                // --- D. 执行插入 ---
                feedMapper.insert(feedEntity);
                successCount++; // 计数+1
                log.info("成功入库第 {} 条: {}", successCount, entry.getTitle());

            } catch (Exception e) {
                // 【重要】Catch放在循环内！这样一篇文章报错不会影响其他文章入库
                log.error("处理单篇文章失败: URL={}, Error={}", entry.getLink(), e.getMessage());
            }
        }

        return successCount;
    }

    // 动作 1: 触发抓取并入库 (Sync)
    public SyndFeed syncBlogRss()
    {
        try {
            // 拿到RSS内容首先
            SyndFeed RssFeed = getRssByUrl(blogRssUrl);

            // 存入数据库
            int count = saveFeedEntries(RssFeed);

            log.error("同步了" + count + "条博文");

            return RssFeed;
        } catch (Exception e) {
            log.error("同步失败", e);
            return null;
        }
    }

    // 动作 2: 给前端查数据 (Query)
    public List<Feed> getAllFeeds()
    {
        // 相当于 SQL: SELECT * FROM t_feed ORDER BY publish_time DESC
        return feedMapper.selectList(
                new LambdaQueryWrapper<Feed>().orderByDesc(Feed::getPublishTime)
        );
    }
}