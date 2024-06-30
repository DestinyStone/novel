package com.entity.vo;

import com.entity.pojo.Novel;
import com.entity.pojo.Tag;

import java.util.List;


public class NovelVO extends Novel {
    private List<Tag> tagList;

    private Long collectCount;

    // 平均分
    private Double score;

    // 评论数
    private Long commentCount;

    // 排序分值
    private Double scoreSort;

    // 阅读时间
    private Long readTime;

    public Long getReadTime() {
        return readTime;
    }

    public void setReadTime(Long readTime) {
        this.readTime = readTime;
    }

    public Long getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Long commentCount) {
        this.commentCount = commentCount;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Long getCollectCount() {
        return collectCount;
    }

    public void setCollectCount(Long collectCount) {
        this.collectCount = collectCount;
    }

    public Double getScoreSort() {
        return scoreSort;
    }

    public void setScoreSort(Double scoreSort) {
        this.scoreSort = scoreSort;
    }

    public List<Tag> getTagList() {
        return tagList;
    }

    public void setTagList(List<Tag> tagList) {
        this.tagList = tagList;
    }
}
