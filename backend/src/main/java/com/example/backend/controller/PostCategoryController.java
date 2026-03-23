package com.example.backend.controller;

import com.example.backend.common.model.PageResult;
import com.example.backend.common.result.BaseResponse;
import com.example.backend.common.result.Result;
import com.example.backend.entity.PostCategory;
import com.example.backend.entity.vo.postcategory.PostCategoryVO;
import com.example.backend.service.IPostCategoryService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description 帖子分类表控制层
 * @Version 1.0
 */
@RestController
@RequestMapping("/postCategory")
public class PostCategoryController {

    @Resource
    private IPostCategoryService postCategoryService;

    @PostMapping("/add")
    @ApiOperation(value = "新增帖子分类")
    public BaseResponse<Integer> add(@RequestBody PostCategory request) {
        Integer res = postCategoryService.add(request);
        return Result.success(res);
    }

    @PostMapping("/batchAdd")
    @ApiOperation(value = "批量新增帖子分类")
    public BaseResponse<Boolean> batchAdd(@RequestBody List<PostCategory> request) {
        Boolean res = postCategoryService.batchAdd(request);
        return Result.success(res);
    }

    @GetMapping("/del")
    @ApiOperation(value = "删除帖子分类")
    public BaseResponse<Boolean> del(@RequestParam Integer id) {
        Boolean res = postCategoryService.del(id);
        return Result.success(res);
    }

    @PostMapping("/batchDel")
    @ApiOperation(value = "批量删除帖子分类")
    public BaseResponse<Boolean> batchDel(@RequestBody List<Integer> ids) {
        Boolean res = postCategoryService.batchDel(ids);
        return Result.success(res);
    }

    @PostMapping("/edit")
    @ApiOperation(value = "编辑帖子分类")
    public BaseResponse<Boolean> edit(@RequestParam Integer id, @RequestBody PostCategory request) {
        Boolean res = postCategoryService.edit(id, request);
        return Result.success(res);
    }

    @GetMapping("/page")
    @ApiOperation(value = "分页查询帖子分类")
    public BaseResponse<PageResult<List<PostCategoryVO>>> page(
            @RequestParam Integer pageNum,
            @RequestParam Integer pageSize,
            @RequestParam(required = false) String name
    ) {
        PageResult<List<PostCategoryVO>> res = postCategoryService.queryPage(pageNum, pageSize, name);
        return Result.success(res);
    }

    @GetMapping("/list")
    @ApiOperation(value = "查询所有帖子分类")
    public BaseResponse<List<PostCategory>> all() {
        List<PostCategory> res = postCategoryService.getAll();
        return Result.success(res);
    }

    @GetMapping("/getById")
    @ApiOperation(value = "根据ID查询帖子分类")
    public BaseResponse<PostCategory> getById(@RequestParam Integer id) {
        PostCategory res = postCategoryService.getByIdDetail(id);
        return Result.success(res);
    }
}
