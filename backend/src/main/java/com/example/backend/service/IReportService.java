package com.example.backend.service;

import com.example.backend.entity.Report;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.backend.common.model.PageResult;
import com.example.backend.entity.vo.report.ReportVO;

import java.util.List;

/**
 * <p>
 * 举报表 服务类
 * </p>
 *
 * @author 
 * @since 2025-01-19
 */
public interface IReportService extends IService<Report> {
        /**
        * 新增
        * @param request
        * @return
        */
        Integer add(Report request);

        /**
         * 批量新增
         *
         * @param request
         * @return
         */
        Boolean batchAdd(List<Report> request);

        /**
         * 删除
         * @param id
         * @return
         */
        Boolean del(Integer id);
        
        /**
         * 批量删除
         * @param ids 批量ID列表
         * @return 是否成功
         */
        Boolean batchDel(List<Integer> ids);

        /**
         * 编辑
         * @param request 请求参数
         * @return
         */
        Boolean edit(Report request);

        /**
         * 分页条件查询
         * @return
         */
        PageResult<List<ReportVO>> queryPage(Integer pageNum, Integer pageSize, String title, Integer status, Integer userId);

        /**
         * 查询全部
         * @return
         */
        List<Report> getAll();

        /**
         * 根据id查询
         *
         * @param id
         * @return
         */
        Report getByIdDetail(Integer id);
}