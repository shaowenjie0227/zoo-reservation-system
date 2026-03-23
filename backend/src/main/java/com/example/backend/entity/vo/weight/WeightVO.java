package com.example.backend.entity.vo.weight;

import com.baomidou.mybatisplus.annotation.TableField;
import com.example.backend.entity.Weight;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author 
 * @Description 权重VO类
 * @Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "WeightVO对象", description = "权重视图对象")
public class WeightVO extends Weight {
    
    @TableField(exist = false)
    @ApiModelProperty(value = "用户昵称")
    private String userNickName;
    
    @TableField(exist = false)
    @ApiModelProperty(value = "景点名称")
    private String travelName;
    
    @TableField(exist = false)
    @ApiModelProperty(value = "景点位置")
    private String travelLocation;
}