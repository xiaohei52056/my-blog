package com.example.blog.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户实体类
 * 注意：字段使用驼峰命名（createTime/updateTime），
 * MyBatis 开启 map-underscore-to-camel-case 后会自动与数据库列 create_time/update_time 映射。
 */
@Data
public class User {
    /** 用户ID */
    private Integer id;
    /** 用户名 */
    private String username;
    /** 密码（加密存储，接口不返回） */
    private String password;
    /** 昵称 */
    private String nickname;
    /** 邮箱（预留） */
    private String email;
    /** 创建时间 */
    private LocalDateTime createTime;
    /** 更新时间 */
    private LocalDateTime updateTime;
}
