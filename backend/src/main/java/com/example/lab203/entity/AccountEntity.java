package com.example.lab203.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * ?????
 */
@Data
public class AccountEntity {

    private Long id;
    private String username;
    private String passwordHash;
    private String role;
    private Long studentId;
    private String displayName;
    private String token;
    private LocalDateTime tokenExpireTime;
    private Integer isDeleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
