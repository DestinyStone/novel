package com.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.entity.pojo.Comment;


public interface CommentService extends IService<Comment> {
    Long getCount(Integer id);
}
