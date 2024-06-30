package com.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.common.api.Query;
import com.common.api.R;
import com.common.exception.ServiceException;
import com.common.utils.AuthUtil;
import com.common.utils.CollaborativeFiltering;
import com.common.utils.Func;
import com.entity.pojo.Collect;
import com.entity.pojo.Novel;
import com.entity.pojo.Tag;
import com.entity.vo.NovelVO;
import com.service.CollectService;
import com.service.NovelService;
import com.service.TagService;
import com.wrappere.NovelWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/novel")
public class NovelController {
    @Autowired
    private NovelService service;

    @Autowired
    private CollectService collectService;

    @Autowired
    private CollaborativeFiltering filtering;

    @GetMapping("/view")
    public synchronized R view(@RequestParam("id") Integer id) {
        Novel novel = service.getById(id);
        novel.setViews(novel.getViews() + 1);
        service.updateById(novel);
        return R.success("操作成功");
    }

    @PostMapping("/upload")
    public R upload(MultipartFile file) {
        InputStream in = null;
        OutputStream out = null;
        try {
            in = file.getInputStream();
            byte[] buffer = new byte[in.available()];
            in.read(buffer);

            String upload = System.getProperty("user.dir") + File.separator + "target" + File.separator + "classes" + File.separator + "static" + File.separator + "upload";
            upload += File.separator + file.getOriginalFilename();
            out = new FileOutputStream(upload);
            out.write(buffer);
            return R.data("http://localhost:9999/upload/" + file.getOriginalFilename());
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        throw new ServiceException("文件上传失败");
    }
    @RequestMapping("/page")
    public R page(NovelVO vo, Query query) {
        LambdaQueryWrapper<Novel> wrapper = convert(vo);
        Page<Novel> page = new Page<>(query.getCurrent(), query.getSize());
        page = service.page(page, wrapper);
        return R.data(NovelWrapper.build().pageVO(page));
    }

    @RequestMapping("/list")
    public R list(NovelVO vo) {
        LambdaQueryWrapper<Novel> wrapper = convert(vo);

        List<Novel> list = service.list(wrapper);
        return R.data(NovelWrapper.build().listVO(list));
    }

    // 热门榜
    @RequestMapping("/list/fire")
    public R<List<NovelVO>> fireList(NovelVO vo) {
        // 阅读量权重
        double viewWeight = 1.0;
        // 评论数权重
        double commentWeight = 1.0;
        // 收藏量权重
        double collectWeight = 1.0;
        // 评分权重
        double scoreWeight = 0.1;

        LambdaQueryWrapper<Novel> wrapper = convert(vo);
        List<Novel> list = service.list(wrapper);
        List<NovelVO> novelVOS = NovelWrapper.build().listVO(list);

        novelVOS = novelVOS.stream().map(item -> {
            double scoreSort = 0.0;
            scoreSort += item.getViews() * viewWeight;
            scoreSort += item.getCommentCount() * commentWeight;
            scoreSort += item.getCollectCount() * collectWeight;
            scoreSort += item.getScore() * scoreWeight;
            item.setScoreSort(scoreSort);
            return item;
        }).sorted((x, y) -> {
            if (x.getScoreSort() > y.getScoreSort()) {
                return -1;
            }
            if (x.getScoreSort() <= y.getScoreSort()) {
                return 1;
            }
            return 0;
        }).limit(4).collect(Collectors.toList());

        return R.data(novelVOS);
    }

    // 新书榜
    @RequestMapping("/list/news")
    public R newsList(NovelVO vo) {
        LambdaQueryWrapper<Novel> wrapper = convert(vo);
        List<Novel> list = service.list(wrapper);
        List<NovelVO> novelVOS = NovelWrapper.build().listVO(list);

        novelVOS = novelVOS.stream().map(item -> {
            item.setScoreSort(item.getCreateDate().getTime() * 1.0);
            return item;
        }).sorted((x, y) -> {
            if (x.getScoreSort() > y.getScoreSort()) {
                return -1;
            }
            if (x.getScoreSort() <= y.getScoreSort()) {
                return 1;
            }
            return 0;
        }).limit(4).collect(Collectors.toList());

        return R.data(novelVOS);
    }

    // 收藏榜
    @RequestMapping("/list/collect")
    public R collectList(NovelVO vo) {
        LambdaQueryWrapper<Novel> wrapper = convert(vo);
        List<Novel> list = service.list(wrapper);
        List<NovelVO> novelVOS = NovelWrapper.build().listVO(list);

        novelVOS = novelVOS.stream().map(item -> {
            item.setScoreSort(item.getCollectCount() * 1.0);
            return item;
        }).sorted((x, y) -> {
            if (x.getScoreSort() > y.getScoreSort()) {
                return -1;
            }
            if (x.getScoreSort() <= y.getScoreSort()) {
                return 1;
            }
            return 0;
        }).limit(4).collect(Collectors.toList());

        return R.data(novelVOS);
    }

    // 评分榜
    @RequestMapping("/list/score")
    public R scoreList(NovelVO vo) {
        LambdaQueryWrapper<Novel> wrapper = convert(vo);
        List<Novel> list = service.list(wrapper);
        List<NovelVO> novelVOS = NovelWrapper.build().listVO(list);

        novelVOS = novelVOS.stream().map(item -> {
            item.setScoreSort(item.getScore());
            return item;
        }).sorted((x, y) -> {
            if (x.getScoreSort() > y.getScoreSort()) {
                return -1;
            }
            if (x.getScoreSort() <= y.getScoreSort()) {
                return 1;
            }
            return 0;
        }).limit(4).collect(Collectors.toList());

        return R.data(novelVOS);
    }

    // 个性推荐
    @RequestMapping("/list/standard")
    public R standardList(NovelVO vo) {
        LambdaQueryWrapper<Novel> wrapper = convert(vo);
        List<Novel> list = service.list(wrapper);

        filtering.initModel(list);
        List<NovelVO> novelVOS = filtering.recommendItems(AuthUtil.getUserId());
        R<List<NovelVO>> fireList = fireList(vo);

        novelVOS.addAll(fireList.getData());

        novelVOS = novelVOS.stream().limit(4).collect(Collectors.toList());

        return R.data(novelVOS);
    }


    private LambdaQueryWrapper convert(NovelVO vo) {
        LambdaQueryWrapper<Novel> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StrUtil.isNotBlank(vo.getName()), Novel::getName, vo.getName());
        wrapper.like(StrUtil.isNotBlank(vo.getAuthor()), Novel::getAuthor, vo.getAuthor());
        wrapper.eq(StrUtil.isNotBlank(vo.getType()), Novel::getType, vo.getType());
        wrapper.eq(StrUtil.isNotBlank(vo.getState()), Novel::getState, vo.getState());

        if (StrUtil.isNotBlank(vo.getTags())) {
            List<Long> longs = Func.toLongList(vo.getTags());
            wrapper.and(childerWrapper -> {
                for (Long aLong : longs) {
                    childerWrapper.like(Novel::getTags, aLong);
                }
            });
        }

        return wrapper;
    }

    @RequestMapping("/update/{id}")
    public R update(@PathVariable("id") Integer id,  @RequestBody Novel dto) {
        dto.setId(id);
        service.updateById(dto);
        return R.success("操作成功");
    }

    @RequestMapping("/delete")
    public R userDelete(@RequestParam("id") Integer id) {
        service.removeById(id);
        return R.success("操作成功");
    }

    @RequestMapping("/save")
    public R userSave(@RequestBody Novel dto) {
        dto.setId(null);
        dto.setNumber("0");
        dto.setViews(0);
        dto.setCreateDate(new Date());
        service.save(dto);
        return R.success("操作成功");
    }

    @RequestMapping("/id")
    public R id(@RequestParam("id") Integer id) {
        Novel novel = service.getById(id);
        return R.data(NovelWrapper.build().entityVO(novel));
    }
}
