package com.kmbeast.service.impl;

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
import java.time.ZoneId;
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

    private final String API_KEY = "sk-b89dde3b88fa4812af560421cffe8440";
    private final String AI_URL = "https://api.deepseek.com/chat/completions";

    @Override
    public Result<String> generateHealthReport() {
        Integer userId = LocalThreadHolder.getUserId();

        // 1. 获取用户信息
        Result<UserVO> userRes = userService.getById(userId);
        String username = (userRes.getData() != null) ? userRes.getData().getUsername() : "用户";

        // 2. 获取健康目标
        Result<Map<String, List<HealthGoal>>> goalRes = healthGoalService.getMyGoals();
        String goalStr = "尚未设定明确的健康目标";
        if (goalRes.getData() != null && !goalRes.getData().isEmpty()) {
            goalStr = goalRes.getData().values().stream()
                    .flatMap(List::stream)
                    .map(g -> {
                        String typeName = (g.getType() != null && g.getType() == 1) ? "减肥" : "增肌";
                        String statusName = (g.getStatus() != null && g.getStatus() == 1) ? "已完成" : "进行中";
                        return String.format("目标:%s(当前值:%s, 目标值:%s)[%s]",
                                typeName, g.getCurrentValue(), g.getTargetValue(), statusName);
                    })
                    .collect(Collectors.joining("；"));
        }

        // 3. 聚合健康记录（彻底修复 LocalDateTime 和 Double 转换问题）
        HealthRecordQueryDto hrDto = new HealthRecordQueryDto();
        hrDto.setUserId(userId);
        hrDto.setCurrent(1);
        hrDto.setSize(150); // 确保获取足够的指标行
        Result<List<HealthRecordVO>> hrRes = healthRecordService.listUser(hrDto);

        String healthDataStr = "暂无详细健康数据记录";
        String currentBodyStatus = "尚未录入体测数据";

        if (hrRes.getData() != null && !hrRes.getData().isEmpty()) {
            Map<Date, AiHealthDataVO> aggregationMap = new LinkedHashMap<>();

            for (HealthRecordVO vo : hrRes.getData()) {
                if (vo.getCreateTime() == null) continue;

                // 将 LocalDateTime 转换为 java.util.Date
                Date timeKey = Date.from(vo.getCreateTime().atZone(ZoneId.systemDefault()).toInstant());

                AiHealthDataVO data = aggregationMap.get(timeKey);
                if (data == null) {
                    data = new AiHealthDataVO();
                    data.setCreateTime(timeKey); // 此时 timeKey 是 Date 类型，编译通过
                    aggregationMap.put(timeKey, data);
                }

                String modelName = vo.getHealthModelName();
                Double val = vo.getValue();

                if (val != null) {
                    // 【修复核心 2】：根据 AiHealthDataVO 的字段类型进行转换
                    if ("体重".equals(modelName)) data.setWeight(val);
                    else if ("心率".equals(modelName)) data.setHeartRate(val.intValue()); // Double 转 Integer
                    else if ("睡眠时长".equals(modelName)) data.setSleepDuration(val);
                    else if ("身体质量指数 (BMI)".equals(modelName)) data.setBmi(val);
                    else if ("血压【低压】".equals(modelName)) data.setLowPressure(val.intValue()); // Double 转 Integer
                    else if ("血压【高压】".equals(modelName)) data.setHighPressure(val.intValue()); // Double 转 Integer
                    else if ("血糖".equals(modelName)) data.setBloodSugar(val);
                    else if ("血氧饱和度".equals(modelName)) data.setBloodOxygen(val);
                    else if ("体脂率".equals(modelName)) data.setBodyFatRate(val);
                    else if ("午休时长".equals(modelName)) data.setNapDuration(val);
                }
            }

            List<AiHealthDataVO> aiList = new ArrayList<>(aggregationMap.values());
            if (!aiList.isEmpty()) {
                AiHealthDataVO latest = aiList.get(0);
                currentBodyStatus = String.format("当前体重:%s kg, BMI:%s",
                        latest.getWeight() != null ? latest.getWeight() : "未录入",
                        latest.getBmi() != null ? latest.getBmi() : "未计算");

                healthDataStr = aiList.stream()
                        .map(r -> String.format(
                                "【时间:%s】体重:%skg, 心率:%s, 血压:%s/%s, 血糖:%s, 体脂:%s%%",
                                r.getCreateTime(), r.getWeight(), r.getHeartRate(),
                                r.getHighPressure(), r.getLowPressure(), r.getBloodSugar(), r.getBodyFatRate()
                        ))
                        .collect(Collectors.joining(";\n"));
            }
        }

        // 4. 获取饮食记录
        DietHistoryQueryDto dietDto = new DietHistoryQueryDto();
        dietDto.setUserId(userId);
        dietDto.setCurrent(1);
        dietDto.setSize(10);
        Result<List<DietHistoryVO>> dietRes = dietService.listUser(dietDto);
        String dietDataStr = "近期无饮食记录";
        if (dietRes.getData() != null && !dietRes.getData().isEmpty()) {
            dietDataStr = dietRes.getData().stream()
                    .map(d -> d.getRecipeName() + "(" + d.getValue() + "g)")
                    .collect(Collectors.joining(", "));
        }

        // 5. 获取系统食谱名
        List<String> recipes = recipeService.listPublicNames();

        // 6. 构造 Prompt
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
            return aiReply != null ? ApiResult.success("报告生成成功", aiReply) : ApiResult.error("AI回复为空");
        } catch (Exception e) {
            return ApiResult.error("AI服务调用失败：" + e.getMessage());
        }
    }
}