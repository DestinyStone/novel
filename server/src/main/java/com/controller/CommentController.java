package com.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.common.api.Query;
import com.common.api.R;
import com.common.utils.AuthUtil;
import com.entity.pojo.Collect;
import com.entity.pojo.Comment;
import com.entity.pojo.Novel;
import com.entity.pojo.NovelContent;
import com.entity.vo.CommentVO;
import com.service.CommentService;
import com.wrappere.CollectWrapper;
import com.wrappere.CommentWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    private CommentService service;

    @RequestMapping("/save")
    public R save(@RequestBody Comment comment) {
        comment.setCreateTime(new Date());
        comment.setUserId(AuthUtil.getUserId());
        comment.setAccount(AuthUtil.getUser().getAccount());
        comment.setStatus(0);
        service.save(comment);
        return R.success("操作成功");
    }

    @RequestMapping("/update/{id}")
    public R update(@PathVariable("id") Integer id, @RequestBody Comment comment) {
        comment.setId(id);
        service.updateById(comment);
        return R.success("操作成功");
    }

    @RequestMapping("/list/{novelId}")
    public R listByNovelId(@PathVariable("novelId") Integer novelId) {
        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Comment::getNovelId, novelId);
        wrapper.eq(Comment::getStatus, 1);
        wrapper.orderByDesc(Comment::getCreateTime);
        wrapper.eq(Comment::getType, 0);
        return R.data(service.list(wrapper));
    }

    @RequestMapping("/page")
    public R page(CommentVO vo, Query query) {
        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(vo.getNovelId() != null, Comment::getNovelId, vo.getNovelId());
        wrapper.eq(vo.getStatus() != null, Comment::getStatus, vo.getStatus());
        Page<Comment> page = new Page<>(query.getCurrent(), query.getSize());
        page = service.page(page, wrapper);
        return R.data(CommentWrapper.build().pageVO(page));
    }

    @RequestMapping("/delete")
    public R userDelete(@RequestParam("id") Integer id) {
        service.removeById(id);
        return R.success("操作成功");
    }

    @RequestMapping("/id")
    public R id(@RequestParam("id") Integer id) {
        Comment content = service.getById(id);
        return R.data(content);
    }
}
