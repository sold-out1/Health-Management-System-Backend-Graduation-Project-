package com.kmbeast.controller;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.kmbeast.pojo.api.ApiResult;
import com.kmbeast.pojo.api.Result;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/ai")
public class AiChatController {

    // 1. 修改這裡：填入你申請的 API Key
    private final String API_KEY = "sk-b89dde3b88fa4812af560421cffe8440";

    // 2. 默認使用 DeepSeek 地址，如果你用阿里或其他，請更換此 URL
    private final String AI_URL = "https://api.deepseek.com/chat/completions";

    @PostMapping("/chat")
    public Result<String> chat(@RequestBody Map<String, String> params) {
        String userContent = params.get("content");

        try {
            // 構建請求體
            Map<String, Object> body = new HashMap<>();
            body.put("model", "deepseek-chat");

            // 使用 JSONUtil 構建消息列表，確保兼容 Java 8
            List<Object> messages = new ArrayList<>();

            // 設定 AI 人設
            messages.add(JSONUtil.createObj()
                    .set("role", "system")
                    .set("content", "你是一个亲切温柔的健康饮食助手，请为用户提供温暖,科学和精准的建议。"));

            // 用戶輸入的內容
            messages.add(JSONUtil.createObj()
                    .set("role", "user")
                    .set("content", userContent));

            body.put("messages", messages);

            // 發送 POST 請求
            String response = HttpUtil.createPost(AI_URL)
                    .header("Authorization", "Bearer " + API_KEY)
                    .header("Content-Type", "application/json")
                    .body(JSONUtil.toJsonStr(body))
                    .timeout(60000) // 設置 60 秒超時，防止 AI 思考太久導致中斷
                    .execute()
                    .body();

            // 解析響應內容
            JSONObject jsonObject = JSONUtil.parseObj(response);
            String aiReply = jsonObject.getByPath("choices[0].message.content", String.class);


            return ApiResult.success("查詢成功", aiReply);

        } catch (Exception e) {
            e.printStackTrace();
            return ApiResult.error("AI 助手暫時走神了：" + e.getMessage());
        }
    }
}