package com.guozhangtong.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 果品档案（品类 + 规格 + 预设单价）。
 */
@Data
@TableName("fruit")
public class Fruit {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 品类，如 沃柑 */
    private String name;

    /** 规格等级，如 大果/统货 */
    private String spec;

    /** 默认收购单价（元） */
    private BigDecimal price;

    /** 计量单位 */
    private String unit;

    /** 1 启用 0 停用 */
    private Integer enabled;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
