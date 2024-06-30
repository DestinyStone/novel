package com.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.entity.pojo.Score;
import com.mapper.ScoreMapper;
import com.service.ScoreService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScoreServiceImpl extends ServiceImpl<ScoreMapper, Score> implements ScoreService {
    @Override
    public Double calcScore(Integer id) {
        LambdaQueryWrapper<Score> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Score::getNovelId, id);
        Double count = 0.0;
        List<Score> list = list(wrapper);
        if (CollUtil.isEmpty(list)) {
            return count;
        }

        for (Score score : list) {
            count += score.getLevel();
        }
        return count / list.size();
    }
}
