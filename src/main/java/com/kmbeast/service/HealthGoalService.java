package com.kmbeast.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kmbeast.pojo.api.Result;
import com.kmbeast.pojo.entity.HealthGoal;
import java.util.List;
import java.util.Map;

public interface HealthGoalService extends IService<HealthGoal> {


    Result<Map<String, List<HealthGoal>>> getMyGoals();


    Result<String> saveOrUpdateGoal(HealthGoal goal);

    Result<Map<String, Object>> analyzeAndRecommend(Integer goalId);
}