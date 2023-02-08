package com.example.demo.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.commom.R;
import com.example.demo.commom.Result;
import com.example.demo.entity.Book;
import com.example.demo.entity.Label;
import com.example.demo.service.LabelService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xin
 * @since 2023-02-02
 */
@RestController
@RequestMapping("/label")
public class LabelController {
    @Resource
    private LabelService labelService;

    @ApiOperation(value = "分页查询")
    @GetMapping("/page/{pageNum}/{pageSize}")
    public Result<?> findPage(@PathVariable Integer pageNum, @PathVariable Integer pageSize){
        Page<Label> page = new Page<>(pageNum, pageSize);
        labelService.page(page, null);
        return Result.success(page);
    }

    @ApiOperation(value = "查询全部")
    @GetMapping("/findAll")
    public Result<?> findAll(){
        List<Label> list = labelService.list(null);

        return Result.success(list);
    }

    @ApiOperation(value = "根据图书Id查询所属标签")
    @GetMapping("/getLabels/{bookId}")
    public Result<?> getLabels(@ApiParam(value = "图书Id") @PathVariable Long bookId){
        labelService.getLabelsByBookId(bookId);

        return Result.success();

    }

    @ApiOperation(value = "新增标签")
    @PostMapping()
    public R save(@RequestBody Label label){
        LambdaQueryWrapper<Label> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Label::getTitle, label.getTitle());
        Label one = labelService.getOne(wrapper);
        if(one != null){
           return R.error().message("标签已存在");
        }
        labelService.save(label);
        return R.ok();
    }

    @ApiOperation(value = "删除标签")
    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Integer id){

        labelService.deleteLabel(id);
        return Result.success();
    }





}

