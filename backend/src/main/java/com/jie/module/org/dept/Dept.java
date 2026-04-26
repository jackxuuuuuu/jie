package com.jie.module.org.dept;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("dept")
public class Dept {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    private Long leaderUserId;

    private Integer sort;

    private Integer status;

    @TableLogic
    private Integer deleted;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
