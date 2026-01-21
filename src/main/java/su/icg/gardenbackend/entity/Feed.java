package su.icg.gardenbackend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("t_feed") // 【核心】告诉 MybatisPlus，这个类对应数据库里的 t_feed 表。而且 mybatis 默认驼峰转蛇
public class Feed
{
    @TableId(type = IdType.AUTO) // 插入主键设置自增
    private Long id;
    private String title;
    private String content; // 存实际内容
    private String url;
    private String sourceType; // 对应 source_type，表示数据的来源是 RSS 还是 memos api
    private LocalDateTime publishTime;
    private LocalDateTime createTime;
}

//建表参考语句
//CREATE TABLE t_feed (
//        id BIGINT AUTO_INCREMENT COMMENT '主键ID',
//        title VARCHAR(255) COMMENT '标题',
//content MEDIUMTEXT COMMENT '内容(长文本)',
//url VARCHAR(500) NOT NULL COMMENT '原文链接(唯一标识)',
//source_type VARCHAR(50) NOT NULL COMMENT '来源: BLOG/MEMO',
//publish_time DATETIME COMMENT '发布时间',
//create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '入库时间',
//PRIMARY KEY (id),
//UNIQUE KEY uk_url (url) -- 【核心】这个唯一索引防止你重复存同一篇文章
//) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='聚合信息表';