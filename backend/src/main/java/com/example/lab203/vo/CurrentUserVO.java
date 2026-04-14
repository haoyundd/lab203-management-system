package com.example.lab203.vo;

import lombok.Data;

/**
 * ???????????
 */
@Data
public class CurrentUserVO {

    private Long accountId;
    private Long studentId;
    private String username;
    private String displayName;
    private String role;
    private String avatarPath;
}
