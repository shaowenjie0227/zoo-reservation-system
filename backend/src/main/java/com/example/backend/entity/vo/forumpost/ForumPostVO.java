package com.example.backend.entity.vo.forumpost;

import com.baomidou.mybatisplus.annotation.TableField;
import com.example.backend.entity.ForumPost;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author 
 * @Description 论坛帖子VO
 * @Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "ForumPostVO对象", description = "论坛帖子视图对象")
public class ForumPostVO extends ForumPost {

    @TableField(exist = false)
    @ApiModelProperty(value = "用户昵称")
    private String userNickName;

    @TableField(exist = false)
    @ApiModelProperty(value = "动物名称")
    private String animalName;

    @TableField(exist = false)
    @ApiModelProperty(value = "动物地点名称")
    private String locationName;

    @TableField(exist = false)
    @ApiModelProperty(value = "分类名称")
    private String categoryName;
}