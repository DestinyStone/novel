package com.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.entity.pojo.Score;


public interface ScoreService extends IService<Score> {
    Double calcScore(Integer id);
}
