package com.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.entity.pojo.NovelContent;

import java.util.List;


public interface NovelContentService extends IService<NovelContent> {
    List<NovelContent> listByNovelId(Integer novelId);
}
