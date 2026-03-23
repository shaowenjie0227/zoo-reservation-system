package com.example.backend.controller;

import com.example.backend.common.model.PageResult;
import com.example.backend.common.result.BaseResponse;
import com.example.backend.common.result.Result;
import com.example.backend.entity.vo.collect.CollectVO;
import com.example.backend.entity.vo.user.UserLoginVO;
import com.example.backend.entity.Weight;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

import com.example.backend.service.ICollectService;
import com.example.backend.service.IWeightService;
import com.example.backend.service.IUserService;
import com.example.backend.entity.Collect;

import org.springframework.web.bind.annotation.RestController;

/**
 * @Description 用户收藏表控制层
 * @Version 1.0
 */
@RestController
@RequestMapping("/collect")
public class CollectController {

    @Resource
    private ICollectService collectService;

    @PostMapping("/add")
    @ApiOperation(value = "新增收藏")
    public BaseResponse<Integer> add(@RequestBody Collect request) {
        Integer res = collectService.add(request);
        return Result.success(res);
    }

    @PostMapping("/batchAdd")
    @ApiOperation(value = "批量新增收藏")
    public BaseResponse<Boolean> batchAdd(@RequestBody List<Collect> request) {
        Boolean res = collectService.batchAdd(request);
        return Result.success(res);
    }

    @GetMapping("/del")
    @ApiOperation(value = "删除收藏")
    public BaseResponse<Boolean> del(@RequestParam Integer id) {
        Boolean res = collectService.del(id);
        return Result.success(res);
    }

    @PostMapping("/batchDel")
    @ApiOperation(value = "批量删除收藏")
    public BaseResponse<Boolean> batchDel(@RequestBody List<Integer> ids) {
        Boolean res = collectService.batchDel(ids);
        return Result.success(res);
    }

    @PostMapping("/edit")
    @ApiOperation(value = "编辑收藏")
    private BaseResponse<Boolean> edit(@RequestParam Integer id, @RequestBody Collect request) {
        Boolean res = collectService.edit(id, request);
        return Result.success(res);
    }

    @GetMapping("/page")
    @ApiOperation(value = "分页查询收藏")
    public BaseResponse<PageResult<List<CollectVO>>> page(
            @RequestParam Integer pageNum,
            @RequestParam Integer pageSize,
            @RequestParam(required = false) String type
    ) {
        PageResult<List<CollectVO>> res = collectService.queryPage(pageNum, pageSize, type);
        return Result.success(res);
    }

    @GetMapping("/list")
    @ApiOperation(value = "查询所有收藏")
    public BaseResponse<List<Collect>> all() {
        List<Collect> res = collectService.getAll();
        return Result.success(res);
    }

    @GetMapping("/getById")
    @ApiOperation(value = "根据ID查询收藏")
    public BaseResponse<Collect> getById(@RequestParam Integer id) {
        Collect res = collectService.getByIdDetail(id);
        return Result.success(res);
    }

    @GetMapping("/isCollected")
    @ApiOperation(value = "检查用户是否已收藏")
    public BaseResponse<Boolean> isCollected(
            @RequestParam Integer userId,
            @RequestParam String type,
            @RequestParam Integer targetId
    ) {
        Boolean res = collectService.isCollected(userId, type, targetId);
        return Result.success(res);
    }

    @PostMapping("/cancelCollect")
    @ApiOperation(value = "取消收藏")
    public BaseResponse<Boolean> cancelCollect(
            @RequestParam Integer userId,
            @RequestParam String type,
            @RequestParam Integer targetId
    ) {
        Boolean res = collectService.cancelCollect(userId, type, targetId);
        return Result.success(res);
    }
}