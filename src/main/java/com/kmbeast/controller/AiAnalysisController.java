package com.kmbeast.controller;

import com.kmbeast.pojo.api.ApiResult;
import com.kmbeast.pojo.api.Result;
import com.kmbeast.service.AiAnalysisService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/ai/analysis")
public class AiAnalysisController {

    @Resource
    private AiAnalysisService aiAnalysisService;

    /**
     * 生成健康分析报告
     * 自动聚合用户最近的体重、饮食、目标数据
     */
    @GetMapping("/generateReport")
    public Result<String> generateReport() {
        return aiAnalysisService.generateHealthReport();
    }
}