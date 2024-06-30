package com.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.entity.pojo.Novel;

/**
 * @Author zhou xiao fen
 * @Date 2024/2/17 4:52
 **/
public interface NovelService extends IService<Novel> {
    void resetNumber(int id);
}
