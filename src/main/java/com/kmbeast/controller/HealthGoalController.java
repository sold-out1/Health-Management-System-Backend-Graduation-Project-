package com.kmbeast.controller;

import com.kmbeast.pojo.api.ApiResult;
import com.kmbeast.pojo.api.Result;
import com.kmbeast.pojo.entity.HealthGoal;
import com.kmbeast.service.impl.HealthGoalServiceImpl;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/health-goal")
public class HealthGoalController {

    @Resource
    private HealthGoalServiceImpl healthGoalService;

    // 保存或更新目标
    @PostMapping("/save")
    public Result<String> save(@RequestBody HealthGoal goal) {
        return healthGoalService.saveOrUpdateGoal(goal);
    }

    // 删除目标
    @DeleteMapping("/delete/{id}")
    public Result<String> delete(@PathVariable Integer id) {
        healthGoalService.removeById(id);
        return ApiResult.success("删除成功");
    }

    // 获取我的目标列表
    @GetMapping("/list")
    public Result<Map<String, List<HealthGoal>>> list() {
        return healthGoalService.getMyGoals();
    }

    // 标记状态（完成/未完成）
    @PostMapping("/updateStatus")
    public Result<String> updateStatus(@RequestBody HealthGoal goal) {
        healthGoalService.updateById(goal);
        return ApiResult.success("状态更新成功");
    }

    // 获取智能分析建议和推荐食谱
    @GetMapping("/analyze/{id}")
    public Result<Map<String, Object>> analyze(@PathVariable Integer id) {
        return healthGoalService.analyzeAndRecommend(id);
    }
}