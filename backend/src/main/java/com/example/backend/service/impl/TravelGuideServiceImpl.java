package com.example.backend.service.impl;

import com.example.backend.entity.TravelGuide;
import com.example.backend.entity.User;
import com.example.backend.entity.Travel;
import com.example.backend.entity.vo.travelguide.TravelGuideVO;
import com.example.backend.entity.vo.user.UserLoginVO;
import com.example.backend.mapper.ITravelGuideMapper;
import com.example.backend.mapper.UserMapper;
import com.example.backend.mapper.TravelMapper;
import com.example.backend.service.ITravelGuideService;
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

import com.example.backend.service.ITravelGuideCategoryService;
import com.example.backend.entity.TravelGuideCategory;

/**
 * <p>
 * 旅游攻略表 服务实现类
 * </p>
 *
 * @author
 */
@Service
public class TravelGuideServiceImpl extends ServiceImpl<ITravelGuideMapper, TravelGuide> implements ITravelGuideService {
    @Resource
    private ITravelGuideMapper travelGuideMapper;
    
    @Resource
    private UserMapper userMapper;

    @Resource
    private IUserService userService;
    
    @Resource
    private TravelMapper travelMapper;

    @Resource
    private ITravelGuideCategoryService travelGuideCategoryService;

    /**
     * 新增
     *
     * @param request
     * @return
     */
    @Override
    public Integer add(TravelGuide request) {
        TravelGuide saveData = new TravelGuide();
        BeanUtils.copyProperties(request, saveData);
        UserLoginVO userInfo = userService.getCurrentUserInfo();
        saveData.setUserId(userInfo.getId());
        travelGuideMapper.insert(saveData);
        return saveData.getId();
    }

    /**
     * 批量新增
     *
     * @param request
     * @return
     */
    @Override
    public Boolean batchAdd(List<TravelGuide> request) {
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
     * @param request 请求参数
     * @return 是否成功
     */
    @Override
    public Boolean edit(TravelGuide request) {
        if (request.getId() == null || request.getId() <= 0) {
            throw new BusinessException(CodeEnum.PARAMS_ERROR);
        }
        return updateById(request);
    }

    /**
     * 分页查询
     *
     * @param pageNum  页码
     * @param pageSize 页大小
     * @param title    标题
     * @param travelId 景点ID
     * @return 分页结果
     */
    @Override
    public PageResult<List<TravelGuideVO>> queryPage(Integer pageNum, Integer pageSize, String title, String userId, Integer travelId, Integer categoryId) {
        QueryWrapper<TravelGuide> queryWrapper = new QueryWrapper<>();
        if (CharSequenceUtil.isNotBlank(title)) {
            queryWrapper.like("title", title);
        }
        if (CharSequenceUtil.isNotBlank(userId)) {
            queryWrapper.eq("user_id", userId);
        }
        if (travelId != null && travelId > 0) {
            queryWrapper.eq("travel_id", travelId);
        }
        if (categoryId != null && categoryId > 0) {
            queryWrapper.eq("category_id", categoryId);
        }
        queryWrapper.orderByDesc("id");
        Page<TravelGuide> page = new Page<>(pageNum, pageSize);
        Page<TravelGuide> result = travelGuideMapper.selectPage(page, queryWrapper);
        List<TravelGuideVO> travelGuideVOList = convertVO(result.getRecords());
        return new PageResult<>(travelGuideVOList, result.getCurrent(), result.getSize(), result.getTotal());
    }

    /**
     * 查询所有
     *
     * @return 所有数据
     */
    @Override
    public List<TravelGuide> getAll() {
        QueryWrapper<TravelGuide> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        return travelGuideMapper.selectList(queryWrapper);
    }

    /**
     * 根据ID查询详情
     *
     * @param id 主键ID
     * @return 详情信息
     */
    @Override
    public TravelGuide getByIdDetail(Integer id) {
        if (id == null || id <= 0) {
            throw new BusinessException(CodeEnum.PARAMS_ERROR);
        }
        TravelGuide travelGuide = travelGuideMapper.selectById(id);
        // 浏览 + 1
        travelGuide.setViewCount(travelGuide.getViewCount() + 1);
        this.updateById(travelGuide);
        return travelGuide;
    }

    /**
     * 转换为VO对象
     *
     * @param travelGuides 实体列表
     * @return VO列表
     */
    private List<TravelGuideVO> convertVO(List<TravelGuide> travelGuides) {
        List<TravelGuideVO> travelGuideVOList = new ArrayList<>();
        for (TravelGuide travelGuide : travelGuides) {
            TravelGuideVO travelGuideVO = new TravelGuideVO();
            BeanUtils.copyProperties(travelGuide, travelGuideVO);
            
            // 查询用户信息
            if (travelGuide.getUserId() != null) {
                User user = userMapper.selectById(travelGuide.getUserId());
                if (user != null) {
                    travelGuideVO.setUserNickName(user.getNickName());
                    travelGuideVO.setUserAvatar(user.getAvatar());
                }
            }
            
            // 查询景点信息
            if (travelGuide.getTravelId() != null) {
                Travel travel = travelMapper.selectById(travelGuide.getTravelId());
                if (travel != null) {
                    travelGuideVO.setTravelName(travel.getName());
                    travelGuideVO.setTravelLocation(travel.getLocation());
                }
            }

            // 查询分类信息
            if (travelGuide.getCategoryId() != null) {
                TravelGuideCategory category = travelGuideCategoryService.getById(travelGuide.getCategoryId());
                if (category != null) {
                    travelGuideVO.setCategoryName(category.getName());
                }
            }
            
            travelGuideVOList.add(travelGuideVO);
        }
        return travelGuideVOList;
    }
}