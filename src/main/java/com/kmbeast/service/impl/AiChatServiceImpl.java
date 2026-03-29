package com.kmbeast.service.impl;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.kmbeast.service.AiChatService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AiChatServiceImpl implements AiChatService {


    private final String API_KEY = "sk-b89dde3b88fa4812af560421cffe8440";
    private final String AI_URL = "https://api.deepseek.com/chat/completions";

    @Override
    public String chat(String userContent) throws Exception {
        Map<String, Object> body = new HashMap<>();
        body.put("model", "deepseek-chat");

        List<Object> messages = new ArrayList<>();

        // 提示词
        messages.add(JSONUtil.createObj()
                .set("role", "system")
                .set("content", "你是一个亲切温柔的健康饮食助手，请为用户提供温暖,科学和精准的建议。给用户的回答不要有任何markdown格式，就直接一段话就可以。"));

        // 用户输入内容
        messages.add(JSONUtil.createObj()
                .set("role", "user")
                .set("content", userContent));

        body.put("messages", messages);

        // 发送 HTTP 请求
        String response = HttpUtil.createPost(AI_URL)
                .header("Authorization", "Bearer " + API_KEY)
                .header("Content-Type", "application/json")
                .body(JSONUtil.toJsonStr(body))
                .timeout(60000) // 60秒超时
                .execute()
                .body();

        // 解析并返回结果
        JSONObject jsonObject = JSONUtil.parseObj(response);
        return jsonObject.getByPath("choices[0].message.content", String.class);
    }
}