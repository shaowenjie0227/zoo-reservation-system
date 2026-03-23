package com.example.backend.service.impl;

import com.example.backend.entity.Report;
import com.example.backend.entity.User;
import com.example.backend.entity.vo.report.ReportVO;
import com.example.backend.entity.vo.user.UserLoginVO;
import com.example.backend.mapper.ReportMapper;
import com.example.backend.service.IReportService;
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
 * 举报表 服务实现类
 * </p>
 *
 * @author
 */
@Service
public class ReportServiceImpl extends ServiceImpl<ReportMapper, Report> implements IReportService {
    @Resource
    private ReportMapper reportMapper;
    @Resource
    private IUserService userService;

    /**
     * 新增
     *
     * @param request
     * @return
     */
    @Override
    public Integer add(Report request) {
        Report saveData = new Report();
        BeanUtils.copyProperties(request, saveData);
        // 如果不是匿名举报，设置用户ID
        UserLoginVO currentUserInfo = userService.getCurrentUserInfo();
        if (currentUserInfo != null) {
            saveData.setUserId(currentUserInfo.getId());
        }
        reportMapper.insert(saveData);
        return saveData.getId();
    }

    /**
     * 批量新增
     *
     * @param request
     * @return
     */
    @Override
    public Boolean batchAdd(List<Report> request) {
        // 获取当前登录用户信息
        UserLoginVO currentUserInfo = userService.getCurrentUserInfo();
        if (currentUserInfo != null) {
            request.forEach(item -> {
                // 如果不是匿名举报，设置用户ID
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
     * @param request
     * @return
     */
    @Override
    public Boolean edit(Report request) {
        return updateById(request);
    }

    /**
     * 分页查询
     *
     * @return
     */
    @Override
    public PageResult<List<ReportVO>> queryPage(Integer pageNum, Integer pageSize, String title, Integer status, Integer userId) {
        QueryWrapper<Report> queryWrapper = new QueryWrapper<>();
        if (CharSequenceUtil.isNotBlank(title)) {
            queryWrapper.like("title", title);
        }
        if (status != null) {
            queryWrapper.eq("status", status);
        }
        if (userId != null) {
            queryWrapper.eq("user_id", userId);
        }
        // ID 降序
        queryWrapper.orderByDesc("id");
        Page<Report> page = page(new Page<>(pageNum, pageSize), queryWrapper);
        // 返回结果
        List<ReportVO> reportVOS = convertVO(page.getRecords());
        return new PageResult<>(reportVOS, page.getCurrent(), page.getSize(), page.getTotal());
    }

    /**
     * 查询全部
     *
     * @return
     */
    @Override
    public List<Report> getAll() {
        return list();
    }


    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    @Override
    public Report getByIdDetail(Integer id) {
        return getById(id);
    }

    // 转换VO
    private List<ReportVO> convertVO(List<Report> reports) {
        ArrayList<ReportVO> list = new ArrayList<>();
        for (Report report : reports) {
            ReportVO reportVO = new ReportVO();
            BeanUtils.copyProperties(report, reportVO);
            User user = userService.getById(report.getUserId());
            if (user != null) {
                reportVO.setReporterName(user.getNickName());
            }
            list.add(reportVO);
        }
        return list;
    }
}