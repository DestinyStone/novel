package com.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.common.api.Query;
import com.common.api.R;
import com.common.utils.AuthUtil;
import com.entity.pojo.Collect;
import com.entity.pojo.Novel;
import com.entity.pojo.NovelContent;
import com.entity.pojo.ReadLog;
import com.entity.vo.ReadProcess;
import com.service.NovelContentService;
import com.service.NovelService;
import com.service.ReadLogService;
import com.wrappere.CollectWrapper;
import com.wrappere.NovelWrapper;
import com.wrappere.ReadLogWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/read/log")
public class ReadLogController {

    @Autowired
    private ReadLogService service;

    @Autowired
    private NovelContentService contentService;

    @Autowired
    private NovelService novelService;

    // 获取阅读进度
    @RequestMapping("/process")
    public R process(@RequestParam("novelId") Integer novelId) {
        LambdaUpdateWrapper<ReadLog> wrapper  = new LambdaUpdateWrapper<>();
        wrapper.eq(ReadLog::getUserId, AuthUtil.getUserId());
        wrapper.eq(ReadLog::getNovelId, novelId);
        wrapper.orderByDesc(ReadLog::getUpdateTime);
        List<ReadLog> list = service.list(wrapper);
        if (CollUtil.isEmpty(list)) {
            return R.data(null);
        }

        ReadLog readLog = list.get(0);
        ReadProcess process = BeanUtil.copyProperties(readLog, ReadProcess.class);

        List<NovelContent> contents = contentService.listByNovelId(novelId);
        process.setProcess(Math.ceil(list.size() / (contents.size() * 1.0)));
        return R.data(process);
    }

    @RequestMapping("/page")
    public R page(Novel vo, Query query) {
        Page<ReadLog> page = new Page<>(query.getCurrent(), query.getSize());
        page = service.page(page);
        return R.data(ReadLogWrapper.build().pageVO(page));
    }

    @RequestMapping("/list")
    public R list(Novel vo, Query query) {
        LambdaQueryWrapper<ReadLog> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ReadLog::getUserId, AuthUtil.getUserId());
        Set<Integer> novelIds = service.list(wrapper).stream().map(ReadLog::getNovelId).collect(Collectors.toSet());
        List<Novel> novels = novelService.listByIds(novelIds);

        return R.data(NovelWrapper.build().listVO(novels));
    }

    @RequestMapping("/save")
    public synchronized R save(@RequestBody ReadLog readLog) {
        LambdaUpdateWrapper<ReadLog> wrapper  = new LambdaUpdateWrapper<>();
        wrapper.eq(ReadLog::getUserId, AuthUtil.getUserId());
        wrapper.eq(ReadLog::getNovelId, readLog.getNovelId());
        wrapper.eq(ReadLog::getContentId, readLog.getContentId());

        ReadLog current = service.getOne(wrapper);

        if (current != null) {
            // 查看上一次更新时间是否超过一分钟
            Date currentDate = new Date();
            if (currentDate.getTime() - current.getUpdateTime().getTime() >= 60 * 1000) {
                current.setTime(current.getTime() + 1);
            }
            current.setUpdateTime(new Date());
            current.setTitle(readLog.getTitle());
            service.updateById(current);
            return R.success("操作成功");
        }

        readLog.setUserId(AuthUtil.getUserId());
        readLog.setUpdateTime(new Date());
        readLog.setTime(1);
        service.save(readLog);
        return R.success("操作成功");
    }
}
