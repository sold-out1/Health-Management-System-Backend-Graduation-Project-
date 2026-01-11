package com.kmbeast.service;

import com.kmbeast.pojo.api.Result;

public interface AiAnalysisService {
    /**
     * 生成深度健康分析报告
     */
    Result<String> generateHealthReport();
}