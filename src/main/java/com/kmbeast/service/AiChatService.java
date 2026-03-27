package com.kmbeast.service;

public interface AiChatService {
    /**
     * 与 AI 助手对话
     * @param userContent 用户输入的内容
     * @return AI 的回复文本
     * @throws Exception 抛出异常由 Controller 统一捕获处理
     */
    String chat(String userContent) throws Exception;
}