package com.example.backend.entity.vo.postscomment;

import com.baomidou.mybatisplus.annotation.TableField;
import com.example.backend.entity.PostsComment;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @Author 
 * @Description 帖子评论VO类
 * @Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PostsCommentVO extends PostsComment {
    
    /**
     * 评论用户昵称
     */
    @TableField(exist = false)
    private String userName;
    
    /**
     * 评论用户头像
     */
    @TableField(exist = false)
    private String userAvatar;
    
    /**
     * 帖子标题
     */
    @TableField(exist = false)
    private String postsTitle;
    
    /**
     * 子评论列表（回复）
     */
    @TableField(exist = false)
    private List<PostsCommentVO> children;
}