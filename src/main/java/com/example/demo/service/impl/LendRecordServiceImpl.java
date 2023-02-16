package com.example.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.entity.LendRecord;
import com.example.demo.mapper.LendRecordMapper;
import com.example.demo.service.LendRecordService;
import org.springframework.stereotype.Service;

/**
 * @author xin
 * @since 2023/2/13 16:40
 */
@Service
public class LendRecordServiceImpl extends ServiceImpl<LendRecordMapper, LendRecord> implements LendRecordService  {
}
