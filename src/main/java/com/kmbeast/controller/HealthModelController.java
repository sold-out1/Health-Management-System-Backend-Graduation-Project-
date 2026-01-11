package com.kmbeast.controller;

import com.kmbeast.aop.Pager;
import com.kmbeast.pojo.api.Result;
import com.kmbeast.pojo.dto.BMIDto;
import com.kmbeast.pojo.dto.HealthModelQueryDto;
import com.kmbeast.pojo.entity.HealthModel;
import com.kmbeast.pojo.vo.BMIResultVO;
import com.kmbeast.pojo.vo.HealthModelCountVO;
import com.kmbeast.pojo.vo.HealthModelToolTipVO;
import com.kmbeast.service.HealthModelService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 健康模型控制器
 */
@RestController
@RequestMapping("/health-model")
public class HealthModelController {

    @Resource
    private HealthModelService healthModelService;

    /**
     * 新增
     *
     * @param healthModel 实体数据
     * @return Result<String> 响应结果
     */
    @PostMapping(value = "/save")
    @ResponseBody
    public Result<String> saveEntity(@RequestBody HealthModel healthModel) {
        return healthModelService.saveEntity(healthModel);
    }

    /**
     * 修改
     *
     * @param healthModel 实体数据
     * @return Result<String> 响应结果
     */
    @PutMapping(value = "/update")
    @ResponseBody
    public Result<String> updateEntity(@RequestBody HealthModel healthModel) {
        return healthModelService.updateEntity(healthModel);
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
        return healthModelService.delete(id);
    }

    /**
     * 列表查询
     *
     * @param healthModelQueryDto 查询参数
     * @return Result<List < HealthModel>> 响应结果
     */
    @Pager
    @PostMapping(value = "/list")
    @ResponseBody
    public Result<List<HealthModel>> list(@RequestBody HealthModelQueryDto healthModelQueryDto) {
        return healthModelService.list(healthModelQueryDto);
    }

    /**
     * 查询所有公共模型及用户自己配置的私有模型
     *
     * @return Result<List < HealthModelToolTipVO>> 响应结果
     */
    @GetMapping(value = "/optionsUser")
    @ResponseBody
    public Result<List<HealthModelToolTipVO>> optionsUser() {
        return healthModelService.optionsUser();
    }

    /**
     * 查询所有公共模型，并构造选择器
     *
     * @return Result<List < HealthModelToolTipVO>> 响应结果
     */
    @GetMapping(value = "/options")
    @ResponseBody
    public Result<List<HealthModelToolTipVO>> options() {
        return healthModelService.options();
    }

    /**
     * 模型统计
     *
     * @return Result<HealthModelCountVO> 响应结果
     */
    @GetMapping(value = "/modelCount")
    @ResponseBody
    public Result<HealthModelCountVO> modelCount() {
        return healthModelService.modelCount();
    }

    /**
     * 测算BMI值含量
     *
     * @return Result<BMIResultVO> 响应结果
     */
    @PostMapping(value = "/computeBMI")
    @ResponseBody
    public Result<BMIResultVO> computeBMI(@RequestBody BMIDto bmiDto) {
        return healthModelService.computeBMI(bmiDto);
    }

}