package com.example.backend.controller;

import com.example.backend.common.model.PageResult;
import com.example.backend.common.result.BaseResponse;
import com.example.backend.common.result.Result;
import com.example.backend.entity.vo.forumpost.ForumPostVO;
import com.example.backend.entity.request.forumpost.ForumPostRequest;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

import com.example.backend.service.IForumPostService;
import com.example.backend.entity.ForumPost;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.BeanUtils;

/**
 * @Description 论坛帖子表控制层
 * @Version 1.0
 */
@RestController
@RequestMapping("/forumPost")
public class ForumPostController {

    @Resource
    private IForumPostService forumPostService;

    @PostMapping("/add")
    @ApiOperation(value = "新增论坛帖子")
    public BaseResponse<Integer> add(@RequestBody ForumPostRequest request) {
        ForumPost forumPost = new ForumPost();
        BeanUtils.copyProperties(request, forumPost);
        Integer res = forumPostService.add(forumPost);
        return Result.success(res);
    }

    @PostMapping("/batchAdd")
    @ApiOperation(value = "批量新增论坛帖子")
    public BaseResponse<Boolean> batchAdd(@RequestBody List<ForumPostRequest> request) {
        List<ForumPost> forumPosts = request.stream().map(item -> {
            ForumPost forumPost = new ForumPost();
            BeanUtils.copyProperties(item, forumPost);
            return forumPost;
        }).collect(java.util.stream.Collectors.toList());
        Boolean res = forumPostService.batchAdd(forumPosts);
        return Result.success(res);
    }

    @GetMapping("/del")
    @ApiOperation(value = "删除论坛帖子")
    public BaseResponse<Boolean> del(@RequestParam Integer id) {
        Boolean res = forumPostService.del(id);
        return Result.success(res);
    }

    @PostMapping("/batchDel")
    @ApiOperation(value = "批量删除论坛帖子")
    public BaseResponse<Boolean> batchDel(@RequestBody List<Integer> ids) {
        Boolean res = forumPostService.batchDel(ids);
        return Result.success(res);
    }

    @PostMapping("/edit")
    @ApiOperation(value = "编辑论坛帖子")
    private BaseResponse<Boolean> edit(@RequestParam Integer id, @RequestBody ForumPostRequest request) {
        ForumPost forumPost = new ForumPost();
        BeanUtils.copyProperties(request, forumPost);
        Boolean res = forumPostService.edit(id, forumPost);
        return Result.success(res);
    }

    @GetMapping("/page")
    @ApiOperation(value = "分页查询论坛帖子")
    public BaseResponse<PageResult<List<ForumPostVO>>> page(
            @RequestParam Integer pageNum,
            @RequestParam Integer pageSize,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String userId,
            @RequestParam(required = false) Integer categoryId
    ) {
        PageResult<List<ForumPostVO>> res = forumPostService.queryPage(pageNum, pageSize, title, userId, categoryId);
        return Result.success(res);
    }

    @GetMapping("/list")
    @ApiOperation(value = "查询所有论坛帖子")
    public BaseResponse<List<ForumPost>> all() {
        List<ForumPost> res = forumPostService.getAll();
        return Result.success(res);
    }

    @GetMapping("/getById")
    @ApiOperation(value = "根据ID查询论坛帖子")
    public BaseResponse<ForumPost> getById(@RequestParam Integer id) {
        ForumPost res = forumPostService.getByIdDetail(id);
        return Result.success(res);
    }

    @GetMapping("/getByIdVO")
    @ApiOperation(value = "根据ID查询论坛帖子详情VO")
    public BaseResponse<ForumPostVO> getByIdVO(@RequestParam Integer id) {
        ForumPostVO res = forumPostService.getByIdDetailVO(id);
        return Result.success(res);
    }
}