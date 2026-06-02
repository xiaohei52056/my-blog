package com.example.blog.service;

import com.example.blog.entity.User;

/**
 * 用户业务逻辑接口。
 */
public interface UserService {

    /** 用户注册 */
    void register(User user);

    /** 用户登录，返回 JWT 令牌 */
    String login(String username, String password);

    /** 根据ID 获取用户信息（不含密码） */
    User getById(Integer id);
}
