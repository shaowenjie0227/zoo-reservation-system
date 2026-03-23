package com.example.backend.controller;

import com.example.backend.common.model.PageResult;
import com.example.backend.common.result.BaseResponse;
import com.example.backend.common.result.Result;
import com.example.backend.entity.vo.report.ReportVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

import com.example.backend.service.IReportService;
import com.example.backend.entity.Report;

import org.springframework.web.bind.annotation.RestController;

/**
 * @Description 举报表控制层
 * @Version 1.0
 */
@RestController
@RequestMapping("/report")
public class ReportController {

    @Resource
    private IReportService reportService;

    @PostMapping("/add")
    @ApiOperation(value = "新增举报")
    public BaseResponse<Integer> add(@RequestBody Report request) {
        Integer res = reportService.add(request);
        return Result.success(res);
    }

    @PostMapping("/batchAdd")
    @ApiOperation(value = "批量新增举报")
    public BaseResponse<Boolean> batchAdd(@RequestBody List<Report> request) {
        Boolean res = reportService.batchAdd(request);
        return Result.success(res);
    }

    @GetMapping("/del")
    @ApiOperation(value = "删除举报")
    public BaseResponse<Boolean> del(@RequestParam Integer id) {
        Boolean res = reportService.del(id);
        return Result.success(res);
    }

    @PostMapping("/batchDel")
    @ApiOperation(value = "批量删除举报")
    public BaseResponse<Boolean> batchDel(@RequestBody List<Integer> ids) {
        Boolean res = reportService.batchDel(ids);
        return Result.success(res);
    }

    @PostMapping("/edit")
    @ApiOperation(value = "编辑举报")
    public BaseResponse<Boolean> edit(@RequestBody Report request) {
        Boolean res = reportService.edit(request);
        return Result.success(res);
    }

    @GetMapping("/page")
    @ApiOperation(value = "分页查询举报")
    public BaseResponse<PageResult<List<ReportVO>>> page(
            @RequestParam Integer pageNum,
            @RequestParam Integer pageSize,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) Integer userId
    ) {
        PageResult<List<ReportVO>> res = reportService.queryPage(pageNum, pageSize, title, status, userId);
        return Result.success(res);
    }

    @GetMapping("/list")
    @ApiOperation(value = "查询所有举报")
    public BaseResponse<List<Report>> all() {
        List<Report> res = reportService.getAll();
        return Result.success(res);
    }

    @GetMapping("/getById")
    @ApiOperation(value = "根据ID查询举报")
    public BaseResponse<Report> getById(@RequestParam Integer id) {
        Report res = reportService.getByIdDetail(id);
        return Result.success(res);
    }
}