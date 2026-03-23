package com.example.backend.controller;

import com.example.backend.common.model.PageResult;
import com.example.backend.common.result.BaseResponse;
import com.example.backend.common.result.Result;
import com.example.backend.entity.vo.travelguidecategory.TravelGuideCategoryVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

import com.example.backend.service.ITravelGuideCategoryService;
import com.example.backend.entity.TravelGuideCategory;

import org.springframework.web.bind.annotation.RestController;

/**
 * @Description 攻略分类表控制层
 * @Version 1.0
 */
@RestController
@RequestMapping("/travelGuideCategory")
public class TravelGuideCategoryController {

    @Resource
    private ITravelGuideCategoryService travelGuideCategoryService;

    @PostMapping("/add")
    @ApiOperation(value = "新增攻略分类")
    public BaseResponse<Integer> add(@RequestBody TravelGuideCategory request) {
        Integer res = travelGuideCategoryService.add(request);
        return Result.success(res);
    }

    @PostMapping("/batchAdd")
    @ApiOperation(value = "批量新增攻略分类")
    public BaseResponse<Boolean> batchAdd(@RequestBody List<TravelGuideCategory> request) {
        Boolean res = travelGuideCategoryService.batchAdd(request);
        return Result.success(res);
    }

    @GetMapping("/del")
    @ApiOperation(value = "删除攻略分类")
    public BaseResponse<Boolean> del(@RequestParam Integer id) {
        Boolean res = travelGuideCategoryService.del(id);
        return Result.success(res);
    }

    @PostMapping("/batchDel")
    @ApiOperation(value = "批量删除攻略分类")
    public BaseResponse<Boolean> batchDel(@RequestBody List<Integer> ids) {
        Boolean res = travelGuideCategoryService.batchDel(ids);
        return Result.success(res);
    }

    @PostMapping("/edit")
    @ApiOperation(value = "编辑攻略分类")
    private BaseResponse<Boolean> edit(@RequestParam Integer id, @RequestBody TravelGuideCategory request) {
        Boolean res = travelGuideCategoryService.edit(id, request);
        return Result.success(res);
    }

    @GetMapping("/page")
    @ApiOperation(value = "分页查询攻略分类")
    public BaseResponse<PageResult<List<TravelGuideCategoryVO>>> page(
            @RequestParam Integer pageNum,
            @RequestParam Integer pageSize,
            @RequestParam(required = false) String name
    ) {
        PageResult<List<TravelGuideCategoryVO>> res = travelGuideCategoryService.queryPage(pageNum, pageSize, name);
        return Result.success(res);
    }

    @GetMapping("/list")
    @ApiOperation(value = "查询所有攻略分类")
    public BaseResponse<List<TravelGuideCategory>> all() {
        List<TravelGuideCategory> res = travelGuideCategoryService.getAll();
        return Result.success(res);
    }

    @GetMapping("/getById")
    @ApiOperation(value = "根据ID查询攻略分类")
    public BaseResponse<TravelGuideCategory> getById(@RequestParam Integer id) {
        TravelGuideCategory res = travelGuideCategoryService.getByIdDetail(id);
        return Result.success(res);
    }
}
