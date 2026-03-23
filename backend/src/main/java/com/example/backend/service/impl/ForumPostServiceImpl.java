package com.example.backend.service.impl;

import com.example.backend.entity.ForumPost;
import com.example.backend.entity.User;
import com.example.backend.entity.Animal;
import com.example.backend.entity.AnimalLocation;
import com.example.backend.entity.PostCategory;
import com.example.backend.entity.vo.forumpost.ForumPostVO;
import com.example.backend.entity.vo.user.UserLoginVO;
import com.example.backend.mapper.ForumPostMapper;
import com.example.backend.service.IForumPostService;
import com.example.backend.service.IUserService;
import com.example.backend.service.IAnimalService;
import com.example.backend.service.IAnimalLocationService;
import com.example.backend.service.IPostCategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import cn.hutool.core.text.CharSequenceUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.backend.common.model.PageResult;
import com.example.backend.exception.BusinessException;
import org.springframework.beans.BeanUtils;

import javax.annotation.Resource;

import com.example.backend.common.enums.CodeEnum;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 论坛帖子表 服务实现类
 * </p>
 *
 * @author
 */
@Service
public class ForumPostServiceImpl extends ServiceImpl<ForumPostMapper, ForumPost> implements IForumPostService {
    @Resource
    private ForumPostMapper forumPostMapper;
    @Resource
    private IUserService userService;
    @Resource
    private IAnimalService animalService;
    @Resource
    private IAnimalLocationService animalLocationService;
    @Resource
    private IPostCategoryService postCategoryService;

    /**
     * 新增
     *
     * @param request
     * @return
     */
    @Override
    public Integer add(ForumPost request) {
        ForumPost saveData = new ForumPost();
        BeanUtils.copyProperties(request, saveData);
        UserLoginVO currentUserInfo = userService.getCurrentUserInfo();
        if (currentUserInfo != null) {
            saveData.setUserId(currentUserInfo.getId());
        }
        forumPostMapper.insert(saveData);
        return saveData.getId();
    }

    /**
     * 批量新增
     *
     * @param request
     * @return
     */
    @Override
    public Boolean batchAdd(List<ForumPost> request) {
        // 获取当前登录用户信息
        UserLoginVO currentUserInfo = userService.getCurrentUserInfo();
        if (currentUserInfo != null) {
            request.forEach(item -> {
                item.setUserId(currentUserInfo.getId());
            });
        }
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
        if (id == null || id <= 0) {
            throw new BusinessException(CodeEnum.PARAMS_ERROR);
        }
        return removeById(id);
    }

    /**
     * 批量删除
     *
     * @param ids 批量ID列表
     * @return 是否成功
     */
    @Override
    public Boolean batchDel(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new BusinessException(CodeEnum.PARAMS_ERROR);
        }
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
    public Boolean edit(Integer id, ForumPost request) {
        ForumPost findData = forumPostMapper.selectById(id);
        if (findData == null) {
            throw new BusinessException(CodeEnum.SYSTEM_ERROR);
        }
        BeanUtils.copyProperties(request, findData);
        return updateById(findData);
    }

    /**
     * 分页查询
     *
     * @return
     */
    @Override
    public PageResult<List<ForumPostVO>> queryPage(Integer pageNum, Integer pageSize, String title, String userId, Integer categoryId) {
        QueryWrapper<ForumPost> queryWrapper = new QueryWrapper<>();
        if (CharSequenceUtil.isNotBlank(title)) {
            queryWrapper.like("title", title);
        }
        if (CharSequenceUtil.isNotBlank(userId)) {
            queryWrapper.eq("user_id", userId);
        }
        if (categoryId != null) {
            queryWrapper.eq("category_id", categoryId);
        }
        // ID 降序
        queryWrapper.orderByDesc("id");
        Page<ForumPost> page = page(new Page<>(pageNum, pageSize), queryWrapper);
        // 返回结果
        List<ForumPostVO> forumPostVOS = convertVO(page.getRecords());
        return new PageResult<>(forumPostVOS, page.getCurrent(), page.getSize(), page.getTotal());
    }

    /**
     * 查询全部
     *
     * @return
     */
    @Override
    public List<ForumPost> getAll() {
        return list();
    }

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    @Override
    public ForumPost getByIdDetail(Integer id) {
        return getById(id);
    }

    /**
     * 根据id查询详情VO
     *
     * @param id
     * @return
     */
    public ForumPostVO getByIdDetailVO(Integer id) {
        ForumPost forumPost = getById(id);
        if (forumPost == null) {
            return null;
        }
        // 浏览 + 1
        if (forumPost.getViewCount() == null) {
            forumPost.setViewCount(0);
        }
        forumPost.setViewCount(forumPost.getViewCount() + 1);
        updateById(forumPost);
        return convertSingleVO(forumPost);
    }

    // 转换单个VO
    private ForumPostVO convertSingleVO(ForumPost forumPost) {
        ForumPostVO forumPostVO = new ForumPostVO();
        BeanUtils.copyProperties(forumPost, forumPostVO);
        
        // 获取用户信息
        if (forumPost.getUserId() != null) {
            User user = userService.getById(forumPost.getUserId());
            if (user != null) {
                forumPostVO.setUserNickName(user.getNickName());
            }
        }
        
        // 获取动物信息
        if (forumPost.getAnimalId() != null) {
            Animal animal = animalService.getById(forumPost.getAnimalId());
            if (animal != null) {
                forumPostVO.setAnimalName(animal.getName());
            }
        }
        
        // 获取动物地点信息
        if (forumPost.getLocationId() != null) {
            AnimalLocation location = animalLocationService.getById(forumPost.getLocationId());
            if (location != null) {
                forumPostVO.setLocationName(location.getName());
            }
        }

        // 获取分类信息
        if (forumPost.getCategoryId() != null) {
            PostCategory category = postCategoryService.getById(forumPost.getCategoryId());
            if (category != null) {
                forumPostVO.setCategoryName(category.getName());
            }
        }
        
        return forumPostVO;
    }

    // 转换VO
    private List<ForumPostVO> convertVO(List<ForumPost> forumPosts) {
        ArrayList<ForumPostVO> list = new ArrayList<>();
        for (ForumPost forumPost : forumPosts) {
            ForumPostVO forumPostVO = new ForumPostVO();
            BeanUtils.copyProperties(forumPost, forumPostVO);
            
            // 获取用户信息
            if (forumPost.getUserId() != null) {
                User user = userService.getById(forumPost.getUserId());
                if (user != null) {
                    forumPostVO.setUserNickName(user.getNickName());
                }
            }
            
            // 获取动物信息
            if (forumPost.getAnimalId() != null) {
                Animal animal = animalService.getById(forumPost.getAnimalId());
                if (animal != null) {
                    forumPostVO.setAnimalName(animal.getName());
                }
            }
            
            // 获取动物地点信息
            if (forumPost.getLocationId() != null) {
                AnimalLocation location = animalLocationService.getById(forumPost.getLocationId());
                if (location != null) {
                    forumPostVO.setLocationName(location.getName());
                }
            }

            // 获取分类信息
            if (forumPost.getCategoryId() != null) {
                PostCategory category = postCategoryService.getById(forumPost.getCategoryId());
                if (category != null) {
                    forumPostVO.setCategoryName(category.getName());
                }
            }
            
            list.add(forumPostVO);
        }
        return list;
    }
}