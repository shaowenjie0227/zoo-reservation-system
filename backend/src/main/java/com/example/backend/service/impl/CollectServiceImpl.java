package com.example.backend.service.impl;

import com.example.backend.entity.*;
import com.example.backend.entity.vo.collect.CollectVO;
import com.example.backend.entity.vo.user.UserLoginVO;
import com.example.backend.mapper.CollectMapper;
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
 * 用户收藏表 服务实现类
 * </p>
 *
 * @author
 */
@Service
public class CollectServiceImpl extends ServiceImpl<CollectMapper, Collect> implements ICollectService {
    @Resource
    private CollectMapper collectMapper;
    @Resource
    private IUserService userService;
    @Resource
    private IAnimalService animalService;
    @Resource
    private IAnimalLocationService animalLocationService;
    @Resource
    private IForumPostService forumPostService;
    @Resource
    private ITravelService travelService;
    @Resource
    private IWeightService weightService;
    @Resource
    private IAnimalWeightService animalWeightService;

    /**
     * 新增
     *
     * @param request
     * @return
     */
    @Override
    public Integer add(Collect request) {
        // 参数校验
        if (request.getUserId() == null) {
            throw new BusinessException(CodeEnum.PARAMS_ERROR, "用户ID不能为空");
        }
        if (CharSequenceUtil.isBlank(request.getType())) {
            throw new BusinessException(CodeEnum.PARAMS_ERROR, "收藏类型不能为空");
        }
        if (request.getTargetId() == null) {
            throw new BusinessException(CodeEnum.PARAMS_ERROR, "目标ID不能为空");
        }

        // 检查是否已收藏
        QueryWrapper<Collect> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", request.getUserId())
                   .eq("type", request.getType())
                   .eq("target_id", request.getTargetId());
        if (count(queryWrapper) > 0) {
            throw new BusinessException(CodeEnum.PARAMS_ERROR, "已收藏该内容");
        }
        Collect saveData = new Collect();
        BeanUtils.copyProperties(request, saveData);
        collectMapper.insert(saveData);
        Integer res = saveData.getId();
        // 添加收藏权重记录（3分）
        if (res != null && "travel".equals(request.getType())) {
            try {
                UserLoginVO currentUser = userService.getCurrentUserInfo();
                if (currentUser != null) {
                    Weight weight = new Weight();
                    weight.setUserId(currentUser.getId());
                    weight.setTravelId(request.getTargetId());
                    weight.setBehavior("collect");
                    weight.setScore(3);
                    weightService.add(weight);
                }
            } catch (Exception e) {
                // 权重记录失败不影响主要功能，记录日志即可
                System.err.println("添加收藏权重记录失败: " + e.getMessage());
            }
        } else if ("animal".equals(request.getType())) {
            try {
                UserLoginVO currentUser = userService.getCurrentUserInfo();
                if (currentUser != null) {
                    AnimalWeight animalWeight = new AnimalWeight();
                    animalWeight.setUserId(currentUser.getId());
                    animalWeight.setAnimalId(request.getTargetId());
                    animalWeight.setBehavior("collect");
                    animalWeight.setScore(3);
                    animalWeightService.add(animalWeight);
                }
            } catch (Exception e) {
                // 权重记录失败不影响主要功能，记录日志即可
                System.err.println("添加收藏权重记录失败: " + e.getMessage());
            }
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
    public Boolean batchAdd(List<Collect> request) {
        if (request == null || request.isEmpty()) {
            throw new BusinessException(CodeEnum.PARAMS_ERROR, "批量新增数据不能为空");
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
        if (id == null) {
            throw new BusinessException(CodeEnum.PARAMS_ERROR, "ID不能为空");
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
            throw new BusinessException(CodeEnum.PARAMS_ERROR, "批量删除ID列表不能为空");
        }
        return removeByIds(ids);
    }

    /**
     * 编辑
     *
     * @param id      id
     * @param request 请求参数
     * @return
     */
    @Override
    public Boolean edit(Integer id, Collect request) {
        if (id == null) {
            throw new BusinessException(CodeEnum.PARAMS_ERROR, "ID不能为空");
        }
        Collect existData = getById(id);
        if (existData == null) {
            throw new BusinessException(CodeEnum.PARAMS_ERROR, "数据不存在");
        }
        Collect updateData = new Collect();
        BeanUtils.copyProperties(request, updateData);
        updateData.setId(id);
        return updateById(updateData);
    }

    /**
     * 分页查询
     *
     * @return
     */
    @Override
    public PageResult<List<CollectVO>> queryPage(Integer pageNum, Integer pageSize, String type) {
        QueryWrapper<Collect> queryWrapper = new QueryWrapper<>();
        if (CharSequenceUtil.isNotBlank(type)) {
            queryWrapper.eq("type", type);
        }
        // ID 降序
        queryWrapper.orderByDesc("id");
        Page<Collect> page = page(new Page<>(pageNum, pageSize), queryWrapper);
        // 返回结果
        List<CollectVO> collectVOS = convertVO(page.getRecords());
        return new PageResult<>(collectVOS, page.getCurrent(), page.getSize(), page.getTotal());
    }

    /**
     * 查询全部
     *
     * @return
     */
    @Override
    public List<Collect> getAll() {
        QueryWrapper<Collect> queryWrapper = new QueryWrapper<>();
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
    public Collect getByIdDetail(Integer id) {
        return getById(id);
    }

    /**
     * 检查用户是否已收藏
     *
     * @param userId   用户ID
     * @param type     收藏类型
     * @param targetId 目标ID
     * @return 是否已收藏
     */
    @Override
    public Boolean isCollected(Integer userId, String type, Integer targetId) {
        QueryWrapper<Collect> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId)
                   .eq("type", type)
                   .eq("target_id", targetId);
        return count(queryWrapper) > 0;
    }

    /**
     * 取消收藏
     *
     * @param userId   用户ID
     * @param type     收藏类型
     * @param targetId 目标ID
     * @return 是否成功
     */
    @Override
    public Boolean cancelCollect(Integer userId, String type, Integer targetId) {
        QueryWrapper<Collect> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId)
                   .eq("type", type)
                   .eq("target_id", targetId);

        Boolean res = remove(queryWrapper);
        // 删除收藏权重记录
        if (res && "travel".equals(type)) {
            try {
                weightService.deleteByUserIdAndTravelIdAndBehavior(userId, targetId, "collect");
            } catch (Exception e) {
                // 权重记录删除失败不影响主要功能，记录日志即可
                System.err.println("删除收藏权重记录失败: " + e.getMessage());
            }
        } else if ("animal".equals(type)) {
            try {
                weightService.deleteByUserIdAndTravelIdAndBehavior(userId, targetId, "collect");
            } catch (Exception e) {
                // 权重记录删除失败不影响主要功能，记录日志即可
                System.err.println("删除收藏权重记录失败: " + e.getMessage());
            }
        }
        return res;
    }

    /**
     * 转换为VO
     *
     * @param collectList
     * @return
     */
    private List<CollectVO> convertVO(List<Collect> collectList) {
        List<CollectVO> collectVOS = new ArrayList<>();
        for (Collect collect : collectList) {
            CollectVO collectVO = new CollectVO();
            BeanUtils.copyProperties(collect, collectVO);

            // 设置用户昵称
            User user = userService.getById(collect.getUserId());
            if (user != null) {
                collectVO.setUserNickName(user.getNickName());
            }

            // 设置目标名称和类型名称
            collectVO.setTargetName(getTargetName(collect.getType(), collect.getTargetId()));
            collectVO.setTypeName(getTypeName(collect.getType()));

            collectVOS.add(collectVO);
        }
        return collectVOS;
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
}