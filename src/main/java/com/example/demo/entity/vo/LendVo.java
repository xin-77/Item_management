package com.example.demo.entity.vo;

import com.example.demo.entity.LendRecord;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author xin
 * @since 2023/2/13 16:45
 */
@Data
public class LendVo extends LendRecord {

    @ApiModelProperty(value = "用户名")
     private String userName;
}
