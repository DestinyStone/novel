package com.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.common.api.R;
import com.entity.pojo.Tag;
import com.entity.pojo.User;
import com.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/tag")
public class TagController {
    @Autowired
    private TagService tagService;

    @RequestMapping("/list")
    @ResponseBody
    public R list(Tag tag) {
        LambdaQueryWrapper<Tag> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StrUtil.isNotBlank(tag.getName()), Tag::getName, tag.getName());
        List<Tag> list = tagService.list(wrapper);
        return R.data(list);
    }

    @RequestMapping("/update/{id}")
    public R update(@PathVariable("id") Integer id,  @RequestBody Tag tag) {
        tag.setId(id);
        tagService.updateById(tag);
        return R.success("操作成功");
    }

    @RequestMapping("/delete")
    public R userDelete(@RequestParam("id") Integer id) {
        tagService.removeById(id);
        return R.success("操作成功");
    }

    @RequestMapping("/save")
    public R userSave(@RequestBody Tag tag) {
        tag.setId(null);
        tagService.save(tag);
        return R.success("操作成功");
    }

    @RequestMapping("/id")
    public R id(@RequestParam("id") Integer id) {
        return R.data(tagService.getById(id));
    }
}
