package com.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.common.api.Query;
import com.common.api.R;
import com.common.utils.AuthUtil;
import com.entity.pojo.Collect;
import com.entity.pojo.Novel;
import com.service.CollectService;
import com.wrappere.CollectWrapper;
import com.wrappere.NovelWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/collect")
public class CollectController {

    @Autowired
    private CollectService service;

    @RequestMapping("/save")
    public synchronized R save(@RequestBody Collect collect) {
        LambdaQueryWrapper<Collect> wrapper  = new LambdaQueryWrapper<>();
        wrapper.eq(Collect::getUserId, AuthUtil.getUserId());
        wrapper.eq(Collect::getNovelId, collect.getNovelId());
        if (service.count(wrapper) > 0) {
            return R.fail("请勿重复加入书架");
        }
        collect.setUserId(AuthUtil.getUserId());

        collect.setCollectTime(new Date());
        service.save(collect);
        return R.success("操作成功");
    }

    @RequestMapping("/exist")
    public synchronized R exist(@RequestParam("novelId") Integer novelId) {
        LambdaQueryWrapper<Collect> wrapper  = new LambdaQueryWrapper<>();
        wrapper.eq(Collect::getUserId, AuthUtil.getUserId());
        wrapper.eq(Collect::getNovelId, novelId);
        if (service.count(wrapper) > 0) {
            return R.data(1);
        }
        return R.data(0);
    }

    @RequestMapping("/delete")
    public synchronized R delete(@RequestParam("novelId") Integer novelId) {
        LambdaQueryWrapper<Collect> wrapper  = new LambdaQueryWrapper<>();
        wrapper.eq(Collect::getUserId, AuthUtil.getUserId());
        wrapper.eq(Collect::getNovelId, novelId);
        service.remove(wrapper);
        return R.success("操作成功");
    }

    @RequestMapping("/page")
    public R page(Novel vo, Query query) {
        LambdaQueryWrapper<Collect> wrapper  = new LambdaQueryWrapper<>();
        wrapper.eq(Collect::getUserId, AuthUtil.getUserId());
        Page<Collect> page = new Page<>(query.getCurrent(), query.getSize());
        page = service.page(page);
        return R.data(CollectWrapper.build().pageVO(page));
    }

}
