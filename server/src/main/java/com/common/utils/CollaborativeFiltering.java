package com.common.utils;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.entity.pojo.*;
import com.entity.vo.NovelVO;
import com.service.*;
import com.wrappere.NovelWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 协同过滤算法类
 */
@Component
public class CollaborativeFiltering {
    // 用户评分数据存储结构：用户ID -> 图书Id -> 图书
    private Map<Integer, Map<Integer, NovelVO>> userRatings;

    @Autowired
    private NovelService novelService;

    @Autowired
    private ScoreService scoreService;

    @Autowired
    private CollectService collectService;

    @Autowired
    private UserService userService;

    public CollaborativeFiltering() {


    }

    public void initModel(List<Novel> novels) {
        List<User> list = userService.list();
        Map<Integer, NovelVO> novelMap = NovelWrapper.build().listVO(novels).stream().collect(Collectors.toMap(Novel::getId, Function.identity()));

        if (CollUtil.isEmpty(list)) {
            return;
        }

        // 初始化数据
        userRatings = new HashMap<>();
        for (User user : list) {
            userRatings.put(user.getUserId(), new HashMap<>());
        }

        // 获取用户给出评分的书籍
        List<Score> scores = scoreService.list();
        for (Score score : scores) {
            Map<Integer, NovelVO> map = userRatings.get(score.getUserId());
            if (map == null) {
                continue;
            }
            NovelVO novel = novelMap.get(score.getNovelId());
            if (novel == null) {
                continue;
            }
            NovelVO novelVO = BeanUtil.copyProperties(novel, NovelVO.class);
            novelVO.setScoreSort(novel.getScore());
            map.put(score.getNovelId(), novelVO);
        }

        // 获取用户加入书架的书籍
        List<Collect> collects = collectService.list();
        for (Collect collect : collects) {
            Map<Integer, NovelVO> map = userRatings.get(collect.getUserId());
            if (map == null) {
                continue;
            }
            NovelVO novel = novelMap.get(collect.getNovelId());
            if (novel == null) {
                continue;
            }
            novel.setScoreSort(10.0);
            NovelVO novelVO = BeanUtil.copyProperties(novel, NovelVO.class);
            novelVO.setScore(10.0);
            if (map.containsKey(novel.getId())) {
                map.replace(novel.getId(), novelVO);
            }else {
                map.put(novel.getId(), novelVO);
            }
        }
    }



    // 计算用户之间的相似度
    private double calculateUserSimilarity(int user1, int user2) {
        Set<Integer> commonItems = new HashSet<>(userRatings.get(user1).keySet());
        commonItems.retainAll(userRatings.get(user2).keySet()); // 获取两个用户共同评价过的物品ID集合

        if (commonItems.size() == 0) {
            return 0.0; // 如果没有共同评分的物品，相似度为0
        }

        double sumOfProducts = 0.0;
        double sumOfSquaresUser1 = 0.0;
        double sumOfSquaresUser2 = 0.0;

        for (int item : commonItems) {
            double rating1 = userRatings.get(user1).get(item).getScoreSort();
            double rating2 = userRatings.get(user2).get(item).getScoreSort();
            sumOfProducts += rating1 * rating2; // 两用户评分的乘积之和
            sumOfSquaresUser1 += Math.pow(rating1, 2); // 用户1评分的平方和
            sumOfSquaresUser2 += Math.pow(rating2, 2); // 用户2评分的平方和
        }

        // 计算余弦相似度作为用户相似度
        return sumOfProducts / (Math.sqrt(sumOfSquaresUser1) * Math.sqrt(sumOfSquaresUser2));
    }

    // 为目标用户进行推荐
    public List<NovelVO> recommendItems(int targetUser) {

        List<NovelVO> result = new ArrayList<>();
        for (int otherUser : userRatings.keySet()) {
            if (otherUser == targetUser) {
                continue; // 不计算目标用户本身
            }
            double similarity = calculateUserSimilarity(targetUser, otherUser); // 计算目标用户与其他用户的相似度
            if (similarity <= 0) {
                continue; // 相似度小于等于0，跳过
            }
            for (int item : userRatings.get(otherUser).keySet()) {

                if (!userRatings.get(targetUser).containsKey(item)) { // 只针对目标用户未评分的物品进行推荐
                    NovelVO novelVO = userRatings.get(otherUser).get(item);
                    result.add(novelVO);
                }
            }
        }
        return result; // 返回推荐结果
    }

}
