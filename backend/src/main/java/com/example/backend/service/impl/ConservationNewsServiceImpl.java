package com.example.backend.service.impl;

import com.example.backend.entity.ConservationNews;
import com.example.backend.entity.vo.conservationnews.ConservationNewsVO;
import com.example.backend.mapper.ConservationNewsMapper;
import com.example.backend.service.IConservationNewsService;
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
 * 生态保护新闻表 服务实现类
 * </p>
 *
 * @author
 */
@Service
public class ConservationNewsServiceImpl extends ServiceImpl<ConservationNewsMapper, ConservationNews> implements IConservationNewsService {
    @Resource
    private ConservationNewsMapper conservationNewsMapper;

    /**
     * 新增
     *
     * @param request
     * @return
     */
    @Override
    public Integer add(ConservationNews request) {
        ConservationNews saveData = new ConservationNews();
        BeanUtils.copyProperties(request, saveData);
        // 设置默认浏览数为0
        if (saveData.getViewCount() == null) {
            saveData.setViewCount(0);
        }
        // 设置默认状态为正常
        if (saveData.getStatus() == null) {
            saveData.setStatus(1);
        }
        conservationNewsMapper.insert(saveData);
        return saveData.getId();
    }

    /**
     * 批量新增
     *
     * @param request
     * @return
     */
    @Override
    public Boolean batchAdd(List<ConservationNews> request) {
        request.forEach(item -> {
            // 设置默认浏览数为0
            if (item.getViewCount() == null) {
                item.setViewCount(0);
            }
            // 设置默认状态为正常
            if (item.getStatus() == null) {
                item.setStatus(1);
            }
        });
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
            throw new BusinessException(CodeEnum.PARAMS_ERROR);
        }
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
    public Boolean edit(ConservationNews request) {
        return updateById(request);
    }

    /**
     * 分页查询
     *
     * @return
     */
    @Override
    public PageResult<List<ConservationNewsVO>> queryPage(Integer pageNum, Integer pageSize, String title, String type, Integer status) {
        QueryWrapper<ConservationNews> queryWrapper = new QueryWrapper<>();
        if (CharSequenceUtil.isNotBlank(title)) {
            queryWrapper.like("title", title);
        }
        if (CharSequenceUtil.isNotBlank(type)) {
            queryWrapper.eq("type", type);
        }
        if (status != null) {
            queryWrapper.eq("status", status);
        }
        // ID 降序
        queryWrapper.orderByDesc("id");
        Page<ConservationNews> page = page(new Page<>(pageNum, pageSize), queryWrapper);
        // 返回结果
        List<ConservationNewsVO> conservationNewsVOS = convertVO(page.getRecords());
        return new PageResult<>(conservationNewsVOS, page.getCurrent(), page.getSize(), page.getTotal());
    }

    /**
     * 查询全部
     *
     * @return
     */
    @Override
    public List<ConservationNews> getAll() {
        return list();
    }

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    @Override
    public ConservationNews getByIdDetail(Integer id) {
        this.increaseViewCount(id);
        return getById(id);
    }

    /**
     * 增加浏览数
     *
     * @param id
     * @return
     */
    @Override
    public Boolean increaseViewCount(Integer id) {
        ConservationNews news = getById(id);
        if (news != null) {
            news.setViewCount(news.getViewCount() + 1);
            return updateById(news);
        }
        return false;
    }

    // 转换VO
    private List<ConservationNewsVO> convertVO(List<ConservationNews> newsList) {
        ArrayList<ConservationNewsVO> list = new ArrayList<>();
        for (ConservationNews news : newsList) {
            ConservationNewsVO newsVO = new ConservationNewsVO();
            BeanUtils.copyProperties(news, newsVO);
            list.add(newsVO);
        }
        return list;
    }
}