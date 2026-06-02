package com.example.blog.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * JWT 工具类
 * 负责生成、验证令牌以及解析用户ID。
 * 注意：密钥使用固定值以保证应用重启后令牌依然有效，生产环境请通过配置文件注入。
 */
@Component
public class JwtUtils {

    // 固定密钥（使用 256 位以上字符串，满足 HS256 算法要求）。
    // 生产环境请改为从 application.yml / 环境变量注入。
    private static final String SECRET_KEY_STR = "blog-demo-secret-key-for-jwt-signing-please-change-in-production-env";
    private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(SECRET_KEY_STR.getBytes(StandardCharsets.UTF_8));

    // 令牌过期时间：7 天
    private static final long EXPIRE_TIME = 7 * 24 * 60 * 60 * 1000L;

    /**
     * 根据用户ID 生成 JWT 令牌。
     */
    public String generateToken(Integer userId) {
        if (userId == null) {
            throw new IllegalArgumentException("用户ID 不能为空");
        }
        return Jwts.builder()
                .setSubject(userId.toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE_TIME))
                .signWith(SECRET_KEY)
                .compact();
    }

    /**
     * 校验令牌是否合法且未过期。
     */
    public boolean validateToken(String token) {
        if (token == null || token.isEmpty()) {
            return false;
        }
        try {
            Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 从令牌中解析出用户ID。若解析失败将抛出异常，调用方应先使用 validateToken 做前置校验。
     */
    public Integer getUserIdFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return Integer.parseInt(claims.getSubject());
    }
}
