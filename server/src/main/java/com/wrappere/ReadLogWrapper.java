package com.wrappere;

import cn.hutool.core.bean.BeanUtil;
import com.common.api.BaseEntityWrapper;
import com.entity.pojo.Collect;
import com.entity.pojo.Novel;
import com.entity.pojo.ReadLog;
import com.entity.vo.CollectVO;
import com.entity.vo.NovelVO;
import com.entity.vo.ReadLogVO;
import com.service.NovelService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;


public class ReadLogWrapper  extends BaseEntityWrapper<ReadLog, ReadLogVO> {

    private static NovelService novelService;

    public static ReadLogWrapper build() {
        return new ReadLogWrapper();
    }

    @Autowired
    public void setNovelService(NovelService novelService) {
        ReadLogWrapper.novelService = novelService;
    }

    @Override
    public ReadLogVO entityVO(ReadLog entity) {
        ReadLogVO vo = Objects.requireNonNull(BeanUtil.copyProperties(entity, ReadLogVO.class));

        Novel novel = novelService.getById(vo.getNovelId());
        if (novel == null) {
            return vo;
        }
        NovelVO novelVO = NovelWrapper.build().entityVO(novel);
        BeanUtil.copyProperties(novelVO, vo);
        return vo;
    }
}