package com.example.blog.controller;

import com.example.blog.common.Result;
import com.example.blog.entity.User;
import com.example.blog.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 用户相关接口：注册、登录、获取当前用户信息。
 * 登录与注册接口已在 WebConfig 中放行 JWT 拦截器。
 * getCurrentUser 接口则需要携带有效的 JWT 令牌。
 */
@RestController
@RequestMapping("/api/users")
@Tag(name = "用户管理", description = "用户注册、登录、获取当前用户信息")
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(summary = "用户注册")
    @PostMapping("/register")
    public Result<String> register(@RequestBody User user) {
        userService.register(user);
        return Result.success("注册成功");
    }

    @Operation(summary = "用户登录", description = "登录成功返回 JWT 令牌，保存到 localStorage 后用作后续请求的 Authorization 头")
    @PostMapping("/login")
    public Result<String> login(@RequestBody User user) {
        String token = userService.login(user.getUsername(), user.getPassword());
        return Result.success(token);
    }

    @Operation(summary = "获取当前登录用户信息", description = "需要携带 JWT 令牌。不会返回密码字段。")
    @GetMapping("/current")
    public Result<User> getCurrentUser(HttpServletRequest request) {
        Integer userId = (Integer) request.getAttribute("userId");
        User user = userService.getById(userId);
        return Result.success(user);
    }
}
