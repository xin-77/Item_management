package com.example.demo.entity;


import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


@Data
public class Book implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Integer id;
    private String isbn;
    private String name;
    private BigDecimal price;
    private String author;
    private Integer borrownum;
    private String publisher;
    private String img;
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;
    private String status;


}
