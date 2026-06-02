package com.example.blog.controller;

import com.example.blog.common.Result;
import com.example.blog.entity.Article;
import com.example.blog.service.ArticleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 文章 CRUD 接口。
 * 列表、详情 面向游客开放；新增、编辑、删除需要 JWT 认证并校验作者权限。
 */
@RestController
@RequestMapping("/api/articles")
@Tag(name = "文章管理", description = "文章的增删改查接口")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    /**
     * 查询所有文章（无需登录）。
     */
    @Operation(summary = "获取文章列表")
    @GetMapping
    public Result<List<Article>> selectAll() {
        return Result.success(articleService.selectAll());
    }

    /**
     * 根据ID 查询单篇文章详情（无需登录）。
     */
    @Operation(summary = "获取文章详情")
    @GetMapping("/{id}")
    public Result<Article> selectById(@PathVariable Integer id) {
        return Result.success(articleService.selectById(id));
    }

    /**
     * 新增文章；必须登录，自动以当前用户作为作者。
     */
    @Operation(summary = "发布文章")
    @PostMapping
    public Result<Article> save(@RequestBody Article article, HttpServletRequest request) {
        Integer userId = (Integer) request.getAttribute("userId");
        Article saved = articleService.save(article, userId);
        return Result.success(saved);
    }

    /**
     * 更新文章；必须登录，且仅允许作者本人修改。
     */
    @Operation(summary = "编辑文章")
    @PutMapping
    public Result<Void> update(@RequestBody Article article, HttpServletRequest request) {
        Integer userId = (Integer) request.getAttribute("userId");
        articleService.update(article, userId);
        return Result.success();
    }

    /**
     * 根据ID 删除文章；必须登录，且仅允许作者本人删除。
     */
    @Operation(summary = "删除文章")
    @DeleteMapping("/{id}")
    public Result<Void> deleteById(@PathVariable Integer id, HttpServletRequest request) {
        Integer userId = (Integer) request.getAttribute("userId");
        articleService.deleteById(id, userId);
        return Result.success();
    }
}
