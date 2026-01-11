package com.kmbeast.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 基于用户的协同过滤推荐工具类
 */
public class UserBasedCFUtil {

    /**
     * 验证协同过滤推荐功能的main方法
     */
    public static void main(String[] args) {
        // 1. 准备测试数据
        List<Integer> itemIds = Arrays.asList(101, 102, 103, 104, 105); // 物品ID列表
        List<Score> scores = Arrays.asList(
                new Score(1, 101, 4.8), new Score(1, 102, 3.2), new Score(1, 103, 4.5),
                new Score(2, 101, 3.7), new Score(2, 102, 2.1), new Score(2, 104, 3.9),
                new Score(3, 101, 3.0), new Score(3, 103, 4.9), new Score(3, 105, 4.2),
                new Score(4, 102, 4.1), new Score(4, 103, 3.8), new Score(4, 104, 5.0),
                new Score(5, 101, 4.7), new Score(5, 104, 4.3), new Score(5, 105, 3.5)
        );

        // 2. 构建用户-物品评分矩阵
        Map<Integer, Map<Integer, Double>> userItemMatrix =
                UserBasedCFUtil.buildUserItemMatrix(itemIds, scores);

        System.out.println("用户-物品评分矩阵:");
        userItemMatrix.forEach((userId, ratings) -> {
            System.out.printf("用户%d: %s%n", userId, ratings);
        });

        // 3. 创建协同过滤工具实例
        UserBasedCFUtil cfUtil = new UserBasedCFUtil(userItemMatrix);

        // 4. 测试用户相似度计算
        System.out.println("\n用户相似度测试:");
        double sim12 = cfUtil.cosineSimilarity(
                userItemMatrix.get(1),
                userItemMatrix.get(2)
        );
        System.out.printf("用户1和用户2的相似度: %.4f%n", sim12);

        double sim13 = cfUtil.cosineSimilarity(
                userItemMatrix.get(1),
                userItemMatrix.get(3)
        );
        System.out.printf("用户1和用户3的相似度: %.4f%n", sim13);

        // 5. 测试推荐功能
        System.out.println("\n推荐测试:");
        int targetUserId = 1;
        int topN = 3;
        List<Integer> recommendations = cfUtil.recommendItems(targetUserId, topN);

        System.out.printf("为用户%d推荐的%d个物品: %s%n",
                targetUserId, topN, recommendations);

        // 6. 验证推荐结果
        System.out.println("\n推荐结果验证:");
        Map<Integer, Double> targetUserRatings = userItemMatrix.get(targetUserId);
        System.out.println("用户已评分的物品:");
        targetUserRatings.entrySet().stream()
                .filter(e -> e.getValue() > 0)
                .forEach(e -> System.out.printf("物品%d: %.1f分%n", e.getKey(), e.getValue()));

        System.out.println("\n推荐物品详情:");
        recommendations.forEach(itemId -> {
            if (!targetUserRatings.containsKey(itemId) || targetUserRatings.get(itemId) == 0) {
                System.out.printf("物品%d: 未评分(推荐)%n", itemId);
            } else {
                System.out.printf("物品%d: 已评分%.1f(不推荐)%n", itemId, targetUserRatings.get(itemId));
            }
        });
    }

    // 用户-物品评分矩阵
    private final Map<Integer, Map<Integer, Double>> userItemMatrix;

    public UserBasedCFUtil(Map<Integer, Map<Integer, Double>> userItemMatrix) {
        this.userItemMatrix = userItemMatrix;
    }

    /**
     * 计算两个用户的余弦相似度
     *
     * @param user1 用户1的评分向量
     * @param user2 用户2的评分向量
     * @return 余弦相似度
     */
    public double cosineSimilarity(Map<Integer, Double> user1, Map<Integer, Double> user2) {
        double dotProduct = 0.0;
        double norm1 = 0.0, norm2 = 0.0;
        // 计算点积和范数
        for (Integer item : user1.keySet()) {
            if (user2.containsKey(item)) {
                dotProduct += user1.get(item) * user2.get(item);
            }
            norm1 += Math.pow(user1.get(item), 2);
        }
        for (Integer item : user2.keySet()) {
            norm2 += Math.pow(user2.get(item), 2);
        }
        // 计算余弦相似度
        return dotProduct / (Math.sqrt(norm1) * Math.sqrt(norm2));
    }

