package com.wrappere;

import cn.hutool.core.bean.BeanUtil;
import com.common.api.BaseEntityWrapper;
import com.common.utils.Func;
import com.entity.pojo.Collect;
import com.entity.pojo.Novel;
import com.entity.pojo.Tag;
import com.entity.vo.CollectVO;
import com.entity.vo.NovelVO;
import com.service.NovelService;
import com.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class CollectWrapper  extends BaseEntityWrapper<Collect, CollectVO> {

    private static NovelService novelService;

    public static CollectWrapper build() {
        return new CollectWrapper();
    }

    @Autowired
    public void setNovelService(NovelService novelService) {
        CollectWrapper.novelService = novelService;
    }

    @Override
    public CollectVO entityVO(Collect entity) {
        CollectVO vo = Objects.requireNonNull(BeanUtil.copyProperties(entity, CollectVO.class));

        Novel novel = novelService.getById(vo.getNovelId());
        if (novel == null) {
            return vo;
        }
        NovelVO novelVO = NovelWrapper.build().entityVO(novel);
        BeanUtil.copyProperties(novelVO, vo);
        return vo;
    }
}