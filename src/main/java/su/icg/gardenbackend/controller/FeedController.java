package su.icg.gardenbackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import su.icg.gardenbackend.model.FeedItem;
import su.icg.gardenbackend.service.FeedService;

import java.util.List;

@RestController // 注解表明是 Restapi 的 Controller 入口。负责处理 HTTP 请求，返回 JSON。
public class FeedController
{

    @Autowired // 自动注入，使得不需要 new 对象
    private FeedService feedService;

    @GetMapping("/api/feed") // 前端请求路由
    public List<FeedItem> getFeed()
    {
        return feedService.fetchRss();
    }
}
