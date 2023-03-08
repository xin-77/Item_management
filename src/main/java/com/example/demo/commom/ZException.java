package com.example.demo.commom;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Xin
 * @date 2022/12/30 8:21
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ZException extends RuntimeException{
    private Integer code;//状态码
    private String msg;//异常信息
}
