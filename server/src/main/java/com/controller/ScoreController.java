package com.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.common.api.R;
import com.common.utils.AuthUtil;
import com.entity.pojo.Collect;
import com.entity.pojo.Score;
import com.service.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/score")
public class ScoreController {

    @Autowired
    private ScoreService service;

    @RequestMapping("/save")
    public synchronized R save(@RequestBody Score score) {
        LambdaQueryWrapper<Score> wrapper  = new LambdaQueryWrapper<>();
        wrapper.eq(Score::getUserId, AuthUtil.getUserId());
        wrapper.eq(Score::getNovelId, score.getNovelId());
        if (service.count(wrapper) > 0) {
            return R.fail("请勿重复评分");
        }
        score.setUserId(AuthUtil.getUserId());
        score.setScoreTime(new Date());
        service.save(score);
        return R.success("操作成功");
    }

    @RequestMapping("/exist")
    public synchronized R exist(@RequestParam("novelId") Integer novelId) {
        LambdaQueryWrapper<Score> wrapper  = new LambdaQueryWrapper<>();
        wrapper.eq(Score::getUserId, AuthUtil.getUserId());
        wrapper.eq(Score::getNovelId, novelId);

        Score one = service.getOne(wrapper);
        if (one != null) {
            return R.data(one.getLevel());
        }
        return R.data(-999);
    }
}
