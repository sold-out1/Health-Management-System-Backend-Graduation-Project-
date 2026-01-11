package com.kmbeast.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kmbeast.pojo.api.Result;
import com.kmbeast.pojo.entity.HealthGoal;
import java.util.List;
import java.util.Map;

public interface HealthGoalService extends IService<HealthGoal> {

    // ⭐ 在接口中添加这个方法声明，这样 AiAnalysisServiceImpl 才能调用它
    Result<Map<String, List<HealthGoal>>> getMyGoals();

    // 如果后续还需要调用其他方法，也建议一并加上
    Result<String> saveOrUpdateGoal(HealthGoal goal);

    Result<Map<String, Object>> analyzeAndRecommend(Integer goalId);
}