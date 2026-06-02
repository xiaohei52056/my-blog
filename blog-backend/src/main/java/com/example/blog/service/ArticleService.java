package com.example.blog.service;

import com.example.blog.entity.Article;

import java.util.List;

/**
 * 文章业务逻辑接口。
 * userId 参数用于权限校验，确保用户只能编辑或删除自己的文章。
 */
public interface ArticleService {

    /** 查询文章列表（无需登录） */
    List<Article> selectAll();

    /** 根据 ID 查询文章详情（无需登录） */
    Article selectById(Integer id);

    /** 新增文章，返回保存后的对象 */
    Article save(Article article, Integer userId);

    /** 更新文章，校验作者权限 */
    void update(Article article, Integer userId);

    /** 删除文章，校验作者权限 */
    void deleteById(Integer id, Integer userId);
}
