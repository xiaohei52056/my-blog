package com.example.blog.service.impl;

import com.example.blog.entity.User;
import com.example.blog.mapper.UserMapper;
import com.example.blog.service.UserService;
import com.example.blog.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 用户业务逻辑实现。
 * 负责注册、登录以及按ID 查询用户信息等核心逻辑。
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtUtils jwtUtils;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * 用户注册：
     * 1. 校验用户名与密码；
     * 2. 检查用户名是否已存在；
     * 3. BCrypt 加密密码；
     * 4. 默认昵称即用户名；
     * 5. 写入数据库。
     */
    @Override
    public void register(User user) {
        if (user == null) {
            throw new IllegalArgumentException("用户信息不能为空");
        }
        String username = user.getUsername();
        String password = user.getPassword();

        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("用户名不能为空");
        }
        if (username.length() > 50) {
            throw new IllegalArgumentException("用户名长度不能超过50个字符");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("密码不能为空");
        }
        if (password.length() < 6) {
            throw new IllegalArgumentException("密码不能少于6位");
        }

        User existing = userMapper.selectByusername(username.trim());
        if (existing != null) {
            throw new RuntimeException("用户名已存在");
        }

        user.setUsername(username.trim());
        user.setPassword(passwordEncoder.encode(password));

        if (user.getNickname() == null || user.getNickname().trim().isEmpty()) {
            user.setNickname(user.getUsername());
        }

        userMapper.insert(user);
    }

    /**
     * 用户登录。校验用户名、密码，返回 JWT 令牌。
     */
    @Override
    public String login(String username, String password) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("用户名不能为空");
        }
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("密码不能为空");
        }

        User user = userMapper.selectByusername(username.trim());
        if (user == null) {
            throw new RuntimeException("用户名或密码错误");
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("用户名或密码错误");
        }

        return jwtUtils.generateToken(user.getId());
    }

    /**
     * 根据ID 查询用户。返回对象不包含密码（Mapper 中已主动排除）。
     */
    @Override
    public User getById(Integer id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("用户ID 非法");
        }
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        // 双保险：即使 Mapper 层忘记清除密码，这里也置空
        user.setPassword(null);
        return user;
    }
}
