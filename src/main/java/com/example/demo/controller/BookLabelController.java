package com.example.demo.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.demo.commom.R;
import com.example.demo.entity.BookLabel;
import com.example.demo.service.BookLabelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xin
 * @since 2023-02-02
 */
@RestController
@RequestMapping("/book-label")
@Api(tags = "书本与标签联系接口")
public class BookLabelController {

    @Resource
    private BookLabelService bookLabelService;

    @ApiOperation(value = "添加书本标签")
    @PostMapping("")
    public R addLabel(@RequestBody BookLabel bookLabel){

        LambdaQueryWrapper<BookLabel> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BookLabel::getBookId, bookLabel.getBookId());
        wrapper.eq(BookLabel::getLabelId, bookLabel.getLabelId());
        long count = bookLabelService.count(wrapper);
        if(count > 0) {
            return R.error().message("书本标签已存在");
        }
        bookLabelService.save(bookLabel);

        return R.ok();
    }

    @ApiOperation(value = "删除书本标签")
    @PostMapping("removeLabel")
    public R removeLabel(@RequestBody BookLabel bookLabel){
        LambdaQueryWrapper<BookLabel> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BookLabel::getLabelId, bookLabel.getLabelId());
        wrapper.eq(BookLabel::getBookId, bookLabel.getBookId());
        bookLabelService.remove(wrapper);

        return R.ok();
    }


}

