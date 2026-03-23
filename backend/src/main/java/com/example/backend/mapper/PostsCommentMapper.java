package com.example.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.backend.entity.PostsComment;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 帖子评论表 Mapper 接口
 * </p>
 *
 * @Author 
 */
@Mapper
public interface PostsCommentMapper extends BaseMapper<PostsComment> {

}