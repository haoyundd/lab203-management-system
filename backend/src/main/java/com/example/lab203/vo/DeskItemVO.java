package com.example.lab203.vo;

import lombok.Data;

/**
 * ?????????
 */
@Data
public class DeskItemVO {

    private Long id;
    private Long studentId;
    private String studentName;
    private String itemName;
    private Integer quantity;
    private String itemStatus;
    private String remark;
}
