package com.guozhangtong.controller.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 果品新增/修改请求。
 */
@Data
public class FruitRequest {

    /** 品类，如 沃柑 */
    @NotBlank(message = "请填写品类")
    private String name;

    /** 规格等级 */
    @NotBlank(message = "请填写规格")
    private String spec;

    /** 默认收购单价 */
    @NotNull(message = "请填写单价")
    @DecimalMin(value = "0", message = "单价不能为负数")
    private BigDecimal price;

    /** 计量单位 */
    @NotBlank(message = "请填写单位")
    private String unit;

    /** 1 启用 0 停用（编辑时可选） */
    private Integer enabled;
}
