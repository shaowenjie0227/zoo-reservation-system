package com.example.backend.controller;

import com.example.backend.common.model.PageResult;
import com.example.backend.common.result.BaseResponse;
import com.example.backend.common.result.Result;
import com.example.backend.entity.vo.conservationnews.ConservationNewsVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

import com.example.backend.service.IConservationNewsService;
import com.example.backend.entity.ConservationNews;

import org.springframework.web.bind.annotation.RestController;

/**
 * @Description 生态保护新闻表控制层
 * @Version 1.0
 */
@RestController
@RequestMapping("/conservationNews")
public class ConservationNewsController {

    @Resource
    private IConservationNewsService conservationNewsService;

    @PostMapping("/add")
    @ApiOperation(value = "新增生态保护新闻")
    public BaseResponse<Integer> add(@RequestBody ConservationNews request) {
        Integer res = conservationNewsService.add(request);
        return Result.success(res);
    }

    @PostMapping("/batchAdd")
    @ApiOperation(value = "批量新增生态保护新闻")
    public BaseResponse<Boolean> batchAdd(@RequestBody List<ConservationNews> request) {
        Boolean res = conservationNewsService.batchAdd(request);
        return Result.success(res);
    }

    @GetMapping("/del")
    @ApiOperation(value = "删除生态保护新闻")
    public BaseResponse<Boolean> del(@RequestParam Integer id) {
        Boolean res = conservationNewsService.del(id);
        return Result.success(res);
    }

    @PostMapping("/batchDel")
    @ApiOperation(value = "批量删除生态保护新闻")
    public BaseResponse<Boolean> batchDel(@RequestBody List<Integer> ids) {
        Boolean res = conservationNewsService.batchDel(ids);
        return Result.success(res);
    }

    @PostMapping("/edit")
    @ApiOperation(value = "编辑生态保护新闻")
    public BaseResponse<Boolean> edit(@RequestBody ConservationNews request) {
        Boolean res = conservationNewsService.edit(request);
        return Result.success(res);
    }

    @GetMapping("/page")
    @ApiOperation(value = "分页查询生态保护新闻")
    public BaseResponse<PageResult<List<ConservationNewsVO>>> page(
            @RequestParam Integer pageNum,
            @RequestParam Integer pageSize,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Integer status
    ) {
        PageResult<List<ConservationNewsVO>> res = conservationNewsService.queryPage(pageNum, pageSize, title, type, status);
        return Result.success(res);
    }

    @GetMapping("/list")
    @ApiOperation(value = "查询所有生态保护新闻")
    public BaseResponse<List<ConservationNews>> all() {
        List<ConservationNews> res = conservationNewsService.getAll();
        return Result.success(res);
    }

    @GetMapping("/getById")
    @ApiOperation(value = "根据ID查询生态保护新闻")
    public BaseResponse<ConservationNews> getById(@RequestParam Integer id) {
        ConservationNews res = conservationNewsService.getByIdDetail(id);
        return Result.success(res);
    }

    @PostMapping("/increaseViewCount")
    @ApiOperation(value = "增加浏览数")
    public BaseResponse<Boolean> increaseViewCount(@RequestParam Integer id) {
        Boolean res = conservationNewsService.increaseViewCount(id);
        return Result.success(res);
    }
}