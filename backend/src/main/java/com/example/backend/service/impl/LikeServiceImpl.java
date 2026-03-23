package com.example.backend.service.impl;

import com.example.backend.entity.*;
import com.example.backend.entity.vo.like.LikeVO;
import com.example.backend.entity.vo.user.UserLoginVO;
import com.example.backend.mapper.LikeMapper;
import com.example.backend.service.*;
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
 * 点赞表 服务实现类
 * </p>
 *
 * @author
 */
@Service
public class LikeServiceImpl extends ServiceImpl<LikeMapper, Like> implements ILikeService {
    @Resource
    private LikeMapper likeMapper;
    @Resource
    private IUserService userService;
    @Resource
    private IAnimalService animalService;
    @Resource
    private ITravelService travelService;
    @Resource
    private IAnimalLocationService animalLocationService;
    @Resource
    private IForumPostService forumPostService;
    @Resource
    private IWeightService weightService;
    @Resource
    private IAnimalWeightService animalWeightService;

    /**
     * 新增点赞
     *
     * @param request
     * @return
     */
    @Override
    public Integer add(Like request) {
        Like saveData = new Like();
        BeanUtils.copyProperties(request, saveData);
        likeMapper.insert(saveData);
        Integer res = saveData.getId();
        // 添加点赞权重记录（2分）
        if (res == null) {
            throw new BusinessException(CodeEnum.SYSTEM_ERROR);
        }
        UserLoginVO currentUser = userService.getCurrentUserInfo();
        if (currentUser == null) {
            throw new BusinessException(CodeEnum.NOT_LOGIN);
        }
        if ("travel".equals(request.getType())) {
            Weight weight = new Weight();
            weight.setUserId(currentUser.getId());
            weight.setTravelId(request.getTargetId());
            weight.setBehavior("like");
            weight.setScore(2);
            weightService.add(weight);
        } else if ("animal".equals(request.getType())) {
            AnimalWeight weight = new AnimalWeight();
            weight.setUserId(currentUser.getId());
            weight.setAnimalId(request.getTargetId());
            weight.setBehavior("like");
            weight.setScore(2);
            animalWeightService.add(weight);
        }
        return res;
    }

    /**
     * 批量新增
     *
     * @param request
     * @return
     */
    @Override
    public Boolean batchAdd(List<Like> request) {
        return saveBatch(request);
    }

    /**
     * 删除点赞
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
     * 编辑点赞
     *
     * @param id
     * @param request
     * @return
     */
    @Override
    public Boolean edit(Integer id, Like request) {
        Like findData = likeMapper.selectById(id);
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
    public PageResult<List<LikeVO>> queryPage(Integer pageNum, Integer pageSize, String type, Integer userId, Integer targetId) {
        QueryWrapper<Like> queryWrapper = new QueryWrapper<>();
        if (CharSequenceUtil.isNotBlank(type)) {
            queryWrapper.eq("type", type);
        }
        if (userId != null) {
            queryWrapper.eq("user_id", userId);
        }
        if (targetId != null) {
            queryWrapper.eq("target_id", targetId);
        }
        // ID 降序
        queryWrapper.orderByDesc("id");
        Page<Like> page = page(new Page<>(pageNum, pageSize), queryWrapper);
        // 返回结果
        List<LikeVO> likeVOS = convertVO(page.getRecords());
        return new PageResult<>(likeVOS, page.getCurrent(), page.getSize(), page.getTotal());
    }

    /**
     * 查询全部
     *
     * @return
     */
    @Override
    public List<Like> getAll() {
        QueryWrapper<Like> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        return list(queryWrapper);
    }

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    @Override
    public Like getByIdDetail(Integer id) {
        return getById(id);
    }

    /**
     * 检查用户是否已点赞
     *
     * @param userId 用户ID
     * @param type 类型
     * @param targetId 目标ID
     * @return 是否已点赞
     */
    @Override
    public Boolean checkUserLike(Integer userId, String type, Integer targetId) {
        QueryWrapper<Like> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId)
                   .eq("type", type)
                   .eq("target_id", targetId);
        return count(queryWrapper) > 0;
    }

    /**
     * 取消点赞
     *
     * @param userId 用户ID
     * @param type 类型
     * @param targetId 目标ID
     * @return 是否成功
     */
    @Override
    public Boolean cancelLike(Integer userId, String type, Integer targetId) {
        QueryWrapper<Like> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId)
                   .eq("type", type)
                   .eq("target_id", targetId);
        // 删除点赞记录
        Boolean res = remove(queryWrapper);

        // 删除点赞权重记录
        if (res && "travel".equals(type)) {
            try {
                weightService.deleteByUserIdAndTravelIdAndBehavior(userId, targetId, "like");
            } catch (Exception e) {
                // 权重记录删除失败不影响主要功能，记录日志即可
                System.err.println("删除点赞权重记录失败: " + e.getMessage());
            }
        } else if (res && "animal".equals(type)) {
            try {
                animalWeightService.deleteByUserIdAndAnimalIdAndBehavior(userId, targetId, "like");
            } catch (Exception e) {
                // 权重记录删除失败不影响主要功能，记录日志即可
                System.err.println("删除点赞权重记录失败: " + e.getMessage());
            }
        }

        return res;
    }

    // 转换VO
    private List<LikeVO> convertVO(List<Like> likes) {
        ArrayList<LikeVO> list = new ArrayList<>();
        for (Like like : likes) {
            LikeVO likeVO = new LikeVO();
            BeanUtils.copyProperties(like, likeVO);
            
            // 获取用户信息
            if (like.getUserId() != null) {
                User user = userService.getById(like.getUserId());
                if (user != null) {
                    likeVO.setUserNickName(user.getNickName());
                }
            }
            
            // 根据类型获取目标名称
            if (like.getType() != null && like.getTargetId() != null) {
                String targetName = getTargetName(like.getType(), like.getTargetId());
                likeVO.setTargetName(targetName);
                likeVO.setTypeName(getTypeName(like.getType()));
            }
            
            list.add(likeVO);
        }
        return list;
    }

    // 获取目标名称
    private String getTargetName(String type, Integer targetId) {
        switch (type) {
            case "animal":
                Animal animal = animalService.getById(targetId);
                return animal != null ? animal.getName() : "未知动物";
            case "location":
                AnimalLocation location = animalLocationService.getById(targetId);
                return location != null ? location.getName() : "未知地点";
            case "post":
                ForumPost post = forumPostService.getById(targetId);
                return post != null ? post.getTitle() : "未知帖子";
            case "travel":
                Travel travel = travelService.getById(targetId);
                return travel != null ? travel.getName() : "未知景点";
            default:
                return "未知类型";
        }
    }

    // 获取类型名称
    private String getTypeName(String type) {
        switch (type) {
            case "animal":
                return "动物";
            case "location":
                return "动物地点";
            case "post":
                return "论坛帖子";
            case "travel":
                return "旅游景点";
            default:
                return "未知类型";
        }
    }
}