package com.example.blog.service.impl;

import com.example.blog.entity.Article;
import com.example.blog.mapper.ArticleMapper;
import com.example.blog.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 文章业务逻辑实现。
 * 增加参数校验和"只能操作自己文章"的权限控制。
 */
@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;

    @Override
    public List<Article> selectAll() {
        return articleMapper.selectAll();
    }

    @Override
    public Article selectById(Integer id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("文章ID 非法");
        }
        Article article = articleMapper.selectById(id);
        if (article == null) {
            throw new RuntimeException("文章不存在");
        }
        return article;
    }

    @Override
    public Article save(Article article, Integer userId) {
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("当前用户未登录");
        }
        if (article == null) {
            throw new IllegalArgumentException("文章信息不能为空");
        }
        String title = article.getTitle();
        String content = article.getContent();
        String author = article.getAuthor();

        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("标题不能为空");
        }
        if (title.length() > 200) {
            throw new IllegalArgumentException("标题不能超过200字");
        }
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("内容不能为空");
        }
        if (content.length() > 50000) {
            throw new IllegalArgumentException("文章内容过长");
        }

        article.setTitle(title.trim());
        article.setContent(content.trim());
        // 作者名若为空则设置默认值
        article.setAuthor((author != null && !author.trim().isEmpty()) ? author.trim() : "匿名");
        article.setUserId(userId);

        articleMapper.insert(article);
        return article;
    }

    @Override
    public void update(Article article, Integer userId) {
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("当前用户未登录");
        }
        if (article == null || article.getId() == null || article.getId() <= 0) {
            throw new IllegalArgumentException("文章ID 非法");
        }
        String title = article.getTitle();
        String content = article.getContent();
        if (title != null && title.length() > 200) {
            throw new IllegalArgumentException("标题不能超过200字");
        }

        // 权限校验：必须是文章作者才能更新
        Article exists = articleMapper.selectById(article.getId());
        if (exists == null) {
            throw new RuntimeException("文章不存在");
        }
        if (exists.getUserId() == null || !exists.getUserId().equals(userId)) {
            throw new RuntimeException("无权编辑他人文章");
        }

        article.setTitle(title != null ? title : exists.getTitle());
        article.setContent(content != null ? content : exists.getContent());

        articleMapper.update(article);
    }

    @Override
    public void deleteById(Integer id, Integer userId) {
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("当前用户未登录");
        }
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("文章ID 非法");
        }

        // 权限校验：必须是文章作者才能删除
        Article exists = articleMapper.selectById(id);
        if (exists == null) {
            throw new RuntimeException("文章不存在");
        }
        if (exists.getUserId() == null || !exists.getUserId().equals(userId)) {
            throw new RuntimeException("无权删除他人文章");
        }

        articleMapper.deleteById(id);
    }
}
