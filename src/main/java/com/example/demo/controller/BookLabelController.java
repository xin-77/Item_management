package com.example.demo.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.demo.commom.R;
import com.example.demo.entity.BookLabel;
import com.example.demo.service.BookLabelService;
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
public class BookLabelController {

    @Resource
    private BookLabelService bookLabelService;

    @ApiOperation(value = "添加书本标签")
    @PostMapping("/{bookId}/{labelId}")
    public R addLabel(@PathVariable Long bookId, @PathVariable Long labelId){
        BookLabel bookLabel = new BookLabel();
        bookLabel.setBookId(bookId);
        bookLabel.setLabelId(labelId);
        LambdaQueryWrapper<BookLabel> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BookLabel::getBookId, bookId);
        wrapper.eq(BookLabel::getLabelId, labelId);
        long count = bookLabelService.count(wrapper);
        if(count > 0) {
            return R.error().message("书本标签已存在");
        }
        bookLabelService.save(bookLabel);

        return R.ok();
    }

    @ApiOperation(value = "删除书本标签")
    @PostMapping("/removeLabel/{bookId}/{labelId}")
    public R removeLabel(@PathVariable Long bookId, @PathVariable Long labelId){
        LambdaQueryWrapper<BookLabel> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BookLabel::getLabelId, labelId);
        wrapper.eq(BookLabel::getBookId, bookId);
        bookLabelService.remove(wrapper);

        return R.ok();
    }


}

