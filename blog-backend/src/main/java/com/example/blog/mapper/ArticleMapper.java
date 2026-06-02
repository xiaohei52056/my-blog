package com.example.blog.mapper;

import com.example.blog.entity.Article;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 文章数据访问接口。
 * 配合 resources/mapper/ArticleMapper.xml 使用。
 */
@Mapper
public interface ArticleMapper {

    /** 查询所有文章（按创建时间倒序） */
    List<Article> selectAll();

    /** 根据 ID 查询文章 */
    Article selectById(@Param("id") Integer id);

    /** 新增文章，返回受影响行数；主键回填至 article.id */
    int insert(Article article);

    /** 更新文章的标题和内容，仅作者本人可用 */
    int update(Article article);

    /** 根据 ID 删除文章，仅作者本人可用 */
    int deleteById(@Param("id") Integer id);
}
