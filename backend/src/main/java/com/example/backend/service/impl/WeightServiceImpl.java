package com.example.backend.service.impl;

import com.example.backend.entity.Weight;
import com.example.backend.entity.Travel;
import com.example.backend.entity.User;
import com.example.backend.entity.vo.weight.WeightVO;
import com.example.backend.entity.vo.user.UserLoginVO;
import com.example.backend.mapper.WeightMapper;
import com.example.backend.mapper.TravelMapper;
import com.example.backend.mapper.UserMapper;
import com.example.backend.service.IWeightService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.backend.service.IUserService;
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
 * 权重表 服务实现类
 * </p>
 *
 * @author
 */
@Service
public class WeightServiceImpl extends ServiceImpl<WeightMapper, Weight> implements IWeightService {
    @Resource
    private WeightMapper weightMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private TravelMapper travelMapper;

    /**
     * 新增
     *
     * @param request
     * @return
     */
    @Override
    public Integer add(Weight request) {
        Weight saveData = new Weight();
        BeanUtils.copyProperties(request, saveData);
        weightMapper.insert(saveData);
        return saveData.getId();
    }

    /**
     * 批量新增
     *
     * @param request
     * @return
     */
    @Override
    public Boolean batchAdd(List<Weight> request) {
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
     * @param request
     * @return
     */
    @Override
    public Boolean edit(Weight request) {
        return updateById(request);
    }

    /**
     * 分页查询
     *
     * @return
     */
    @Override
    public PageResult<List<WeightVO>> queryPage(Integer pageNum, Integer pageSize, String behavior, Integer userId, Integer travelId) {
        QueryWrapper<Weight> queryWrapper = new QueryWrapper<>();
        if (userId != null) {
            queryWrapper.eq("user_id", userId);
        }
        if (travelId != null) {
            queryWrapper.eq("travel_id", travelId);
        }
        if (CharSequenceUtil.isNotBlank(behavior)) {
            queryWrapper.eq("behavior", behavior);
        }
        // ID 降序
        queryWrapper.orderByDesc("id");
        Page<Weight> page = page(new Page<>(pageNum, pageSize), queryWrapper);
        // 返回结果
        List<WeightVO> weightVOS = convertVO(page.getRecords());
        return new PageResult<>(weightVOS, page.getCurrent(), page.getSize(), page.getTotal());
    }

    /**
     * 查询全部
     *
     * @return
     */
    @Override
    public List<Weight> getAll() {
        return list();
    }

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    @Override
    public Weight getByIdDetail(Integer id) {
        return getById(id);
    }

    @Override
    public void deleteByUserIdAndTravelIdAndBehavior(Integer userId, Integer targetId, String collect) {
        QueryWrapper<Weight> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId)
                .eq("travel_id", targetId)
                .eq("behavior", collect);
        remove(queryWrapper);
    }

    /**
     * 转换为VO对象
     *
     * @param weights 实体列表
     * @return VO列表
     */
    private List<WeightVO> convertVO(List<Weight> weights) {
        List<WeightVO> weightVOList = new ArrayList<>();
        for (Weight weight : weights) {
            WeightVO weightVO = new WeightVO();
            BeanUtils.copyProperties(weight, weightVO);

            // 查询用户信息
            if (weight.getUserId() != null) {
                User user = userMapper.selectById(weight.getUserId());
                if (user != null) {
                    weightVO.setUserNickName(user.getNickName());
                }
            }

            // 查询景点信息
            if (weight.getTravelId() != null) {
                Travel travel = travelMapper.selectById(weight.getTravelId());
                if (travel != null) {
                    weightVO.setTravelName(travel.getName());
                    weightVO.setTravelLocation(travel.getLocation());
                }
            }

            weightVOList.add(weightVO);
        }
        return weightVOList;
    }
}