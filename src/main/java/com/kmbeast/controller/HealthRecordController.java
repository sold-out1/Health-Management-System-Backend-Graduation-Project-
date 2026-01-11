package com.kmbeast.controller;

import com.kmbeast.aop.Pager;
import com.kmbeast.pojo.api.Result;
import com.kmbeast.pojo.dto.HealthRecordQueryDto;
import com.kmbeast.pojo.entity.HealthRecord;
import com.kmbeast.pojo.vo.HealthRecordLineChartVO;
import com.kmbeast.pojo.vo.HealthRecordVO;
import com.kmbeast.service.HealthRecordService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 健康记录控制器
 */
@RestController
@RequestMapping("/health-record")
public class HealthRecordController {

    @Resource
    private HealthRecordService healthRecordService;

    /**
     * 批量新增
     *
     * @param healthRecordList 实体数据
     * @return Result<String> 响应结果
     */
    @PostMapping(value = "/batchSave")
    @ResponseBody
    public Result<String> batchSave(@RequestBody List<HealthRecord> healthRecordList) {
        return healthRecordService.batchSave(healthRecordList);
    }

    /**
     * 删除
     *
     * @param id 主键ID
     * @return Result<String> 响应结果
     */
    @DeleteMapping(value = "/{id}")
    @ResponseBody
    public Result<String> delete(@PathVariable Integer id) {
        return healthRecordService.delete(id);
    }

    /**
     * 用户查询自己的健康记录列表
     *
     * @param healthRecordQueryDto 查询参数
     * @return Result<List < HealthRecordVO>> 响应结果
     */
    @Pager
    @PostMapping(value = "/listUser")
    @ResponseBody
    public Result<List<HealthRecordVO>> listUser(@RequestBody HealthRecordQueryDto healthRecordQueryDto) {
        return healthRecordService.listUser(healthRecordQueryDto);
    }

    /**
     * 健康数据可视化（折线图）
     *
     * @param healthRecordQueryDto 查询参数
     * @return Result<List < HealthRecordLineChartVO>> 响应结果
     */
    @PostMapping(value = "/listLineChart")
    @ResponseBody
    public Result<List<HealthRecordLineChartVO>> listLineChart(@RequestBody HealthRecordQueryDto healthRecordQueryDto) {
        return healthRecordService.listLineChart(healthRecordQueryDto);
    }

    /**
     * 列表查询
     *
     * @param healthRecordQueryDto 查询参数
     * @return Result<List < HealthRecordVO>> 响应结果
     */
    @Pager
    @PostMapping(value = "/list")
    @ResponseBody
    public Result<List<HealthRecordVO>> list(@RequestBody HealthRecordQueryDto healthRecordQueryDto) {
        return healthRecordService.list(healthRecordQueryDto);
    }

}