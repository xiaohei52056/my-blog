package com.example.blog.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 文章实体类
 * 字段使用驼峰命名，MyBatis 开启 map-underscore-to-camel-case 后会自动与数据库列映射。
 */
@Data
public class Article {
    /** 文章ID */
    private Integer id;
    /** 文章标题 */
    private String title;
    /** 文章内容 */
    private String content;
    /** 文章作者（昵称） */
    private String author;
    /** 创建时间 */
    private LocalDateTime createTime;
    /** 更新时间 */
    private LocalDateTime updateTime;
    /** 文章作者的用户ID（用于权限校验） */
    private Integer userId;
}