    /**
     * 为目标用户生成推荐物品列表
     *
     * @param targetUserId 目标用户ID
     * @param topN         推荐物品数量
     * @return 推荐物品ID列表
     */
    public List<Integer> recommendItems(int targetUserId, int topN) {
        Map<Integer, Double> targetUser = userItemMatrix.get(targetUserId);
        if (targetUser == null) {
            return new ArrayList<>();
        }
        // 优先队列用于存储相似用户
        PriorityQueue<SimilarUser> heap = new PriorityQueue<>();
        // 计算所有用户的相似度
        for (Integer otherUserId : userItemMatrix.keySet()) {
            if (otherUserId == targetUserId) continue; // 跳过目标用户
            double sim = cosineSimilarity(targetUser, userItemMatrix.get(otherUserId));
            heap.add(new SimilarUser(otherUserId, sim));
        }
        // 聚合相似用户评分
        Map<Integer, Double> itemScores = new HashMap<>();
        for (int i = 0; i < 20 && !heap.isEmpty(); i++) { // 取Top20相似用户
            SimilarUser simUser = heap.poll();
            for (Map.Entry<Integer, Double> entry : userItemMatrix.get(simUser.userId).entrySet()) {
                if (targetUser.containsKey(entry.getKey())) {
                    itemScores.merge(entry.getKey(), entry.getValue() * simUser.similarity, Double::sum);
                }
            }
        }
        // 返回得分最高的物品
        return itemScores.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(topN)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    /**
     * 相似用户类
     */
    private static class SimilarUser implements Comparable<SimilarUser> {
        int userId;
        double similarity;

        public SimilarUser(int userId, double similarity) {
            this.userId = userId;
            this.similarity = similarity;
        }

        @Override
        public int compareTo(SimilarUser other) {
            return Double.compare(other.similarity, this.similarity); // 按相似度降序排序
        }
    }

    /**
     * 评分内部类
     */
    @Setter
    @Getter
    @AllArgsConstructor
    public static class Score {
        /**
         * 评分者用户ID
         */
        private Integer userId;
        /**
         * 物品ID
         */
        private Integer itemId;
        /**
         * 评分分数
         */
        private Double score;
    }

    /**
     * 构建用户-物品评分矩阵
     *
     * @param itemIds  物品ID列表
     * @param scoreVOS 评分数据集合
     * @return Map<Integer, Map < Integer, Double>>
     */
    public static Map<Integer, Map<Integer, Double>> buildUserItemMatrix(List<Integer> itemIds, List<Score> scoreVOS) {
        // 获取所有物品ID
        Set<Integer> allNewsIds = new HashSet<>(itemIds);
        // 获取所有评分者用户ID
        Set<Integer> allUserIds = new HashSet<>();
        for (Score score : scoreVOS) {
            allUserIds.add(score.getUserId());
        }
        // 构建用户-物品评分矩阵
        Map<Integer, Map<Integer, Double>> userItemMatrix = new HashMap<>();
        // 初始化每个用户的评分映射，默认值为0
        for (Integer userId : allUserIds) {
            Map<Integer, Double> itemScores = new HashMap<>();
            for (Integer newsId : allNewsIds) {
                itemScores.put(newsId, 0.0);
            }
            userItemMatrix.put(userId, itemScores);
        }
        // 填充已有评分
        scoreVOS.forEach(scoreVO -> {
            Integer userId = scoreVO.getUserId();
            Integer itemId = scoreVO.getItemId();
            Double score = Double.valueOf(scoreVO.getScore());
            userItemMatrix.computeIfAbsent(userId, k -> new HashMap<>()).put(itemId, score);
        });
        return userItemMatrix;
    }
}