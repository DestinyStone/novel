package com.wrappere;

import cn.hutool.core.bean.BeanUtil;
import com.common.api.BaseEntityWrapper;
import com.entity.pojo.Comment;
import com.entity.pojo.Novel;
import com.entity.pojo.ReadLog;
import com.entity.vo.CommentVO;
import com.entity.vo.NovelVO;
import com.entity.vo.ReadLogVO;
import com.service.NovelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class CommentWrapper  extends BaseEntityWrapper<Comment, CommentVO> {

    private static NovelService novelService;

    public static CommentWrapper build() {
        return new CommentWrapper();
    }

    @Autowired
    public void setNovelService(NovelService novelService) {
        CommentWrapper.novelService = novelService;
    }

    @Override
    public CommentVO entityVO(Comment entity) {
        CommentVO vo = Objects.requireNonNull(BeanUtil.copyProperties(entity, CommentVO.class));

        Novel novel = novelService.getById(vo.getNovelId());
        if (novel == null) {
            return vo;
        }
        vo.setNovelName(novel.getName());
        return vo;
    }
}