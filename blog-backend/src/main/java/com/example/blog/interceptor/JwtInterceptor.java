// JWT 认证拦截器
package com.example.blog.interceptor;

import com.example.blog.utils.JwtUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.blog.common.Result;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * JWT 认证拦截器。
 * 根据请求路径和方法决定是否需要认证：
 * - 公开接口（登录、注册）：直接放行
 * - 文章列表/详情（GET）：放行（游客可访问）
 * - 其他接口（发布、修改、删除文章等）：需要有效 token
 */
@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtils jwtUtils;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String path = request.getRequestURI();
        String method = request.getMethod();

        // 公开接口：登录、注册直接放行
        if (path.equals("/api/users/login") || path.equals("/api/users/register")) {
            return true;
        }

        // 文章列表和详情的 GET 请求放行（游客可访问）
        if (HttpMethod.GET.name().equalsIgnoreCase(method)) {
            if (path.equals("/api/articles") || path.matches("/api/articles/\\d+")) {
                return true;
            }
        }

        // 其他请求需要验证 token
        String authHeader = request.getHeader("Authorization");

        // 检查是否有 Bearer 前缀
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            writeUnauthorized(response, "未登录或缺少令牌");
            return false;
        }

        // 提取 token
        String token = authHeader.substring(7);

        // 验证 token 有效性
        Integer userId;
        try {
            if (!jwtUtils.validateToken(token)) {
                writeUnauthorized(response, "令牌已过期或无效");
                return false;
            }
            userId = jwtUtils.getUserIdFromToken(token);
        } catch (Exception e) {
            writeUnauthorized(response, "令牌解析失败");
            return false;
        }

        if (userId == null) {
            writeUnauthorized(response, "无效的令牌内容");
            return false;
        }

        // 把用户ID 存入 request 属性，供 Controller 使用
        request.setAttribute("userId", userId);
        return true;
    }

    private void writeUnauthorized(HttpServletResponse response, String message) {
        try {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(objectMapper.writeValueAsString(Result.error(message)));
        } catch (Exception ignored) {
        }
    }
}
