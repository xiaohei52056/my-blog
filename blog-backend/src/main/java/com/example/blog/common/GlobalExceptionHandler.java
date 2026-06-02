package com.example.blog.common;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 * 统一捕获并处理所有 Controller 抛出的异常，返回标准化的 Result 响应。
 */
@Hidden
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理业务异常（RuntimeException），例如用户名已存在、密码错误、无权限操作等。
     * 返回 HTTP 200 + code=500 的业务响应体，便于前端统一解析 message。
     */
    @ExceptionHandler(RuntimeException.class)
    public Result<String> handleRuntimeException(RuntimeException e) {
        // 业务异常不需要打印完整堆栈，仅记录日志即可
        System.out.println("[业务异常] " + e.getMessage());
        return Result.error(e.getMessage());
    }

    /**
     * 处理参数非法异常（IllegalArgumentException）。
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public Result<String> handleIllegalArgumentException(IllegalArgumentException e) {
        System.out.println("[参数异常] " + e.getMessage());
        return Result.error(e.getMessage());
    }

    /**
     * 兜底的异常处理。捕获所有未预料到的异常，避免向前端暴露堆栈信息。
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Result<String>> handleException(Exception e) {
        e.printStackTrace();
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Result.error("服务器内部错误，请稍后重试"));
    }
}
