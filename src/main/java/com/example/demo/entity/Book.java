package com.example.demo.entity;


import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


@Data
public class Book implements Serializable {
    private static final long serialVersionUID = 1L;



    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    private String isbn;
    private String name;
    private BigDecimal price;
    private String author;
    private Integer borrownum;
    private String publisher;
    private String img;
    @JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
    private Date publishTime;

    @ApiModelProperty(hidden = true)
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
    private Date createTime;

    @ApiModelProperty(hidden = true)
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
    private Date updateTime;
    private Integer stock;


}
