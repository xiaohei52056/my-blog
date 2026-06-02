package com.example.blog.mapper;

import com.example.blog.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 用户数据访问接口。
 * 注：由于 MyBatis 已开启 map-underscore-to-camel-case，
 * 数据库列 create_time / update_time 会自动映射为实体字段 createTime / updateTime。
 */
@Mapper
public interface UserMapper {

    /**
     * 根据用户名查询用户（用户名唯一约束）。
     */
    @Select("SELECT id, username, password, nickname, email, create_time, update_time " +
            "FROM user WHERE username = #{username}")
    User selectByusername(@Param("username") String username);

    /**
     * 新增用户，数据库默认值处理 create_time / update_time。
     */
    @Insert("INSERT INTO user (username, password, nickname) VALUES (#{username}, #{password}, #{nickname})")
    int insert(User user);

    /**
     * 根据ID 查询用户（不含密码），用于获取当前登录用户信息。
     */
    @Select("SELECT id, username, nickname, email, create_time, update_time FROM user WHERE id = #{id}")
    User selectById(@Param("id") Integer id);
}
