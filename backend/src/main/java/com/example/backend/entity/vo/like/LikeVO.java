package com.example.backend.entity.vo.like;

import com.baomidou.mybatisplus.annotation.TableField;
import com.example.backend.entity.Like;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author 
 * @Description 点赞VO
 * @Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "LikeVO对象", description = "点赞视图对象")
public class LikeVO extends Like {

    @ApiModelProperty(value = "用户昵称")
    private String userNickName;

    @ApiModelProperty(value = "目标名称")
    private String targetName;

    @ApiModelProperty(value = "类型名称")
    private String typeName;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "图片链接")
    private String imgUrl;
}