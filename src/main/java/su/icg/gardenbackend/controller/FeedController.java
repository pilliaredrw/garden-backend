package su.icg.gardenbackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
    // 浏览器访问 http://localhost:8080/api/sync 就会触发一次抓取
    @GetMapping("/api/sync")
    public String triggerSync() {
        feedService.syncBlogRss();
        return "同步指令已发送，请查看控制台日志";
    }
}