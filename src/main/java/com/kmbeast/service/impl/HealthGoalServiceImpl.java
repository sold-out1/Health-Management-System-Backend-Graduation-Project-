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
        Integer userId = LocalThreadHolder.getUserId(); // 获取当前登录用户ID
        goal.setUserId(userId);

        // 如果是新增，默认状态为进行中
        if (goal.getId() == null) {
            goal.setStatus(0);
        }

        this.saveOrUpdate(goal);
        return ApiResult.success("目标保存成功");
    }

    /**
     * 获取我的目标列表（按状态分类）
     */
    public Result<Map<String, List<HealthGoal>>> getMyGoals() {
        Integer userId = LocalThreadHolder.getUserId();

        // 查询进行中
        List<HealthGoal> current = this.list(new QueryWrapper<HealthGoal>()
                .eq("user_id", userId).eq("status", 0).orderByDesc("create_time"));

        // 查询历史（已完成或未完成）
        List<HealthGoal> history = this.list(new QueryWrapper<HealthGoal>()
                .eq("user_id", userId).ne("status", 0).orderByDesc("create_time"));

        Map<String, List<HealthGoal>> map = new HashMap<>();
        map.put("current", current);
        map.put("history", history);

        return ApiResult.success(map);
    }

    /**
     * 智能分析与推荐 (核心亮点)
     * @param goalId 目标ID
     */
    public Result<Map<String, Object>> analyzeAndRecommend(Integer goalId) {
        HealthGoal goal = this.getById(goalId);
        Integer userId = LocalThreadHolder.getUserId();

        // 1. 获取用户最近 3 天的饮食记录

        List<DietHistory> recentDiets = dietHistoryMapper.selectList(new QueryWrapper<DietHistory>()
                .eq("user_id", userId)
                .last("LIMIT 20")); // 简单取最近20条

        // 2. 分析逻辑
        String advice = "";
        QueryWrapper<Recipe> recipeQuery = new QueryWrapper<>();

        if (goal.getType() == 1) {
            // === 减肥目标 ===
            advice = "根据您的减肥目标，建议控制碳水化合物摄入，增加膳食纤维。";
            if (recentDiets.isEmpty()) {
                advice += "检测到您最近没有饮食记录，建议开始记录饮食以便更好分析。";
            }
            // 推荐逻辑：假设食谱表 type_id=1 是低脂/素食 (请根据你数据库实际 type_id 修改)
            recipeQuery.in("type_id", 1, 3).last("LIMIT 3");

        } else {
            // === 增肌目标 ===
            advice = "根据您的增肌目标，建议增加蛋白质摄入（如鸡胸肉、牛肉、蛋类），并在力量训练后补充能量。";
            // 推荐逻辑：假设食谱表 type_id=2 是肉类/高蛋白 (请根据你数据库实际 type_id 修改)
            recipeQuery.in("type_id", 2, 4).last("LIMIT 3");
        }

        List<Recipe> recommendedRecipes = recipeMapper.selectList(recipeQuery);

        Map<String, Object> result = new HashMap<>();
        result.put("advice", advice);
        result.put("recipes", recommendedRecipes); // 推荐的食谱列表

        return ApiResult.success(result);
    }
}