package com.example.lab203.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * ???????
 */
@Data
public class DeskItemEntity {

    private Long id;
    private Long studentId;
    private String itemName;
    private Integer quantity;
    private String itemStatus;
    private String remark;
    private Integer isDeleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
