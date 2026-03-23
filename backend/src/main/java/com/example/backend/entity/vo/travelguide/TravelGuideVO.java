package com.example.backend.entity.vo.travelguide;

import com.baomidou.mybatisplus.annotation.TableField;
import com.example.backend.entity.TravelGuide;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author 
 * @Description 旅游攻略VO
 * @Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "TravelGuideVO对象", description = "旅游攻略视图对象")
public class TravelGuideVO extends TravelGuide {

    @TableField(exist = false)
    @ApiModelProperty(value = "用户昵称")
    private String userNickName;

    @TableField(exist = false)
    @ApiModelProperty(value = "用户头像")
    private String userAvatar;

    @TableField(exist = false)
    @ApiModelProperty(value = "景点名称")
    private String travelName;

    @TableField(exist = false)
    @ApiModelProperty(value = "景点位置")
    private String travelLocation;

    @TableField(exist = false)
    @ApiModelProperty(value = "分类名称")
    private String categoryName;
}