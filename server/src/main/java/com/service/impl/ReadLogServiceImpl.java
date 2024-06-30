package com.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.common.utils.AuthUtil;
import com.entity.pojo.ReadLog;
import com.mapper.ReadLogMapper;
import com.service.ReadLogService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReadLogServiceImpl extends ServiceImpl<ReadLogMapper, ReadLog> implements ReadLogService {
    @Override
    public Long getCount(Integer id) {
        LambdaQueryWrapper<ReadLog> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ReadLog::getUserId, AuthUtil.getUserId());
        wrapper.eq(ReadLog::getNovelId, id);
        List<ReadLog> list = list(wrapper);

        Long count = 0L;
        for (ReadLog readLog : list) {
            count += readLog.getTime();
        }
        return count;
    }
}
