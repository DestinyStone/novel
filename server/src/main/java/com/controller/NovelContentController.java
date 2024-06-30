package com.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.common.api.Query;
import com.common.api.R;
import com.common.exception.ServiceException;
import com.entity.pojo.Novel;
import com.entity.pojo.NovelContent;
import com.service.NovelContentService;
import com.service.NovelService;
import com.wrappere.NovelWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Date;
import java.util.List;


@RestController
@RequestMapping("/novel/content")
public class NovelContentController {
    @Autowired
    private NovelContentService service;

    @Autowired
    private NovelService novelService;

    @RequestMapping("/page")
    public R page(NovelContent vo, Query query) {
        LambdaQueryWrapper<NovelContent> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(vo.getNovelId() != null, NovelContent::getNovelId, vo.getNovelId());
        wrapper.like(StrUtil.isNotBlank(vo.getTitle()), NovelContent::getTitle, vo.getTitle());
        wrapper.orderByAsc(NovelContent::getSort);

        Page<NovelContent> page = new Page<>(query.getCurrent(), query.getSize());
        page = service.page(page, wrapper);
        return R.data(page);
    }

    @RequestMapping("/list")
    public R list(NovelContent vo) {
        LambdaQueryWrapper<NovelContent> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StrUtil.isNotBlank(vo.getTitle()), NovelContent::getTitle, vo.getTitle());
        wrapper.eq(vo.getNovelId() != null, NovelContent::getNovelId, vo.getNovelId());
        wrapper.orderByAsc(NovelContent::getSort);
        List<NovelContent> list = service.list(wrapper);
        return R.data(list);
    }

    @RequestMapping("/update/{id}")
    public R update(@PathVariable("id") Integer id,  @RequestBody NovelContent dto) {
        dto.setId(id);
        service.updateById(dto);
        novelService.resetNumber(dto.getNovelId());
        return R.success("操作成功");
    }

    @RequestMapping("/delete")
    public R userDelete(@RequestParam("id") Integer id) {
        NovelContent content = service.getById(id);

        service.removeById(id);

        novelService.resetNumber(content.getNovelId());
        return R.success("操作成功");
    }

    @RequestMapping("/save")
    public R userSave(@RequestBody NovelContent dto) {
        dto.setId(null);
        dto.setCreateDate(new Date());
        service.save(dto);
        novelService.resetNumber(dto.getNovelId());
        return R.success("操作成功");
    }

    @RequestMapping("/id")
    public R id(@RequestParam("id") Integer id) {
        NovelContent content = service.getById(id);
        return R.data(content);
    }
}
