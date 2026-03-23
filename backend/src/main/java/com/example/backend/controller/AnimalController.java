package com.example.backend.controller;

import com.example.backend.common.model.PageResult;
import com.example.backend.common.result.BaseResponse;
import com.example.backend.common.result.Result;
import com.example.backend.entity.Animal;
import com.example.backend.entity.request.animal.AnimalRequest;
import com.example.backend.entity.vo.animal.AnimalVO;
import com.example.backend.service.IAnimalService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description 动物信息表控制层
 * @Version 1.0
 */
@RestController
@RequestMapping("/animal")  //定位重导向
public class AnimalController {

    @Resource
    private IAnimalService animalService;

    @PostMapping("/add")     //localhost:8088/api/ animal/add
    @ApiOperation(value = "新增动物信息")
    public BaseResponse<Integer> add(@RequestBody AnimalRequest request) {
        Animal animal = new Animal();
        BeanUtils.copyProperties(request, animal);
        if (request.getTagList() != null) {
            animal.setTagsList(request.getTagList());
        }
        Integer res = animalService.add(animal);
        return Result.success(res);
    }

    @PostMapping("/batchAdd")
    @ApiOperation(value = "批量新增动物信息")
    public BaseResponse<Boolean> batchAdd(@RequestBody List<AnimalRequest> request) {
        List<Animal> animals = new java.util.ArrayList<>();
        for (AnimalRequest animalRequest : request) {
            Animal animal = new Animal();
            BeanUtils.copyProperties(animalRequest, animal);
            animals.add(animal);
        }
        Boolean res = animalService.batchAdd(animals);
        return Result.success(res);
    }

    @GetMapping("/del")
    @ApiOperation(value = "删除动物信息")
    public BaseResponse<Boolean> del(@RequestParam Integer id) {
        Boolean res = animalService.del(id);
        return Result.success(res);
    }

    @PostMapping("/batchDel")
    @ApiOperation(value = "批量删除动物信息")
    public BaseResponse<Boolean> batchDel(@RequestBody List<Integer> ids) {
        Boolean res = animalService.batchDel(ids);  
        return Result.success(res);
    }

    @PostMapping("/edit")
    @ApiOperation(value = "编辑动物信息")
    public BaseResponse<Boolean> edit(@RequestParam Integer id, @RequestBody AnimalRequest request) {
        Boolean res = animalService.edit(id, request);
        return Result.success(res);
    }

    @GetMapping("/page")
    @ApiOperation(value = "分页查询动物信息")
    public BaseResponse<PageResult<List<AnimalVO>>> page(
            @RequestParam Integer pageNum,
            @RequestParam Integer pageSize,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String typeId
    ) {
        PageResult<List<AnimalVO>> res = animalService.queryPage(pageNum, pageSize, name, typeId);
        return Result.success(res);
    }

    @GetMapping("/list")
    @ApiOperation(value = "查询所有动物信息")
    public BaseResponse<List<Animal>> all() {
        List<Animal> res = animalService.getAll();
        return Result.success(res);
    }

    @GetMapping("/getById")
    @ApiOperation(value = "根据ID查询动物信息")
    public BaseResponse<AnimalVO> getById(@RequestParam Integer id) {
        AnimalVO res = animalService.getByIdDetail(id);
        return Result.success(res);
    }

    @GetMapping("/recommend")
    @ApiOperation(value = "推荐动物")
    public BaseResponse<List<AnimalVO>> recommend(@RequestParam Integer num) {
        List<AnimalVO> res = animalService.recommend(num);                  
        return Result.success(res);
    }
}