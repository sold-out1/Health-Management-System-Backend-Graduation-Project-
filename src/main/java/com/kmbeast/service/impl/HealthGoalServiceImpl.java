package com.kmbeast.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kmbeast.mapper.DietHistoryMapper;
import com.kmbeast.mapper.HealthGoalMapper;
import com.kmbeast.mapper.RecipeMapper;
import com.kmbeast.pojo.api.ApiResult;
import com.kmbeast.pojo.api.Result;
import com.kmbeast.pojo.entity.DietHistory;
import com.kmbeast.pojo.entity.HealthGoal;
import com.kmbeast.pojo.entity.Recipe;
import com.kmbeast.service.HealthGoalService;
import com.kmbeast.context.LocalThreadHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class HealthGoalServiceImpl extends ServiceImpl<HealthGoalMapper, HealthGoal> implements HealthGoalService {

    @Resource
    private DietHistoryMapper dietHistoryMapper;
    @Resource
    private RecipeMapper recipeMapper;

    /**
     * 新增或修改目标
     */
    public Result<String> saveOrUpdateGoal(HealthGoal goal) {
        Integer userId = LocalThreadHolder.getUserId();
        goal.setUserId(userId);

        if (goal.getId() == null) {
            goal.setStatus(0);
        }

        this.saveOrUpdate(goal);
        return ApiResult.success("目标保存成功");
    }

    /**
     * 获取我的目标列表（按状态分类）- 采用内存对比法保证准确性
     */
    public Result<Map<String, List<HealthGoal>>> getMyGoals() {
        Integer userId = LocalThreadHolder.getUserId();

        // 1. 先查出该用户所有【状态为进行中(0)】的目标
        List<HealthGoal> currentGoals = this.list(new QueryWrapper<HealthGoal>()
                .eq("user_id", userId)
                .eq("status", 0)
                .orderByDesc("create_time"));

        boolean hasExpired = false;

        // 2. 在 Java 内存中逐个比对 endTime
        LocalDateTime now = LocalDateTime.now();
        for (HealthGoal goal : currentGoals) {
            // 注意这里改成了 getEndTime()
            if (goal.getEndTime() != null) {
                if (now.isAfter(goal.getEndTime())) {
                    System.out.println("【系统提示】检测到目标已过期！目标ID: " + goal.getId() + "，截止时间: " + goal.getEndTime());

                    goal.setStatus(2); // 设置为未完成
                    this.updateById(goal);
                    hasExpired = true;
                }
            }
        }

        // 3. 如果刚才有目标过期了，重新查询一次进行中的列表
        if (hasExpired) {
            currentGoals = this.list(new QueryWrapper<HealthGoal>()
                    .eq("user_id", userId)
                    .eq("status", 0)
                    .orderByDesc("create_time"));
        }

        // 4. 查询历史（已完成1 或 未完成2）
        List<HealthGoal> historyGoals = this.list(new QueryWrapper<HealthGoal>()
                .eq("user_id", userId)
                .ne("status", 0)
                .orderByDesc("create_time"));

        Map<String, List<HealthGoal>> map = new HashMap<>();
        map.put("current", currentGoals);
        map.put("history", historyGoals);

        return ApiResult.success(map);
    }

    /**
     * 智能分析与推荐 (核心亮点)
     */
    public Result<Map<String, Object>> analyzeAndRecommend(Integer goalId) {
        HealthGoal goal = this.getById(goalId);

        // 分析前也做一次单条检查（注意这里改成了 getEndTime()）
        if (goal != null && goal.getStatus() == 0 && goal.getEndTime() != null) {
            if (LocalDateTime.now().isAfter(goal.getEndTime())) {
                goal.setStatus(2);
                this.updateById(goal);
            }
        }

        Integer userId = LocalThreadHolder.getUserId();
        List<DietHistory> recentDiets = dietHistoryMapper.selectList(new QueryWrapper<DietHistory>()
                .eq("user_id", userId)
                .last("LIMIT 20"));

        String advice = "";
        QueryWrapper<Recipe> recipeQuery = new QueryWrapper<>();

        if (goal.getType() == 1) {
            advice = "根据您的减肥目标，建议控制碳水化合物摄入，增加膳食纤维。";
            if (recentDiets.isEmpty()) {
                advice += "检测到您最近没有饮食记录，建议开始记录饮食以便更好分析。";
            }
            recipeQuery.in("type_id", 1, 3).last("LIMIT 3");
        } else {
            advice = "根据您的增肌目标，建议增加蛋白质摄入（如鸡胸肉、牛肉、蛋类），并在力量训练后补充能量。";
            recipeQuery.in("type_id", 2, 4).last("LIMIT 3");
        }

        List<Recipe> recommendedRecipes = recipeMapper.selectList(recipeQuery);

        Map<String, Object> result = new HashMap<>();
        result.put("advice", advice);
        result.put("recipes", recommendedRecipes);
        result.put("goal", goal);

        return ApiResult.success(result);
    }
}