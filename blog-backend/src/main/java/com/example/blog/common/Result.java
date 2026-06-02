package com.example.blog.common;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 全局统一响应结果类
 * @param <T> 业务数据类型
 */
@Data
public class Result<T> {
    /**
     * 响应状态码
     */
    private Integer code;

    /**
     * 响应消息
     */
    private String message;

    /**
     * 业务数据
     */
    private T data;

    /**
     * 响应时间戳
     */
    private LocalDateTime timestamp;

    /**
     * 私有构造方法，禁止外部直接创建
     */
    private Result() {
        this.timestamp = LocalDateTime.now();
    }

    /**
     * 成功响应（带数据）
     */
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMessage("操作成功");
        result.setData(data);
        return result;
    }

    /**
     * 成功响应（不带数据）
     */
    public static <T> Result<T> success() {
        return success(null);
    }

    /**
     * 失败响应
     */
    public static <T> Result<T> error(Integer code, String message) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }

    /**
     * 失败响应（默认500错误）
     */
    public static <T> Result<T> error(String message) {
        return error(500, message);
    }
}