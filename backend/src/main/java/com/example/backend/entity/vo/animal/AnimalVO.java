package com.example.backend.entity.vo.animal;

import com.baomidou.mybatisplus.annotation.TableField;
import com.example.backend.entity.Animal;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author 
 * @Description 动物信息VO
 * @Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AnimalVO extends Animal {
    @TableField(exist = false)
    private String typeName;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "图片链接")
    private String coverUrl;

    @ApiModelProperty(value = "简介")
    private String intro;

    @ApiModelProperty(value = "最佳动物季节")
    private String season;

    @ApiModelProperty(value = "浏览量")
    private Integer viewCount;

    @ApiModelProperty(value = "标签数组")
    private String tags;
}