package com.wrappere;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.common.api.BaseEntityWrapper;
import com.common.utils.Func;
import com.entity.pojo.Collect;
import com.entity.pojo.Novel;
import com.entity.pojo.NovelContent;
import com.entity.pojo.Tag;
import com.entity.vo.NovelVO;
import com.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class NovelWrapper extends BaseEntityWrapper<Novel, NovelVO> {

    private static TagService tagService;
    private static CollectService collectService;
    private static ScoreService scoreService;
    private static CommentService commentService;
    private static ReadLogService readLogService;

    @Autowired
    public void setReadLogService(ReadLogService readLogService) {
        NovelWrapper.readLogService = readLogService;
    }

    @Autowired
    public void setCommentService(CommentService commentService) {
        NovelWrapper.commentService = commentService;
    }

    @Autowired
    public void setTagService(TagService tagService) {
        NovelWrapper.tagService = tagService;
    }

    @Autowired
    public void setCollectService(CollectService collectService) {
        NovelWrapper.collectService = collectService;
    }

    @Autowired
    public void setScoreService(ScoreService scoreService) {
        NovelWrapper.scoreService = scoreService;
    }

    public static NovelWrapper build() {
        return new NovelWrapper();
    }

    @Override
    public NovelVO entityVO(Novel novel) {
        NovelVO vo = Objects.requireNonNull(BeanUtil.copyProperties(novel, NovelVO.class));
        List<Long> tagIds = Func.toLongList(vo.getTags());
        List<Tag> tags = tagService.listByIds(tagIds);
        vo.setTagList(tags);

        LambdaQueryWrapper<Collect> collectWrapper = new LambdaQueryWrapper<>();
        collectWrapper.eq(Collect::getNovelId, vo.getId());
        long count = collectService.count(collectWrapper);
        vo.setCollectCount(count);

        vo.setScore(scoreService.calcScore(vo.getId()));

        vo.setCommentCount(commentService.getCount(vo.getId()));

        vo.setReadTime(readLogService.getCount(vo.getId()));
        return vo;
    }
}