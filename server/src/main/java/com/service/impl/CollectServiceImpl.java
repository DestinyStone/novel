package com.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.entity.pojo.Collect;
import com.mapper.CollectMapper;
import com.service.CollectService;
import org.springframework.stereotype.Service;


@Service
public class CollectServiceImpl extends ServiceImpl<CollectMapper, Collect> implements CollectService {
}
