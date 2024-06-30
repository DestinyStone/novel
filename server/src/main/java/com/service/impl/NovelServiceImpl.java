package com.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.entity.pojo.Novel;
import com.entity.pojo.NovelContent;
import com.mapper.NovelMapper;
import com.service.NovelContentService;
import com.service.NovelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class NovelServiceImpl extends ServiceImpl<NovelMapper, Novel> implements NovelService {

    @Autowired
    private NovelContentService contentService;

    @Override
    public void resetNumber(int id) {
        LambdaQueryWrapper<NovelContent> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(NovelContent::getNovelId, id);
        wrapper.orderByDesc(NovelContent::getSort);
        List<NovelContent> list = contentService.list(wrapper);
        if (list.size() == 0) {
            Novel novel = new Novel();
            novel.setId(id);
            novel.setNumber("0");
            novel.setNewest("");
            updateById(novel);
            return;
        }

        Long count = 0L;
        for (NovelContent content : list) {
            if (StrUtil.isBlank(content.getContent())) {
                continue;
            }
            count += content.getContent().length();
        }

        Novel novel = new Novel();
        novel.setId(id);
        novel.setNumber(count + "");
        novel.setNewest(list.get(0).getTitle());
        updateById(novel);
    }
}
