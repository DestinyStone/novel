package com.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.entity.pojo.ReadLog;

public interface ReadLogService extends IService<ReadLog> {
    Long getCount(Integer id);
}
