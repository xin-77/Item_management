package com.example.demo.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author xin
 * @since 2023/2/3 13:01
 */
@Data
public class BookQuery {

    @ApiModelProperty(value = "是否借阅")
    private String isbn;
    @ApiModelProperty(value = "图书名")
    private String name;
    @ApiModelProperty(value = "作者名")
    private String author;
    @ApiModelProperty(value = "标签")
    private String label;
}
