package com.example.backend.entity.vo.animalweight;

import com.baomidou.mybatisplus.annotation.TableField;
import com.example.backend.entity.AnimalWeight;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author 
 * @Description 动物权重VO类
 * @Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "AnimalWeightVO对象", description = "动物权重视图对象")
public class AnimalWeightVO extends AnimalWeight {
    
    @TableField(exist = false)
    @ApiModelProperty(value = "用户昵称")
    private String userNickName;
    
    @TableField(exist = false)
    @ApiModelProperty(value = "动物名称")
    private String animalName;
    
    @TableField(exist = false)
    @ApiModelProperty(value = "动物描述")
    private String animalDescription;
}