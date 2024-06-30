package com.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.entity.pojo.NovelContent;
import com.mapper.NovelContentMapper;
import com.service.NovelContentService;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class NovelContentServiceImpl extends ServiceImpl<NovelContentMapper, NovelContent> implements NovelContentService {
    @Override
    public List<NovelContent> listByNovelId(Integer novelId) {
        LambdaQueryWrapper<NovelContent> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(NovelContent::getNovelId, novelId);
        return list(wrapper);
    }
}
