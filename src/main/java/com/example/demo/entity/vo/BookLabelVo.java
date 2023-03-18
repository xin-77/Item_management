package com.example.demo.entity.vo;

import com.example.demo.entity.Book;
import com.example.demo.entity.Bookshelf;
import com.example.demo.entity.Label;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author xin
 * @since 2023/2/3 11:17
 */
@Data
public class BookLabelVo extends Book {

    @ApiModelProperty(value = "标签")
    private List<Label> labels;

    @ApiModelProperty(value = "书架信息")
    private List<BookShelfVo> BookShelfVo;

}
