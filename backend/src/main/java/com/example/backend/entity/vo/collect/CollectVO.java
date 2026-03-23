package com.example.backend.entity.vo.collect;

import com.baomidou.mybatisplus.annotation.TableField;
import com.example.backend.entity.Collect;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author 
 * @Description 收藏VO
 * @Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "CollectVO对象", description = "收藏视图对象")
public class CollectVO extends Collect {

    @TableField(exist = false)
    @ApiModelProperty(value = "用户昵称")
    private String userNickName;

    @TableField(exist = false)
    @ApiModelProperty(value = "目标名称")
    private String targetName;

    @TableField(exist = false)
    @ApiModelProperty(value = "类型名称")
    private String typeName;
}