package com.fast.take.domain;

import java.math.BigDecimal;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;

/**
 * 包裹规格对象 size
 *
 * @author fast
 * @date 2025-12-30
 */

@Data
public class Size
{

    /** 包裹规格ID */
    private String sizeId;

    /** 规格名称 */
    private String name;

    /** 描述 */
    private String description;

    /** 加价 */
    private BigDecimal price;


}
