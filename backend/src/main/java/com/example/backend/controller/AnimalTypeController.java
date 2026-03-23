package com.example.backend.controller;

import com.example.backend.common.model.PageResult;
import com.example.backend.common.result.BaseResponse;
import com.example.backend.common.result.Result;
import com.example.backend.entity.vo.animaltype.AnimalTypeVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

import com.example.backend.service.IAnimalTypeService;
import com.example.backend.entity.AnimalType;

import org.springframework.web.bind.annotation.RestController;

/**
 * @Description 动物物种表控制层
 * @Version 1.0
 */
@RestController
@RequestMapping("/animalType")
public class AnimalTypeController {

    @Resource
    private IAnimalTypeService animalTypeService;

    @PostMapping("/add")
    @ApiOperation(value = "新增动物物种")
    public BaseResponse<Integer> add(@RequestBody AnimalType request) {
        Integer res = animalTypeService.add(request);
        return Result.success(res);
    }

    @PostMapping("/batchAdd")
    @ApiOperation(value = "批量新增动物物种")
    public BaseResponse<Boolean> batchAdd(@RequestBody List<AnimalType> request) {
        Boolean res = animalTypeService.batchAdd(request);
        return Result.success(res);
    }

    @GetMapping("/del")
    @ApiOperation(value = "删除动物物种")
    public BaseResponse<Boolean> del(@RequestParam Integer id) {
        Boolean res = animalTypeService.del(id);
        return Result.success(res);
    }

    @PostMapping("/batchDel")
    @ApiOperation(value = "批量删除动物物种")
    public BaseResponse<Boolean> batchDel(@RequestBody List<Integer> ids) {
        Boolean res = animalTypeService.batchDel(ids);
        return Result.success(res);
    }

    @PostMapping("/edit")
    @ApiOperation(value = "编辑动物物种")
    private BaseResponse<Boolean> edit(@RequestParam Integer id, @RequestBody AnimalType request) {
        Boolean res = animalTypeService.edit(id, request);
        return Result.success(res);
    }

    @GetMapping("/page")
    @ApiOperation(value = "分页查询动物物种")
    public BaseResponse<PageResult<List<AnimalTypeVO>>> page(
            @RequestParam Integer pageNum,
            @RequestParam Integer pageSize,
            @RequestParam(required = false) String name
    ) {
        PageResult<List<AnimalTypeVO>> res = animalTypeService.queryPage(pageNum, pageSize, name);
        return Result.success(res);
    }

    @GetMapping("/list")
    @ApiOperation(value = "查询所有动物物种")
    public BaseResponse<List<AnimalType>> all() {
        List<AnimalType> res = animalTypeService.getAll();
        return Result.success(res);
    }

    @GetMapping("/getById")
    @ApiOperation(value = "根据ID查询动物物种")
    public BaseResponse<AnimalType> getById(@RequestParam Integer id) {
        AnimalType res = animalTypeService.getByIdDetail(id);
        return Result.success(res);
    }
}