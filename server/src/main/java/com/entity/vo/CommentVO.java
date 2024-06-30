package com.entity.vo;

import com.entity.pojo.Comment;

/**
 * @Author zhou xiao fen
 * @Date 2024/2/17 11:12
 **/
public class CommentVO extends Comment {
    private String novelName;

    public String getNovelName() {
        return novelName;
    }

    public void setNovelName(String novelName) {
        this.novelName = novelName;
    }
}
