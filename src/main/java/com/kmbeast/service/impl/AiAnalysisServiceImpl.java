package com.kmbeast.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.kmbeast.context.LocalThreadHolder;
import com.kmbeast.pojo.api.ApiResult;
import com.kmbeast.pojo.api.Result;
import com.kmbeast.pojo.dto.DietHistoryQueryDto;
import com.kmbeast.pojo.dto.HealthRecordQueryDto;
import com.kmbeast.pojo.entity.HealthGoal;
import com.kmbeast.pojo.vo.*;
import com.kmbeast.service.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AiAnalysisServiceImpl implements AiAnalysisService {

    @Resource
    private UserService userService;
    @Resource
    private DietHistoryService dietService;
    @Resource
    private HealthRecordService healthRecordService;
    @Resource
    private RecipeService recipeService;
    @Resource
    private HealthGoalService healthGoalService;

    // 保持 API KEY 不动
    private final String API_KEY = "sk-b89dde3b88fa4812af560421cffe8440";
    private final String AI_URL = "https://api.deepseek.com/chat/completions";

    @Override
    public Result<String> generateHealthReport() {
        Integer userId = LocalThreadHolder.getUserId();

        // 1. 获取基础用户名
        Result<UserVO> userRes = userService.getById(userId);
        String username = (userRes.getData() != null) ? userRes.getData().getUsername() : "用户";

        // 2. ⭐ 核心修改：适配 HealthGoal 的 type 和 status 字段
        Result<Map<String, List<HealthGoal>>> goalRes = healthGoalService.getMyGoals();
        String goalStr = "尚未设定明确的健康目标";

        if (goalRes.getData() != null && !goalRes.getData().isEmpty()) {
            goalStr = goalRes.getData().values().stream()
                    .flatMap(List::stream)
                    .map(g -> {
                        // 转换类型：1-减肥，2-增肌
                        String typeName = (g.getType() != null && g.getType() == 1) ? "减肥" : "增肌";
                        // 转换状态：0-进行中，1-已完成，2-未完成
                        String statusName = "进行中";
                        if (g.getStatus() != null) {
                            if (g.getStatus() == 1) statusName = "已完成";
                            else if (g.getStatus() == 2) statusName = "未完成";
                        }
                        return String.format("目标:%s(当前值:%s, 目标值:%s)[%s]",
                                typeName, g.getCurrentValue(), g.getTargetValue(), statusName);
                    })
                    .collect(Collectors.joining("；"));
        }

        // 3. 获取健康记录（10项指标）
        HealthRecordQueryDto hrDto = new HealthRecordQueryDto();
        hrDto.setCurrent(1);
        hrDto.setSize(3);
        Result<List<HealthRecordVO>> hrRes = healthRecordService.listUser(hrDto);

        String healthDataStr = "暂无详细健康数据记录";
        String currentBodyStatus = "尚未录入体测数据";

        if (hrRes.getData() != null && !hrRes.getData().isEmpty()) {
            List<AiHealthDataVO> aiList = hrRes.getData().stream()
                    .map(obj -> BeanUtil.copyProperties(obj, AiHealthDataVO.class))
                    .collect(Collectors.toList());

            AiHealthDataVO latest = aiList.get(0);
            currentBodyStatus = String.format("当前Bmi:%s, 当前体重:%s kg",
                    latest.getBmi(), latest.getWeight());

            healthDataStr = aiList.stream()
                    .map(r -> String.format(
                            "【时间:%s】体重:%skg, 心率:%s, 血压:%s/%s, 血糖:%s, 体脂:%s%%",
                            r.getCreateTime(), r.getWeight(), r.getHeartRate(),
                            r.getHighPressure(), r.getLowPressure(), r.getBloodSugar(), r.getBodyFatRate()
                    ))
                    .collect(Collectors.joining(";\n"));
        }

        // 4. 获取饮食记录
        DietHistoryQueryDto dietDto = new DietHistoryQueryDto();
        dietDto.setCurrent(1);
        dietDto.setSize(5);
        Result<List<DietHistoryVO>> dietRes = dietService.listUser(dietDto);
        String dietDataStr = "近期无饮食记录";
        if (dietRes.getData() != null && !dietRes.getData().isEmpty()) {
            dietDataStr = dietRes.getData().stream()
                    .map(d -> d.getRecipeName() + "(" + d.getValue() + "g)")
                    .collect(Collectors.joining(", "));
        }

        // 5. 获取系统食谱名
        List<String> recipes = recipeService.listPublicNames();

        // 6. 拼接 Prompt
        String prompt = String.format(
                "你是一个健康管理专家。请根据以下数据进行分析：\n" +
                        "用户：%s\n" +
                        "【当前核心目标】：%s\n" +
                        "【当前身体状态】：%s\n" +
                        "【历史指标趋势】：\n%s\n" +
                        "【近期饮食摄入】：%s\n" +
                        "【系统可选食谱】：%s\n\n" +
                        "要求：\n" +
                        "1. 结合“核心目标”评价其身体指标和饮食是否匹配目标。\n" +
                        "2. 从“可选食谱”中推荐2个最符合其目标的食谱并说明原因。\n" +
                        "3. 用Markdown格式输出报告。",
                username, goalStr, currentBodyStatus, healthDataStr, dietDataStr, String.join(",", recipes)
        );

        return callAi(prompt);
    }

    private Result<String> callAi(String prompt) {
        try {
            Map<String, Object> body = new HashMap<>();
            body.put("model", "deepseek-chat");
            List<Object> messages = new ArrayList<>();
            messages.add(JSONUtil.createObj().set("role", "user").set("content", prompt));
            body.put("messages", messages);

            String response = HttpUtil.createPost(AI_URL)
                    .header("Authorization", "Bearer " + API_KEY)
                    .body(JSONUtil.toJsonStr(body))
                    .timeout(60000).execute().body();

            String aiReply = JSONUtil.parseObj(response).getByPath("choices[0].message.content", String.class);
            return ApiResult.success("报告生成成功", aiReply);
        } catch (Exception e) {
            return ApiResult.error("AI服务调用失败");
        }
    }
}