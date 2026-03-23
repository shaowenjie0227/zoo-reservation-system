package com.example.backend.entity.vo.travel;

import com.example.backend.entity.Travel;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author 
 * @Description 旅游景点VO
 * @Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "TravelVO对象", description = "旅游景点视图对象")
public class TravelVO extends Travel {
    // 如果需要额外的字段，可以在这里添加
    // 例如：关联的用户信息、统计信息等
}