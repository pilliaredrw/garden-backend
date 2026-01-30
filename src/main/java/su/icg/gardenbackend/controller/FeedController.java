package su.icg.gardenbackend.controller;

import com.rometools.rome.feed.synd.SyndFeed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import su.icg.gardenbackend.entity.Feed;
import su.icg.gardenbackend.service.FeedService;
import java.util.List;

@RestController
public class FeedController {

    @Autowired
    private FeedService feedService;

    // 前端读取接口 (查库)
    @GetMapping("/api/feed")
    public List<Feed> getFeed() {
        return feedService.getAllFeeds();
    }

    // 手动触发同步接口 (抓取)
    @GetMapping("/api/sync")
    public String triggerSync() {
        SyndFeed feed = feedService.syncBlogRss();
        return "测试RSS接收连通性" + feed.toString();
    }
}