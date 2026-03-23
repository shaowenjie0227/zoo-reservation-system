package com.example.backend.service.impl;

import cn.hutool.core.text.CharSequenceUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.backend.common.model.PageResult;
import com.example.backend.entity.ForumPost;
import com.example.backend.entity.PostsComment;
import com.example.backend.entity.User;
import com.example.backend.entity.vo.postscomment.PostsCommentVO;
import com.example.backend.entity.vo.user.UserLoginVO;
import com.example.backend.mapper.ForumPostMapper;
import com.example.backend.mapper.PostsCommentMapper;
import com.example.backend.service.IPostsCommentService;
import com.example.backend.service.IUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 帖子评论表 服务实现类
 * </p>
 *
 * @Author 
 */
@Service
public class PostsCommentServiceImpl extends ServiceImpl<PostsCommentMapper, PostsComment> implements IPostsCommentService {
    @Resource
    private PostsCommentMapper postsCommentMapper;
    @Resource
    private IUserService userService;
    @Resource
    private ForumPostMapper postsMapper;

    /**
     * 新增
     *
     * @param request
     * @return
     */
    @Override
    public Integer add(PostsComment request) {
        PostsComment saveData = new PostsComment();
        BeanUtils.copyProperties(request, saveData);
        // 获取当前登录用户信息
        UserLoginVO userInfo = userService.getCurrentUserInfo();
        if (userInfo != null) {
            saveData.setUserId(userInfo.getId());
        }
        postsCommentMapper.insert(saveData);
        return saveData.getId();
    }

    /**
     * 批量新增
     *
     * @param request
     * @return
     */
    @Override
    public Boolean batchAdd(List<PostsComment> request) {
        return saveBatch(request);
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @Override
    public Boolean del(Integer id) {
        return removeById(id);
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @Override
    public Boolean batchDel(List<Integer> ids) {
        return removeByIds(ids);
    }

    /**
     * 编辑
     *
     * @param id
     * @param request
     * @return
     */
    @Override
    public Boolean edit(Integer id, PostsComment request) {
        PostsComment updateData = new PostsComment();
        BeanUtils.copyProperties(request, updateData);
        updateData.setId(id);
        return updateById(updateData);
    }

    /**
     * 分页条件查询
     *
     * @param pageNum
     * @param pageSize
     * @param content
     * @return
     */
    @Override
    public PageResult<List<PostsCommentVO>> queryPage(Integer pageNum, Integer pageSize, String content, String userId) {
        QueryWrapper<PostsComment> queryWrapper = new QueryWrapper<>();
        if (CharSequenceUtil.isNotBlank(content)) {
            queryWrapper.like("content", content);
        }
        if (CharSequenceUtil.isNotBlank(userId)) {
            queryWrapper.like("user_id", userId);
        }
        queryWrapper.orderByDesc("create_time");
        Page<PostsComment> page = new Page<>(pageNum, pageSize);
        Page<PostsComment> result = postsCommentMapper.selectPage(page, queryWrapper);
        
        List<PostsCommentVO> voList = convertToVOList(result.getRecords());
        return new PageResult<>(voList, page.getCurrent(), page.getSize(), page.getTotal());
    }

    /**
     * 查询全部
     *
     * @return
     */
    @Override
    public List<PostsComment> getAll() {
        return list();
    }

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    @Override
    public PostsComment getByIdDetail(Integer id) {
        return getById(id);
    }

    /**
     * 根据帖子ID获取评论列表（分级结构）
     *
     * @param postsId
     * @return
     */
    @Override
    public List<PostsCommentVO> getByPostsId(Integer postsId) {
        QueryWrapper<PostsComment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("posts_id", postsId);
        queryWrapper.orderByAsc("create_time");
        List<PostsComment> commentList = postsCommentMapper.selectList(queryWrapper);
        List<PostsCommentVO> voList = convertToVOList(commentList);
        return voList;
    }

    /**
     * 转换为VO列表
     *
     * @param commentList
     * @return
     */
    private List<PostsCommentVO> convertToVOList(List<PostsComment> commentList) {
        if (commentList == null || commentList.isEmpty()) {
            return new ArrayList<>();
        }

        // 获取所有用户ID和帖子ID
        List<Integer> userIds = commentList.stream()
                .map(PostsComment::getUserId)
                .distinct()
                .collect(Collectors.toList());
        
        List<Integer> postsIds = commentList.stream()
                .map(PostsComment::getPostsId)
                .distinct()
                .collect(Collectors.toList());

        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.in("id", userIds);
        // 批量查询用户信息
        Map<Integer, User> userMap = userService.list(userQueryWrapper).stream()
                .collect(Collectors.toMap(User::getId, user -> user));
        
        // 批量查询帖子信息
        Map<Integer, ForumPost> postsMap = postsMapper.selectBatchIds(postsIds).stream()
                .collect(Collectors.toMap(ForumPost::getId, posts -> posts));

        // 转换为VO
        return commentList.stream().map(comment -> {
            PostsCommentVO vo = new PostsCommentVO();
            BeanUtils.copyProperties(comment, vo);

            // 设置用户信息
            User user = userMap.get(comment.getUserId());
            if (user != null) {
                vo.setUserName(user.getNickName());
                vo.setUserAvatar(user.getAvatar());
            }

            // 设置帖子信息
            ForumPost posts = postsMap.get(comment.getPostsId());
            if (posts != null) {
                vo.setPostsTitle(posts.getTitle());
            }
            return vo;
        }).collect(Collectors.toList());
    }
    
    /**
     * 构建评论树形结构
     *
     * @param commentList 评论列表
     * @return 树形结构的评论列表
     */
    private List<PostsCommentVO> buildCommentTree(List<PostsCommentVO> commentList) {
        if (commentList == null || commentList.isEmpty()) {
            return new ArrayList<>();
        }
        
        // 创建ID到评论的映射
        Map<Integer, PostsCommentVO> commentMap = commentList.stream()
                .collect(Collectors.toMap(PostsCommentVO::getId, comment -> comment));
        
        // 根评论列表（pid为null或0的评论）
        List<PostsCommentVO> rootComments = new ArrayList<>();
        
        for (PostsCommentVO comment : commentList) {
            Integer pid = comment.getPid();
            if (pid == null || pid == 0) {
                // 根评论
                rootComments.add(comment);
            } else {
                // 子评论，找到父评论并添加到其children列表中
                PostsCommentVO parentComment = commentMap.get(pid);
                if (parentComment != null) {
                    if (parentComment.getChildren() == null) {
                        parentComment.setChildren(new ArrayList<>());
                    }
                    parentComment.getChildren().add(comment);
                }
            }
        }
        
        return rootComments;
    }
}