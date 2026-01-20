package su.icg.gardenbackend.model;

import lombok.Data;

@Data // lombok 自动生成getter和setter，toSring等方法
public class FeedItem
{
    private String title;
    private String link;
    private String date;
}

