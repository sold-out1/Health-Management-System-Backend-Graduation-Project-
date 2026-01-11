package com.kmbeast.controller;

import com.kmbeast.aop.Pager;
import com.kmbeast.context.LocalThreadHolder;
import com.kmbeast.pojo.api.Result;
import com.kmbeast.pojo.dto.FlowIndexQueryDto;
import com.kmbeast.pojo.entity.FlowIndex;
import com.kmbeast.service.FlowIndexService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 流量指标控制器
 */
@RestController
@RequestMapping("/flow-index")
public class FlowIndexController {

    @Resource
    private FlowIndexService flowIndexService;

    /**
     * 新增
     *
     * @param flowIndex 实体数据
     * @return Result<String> 响应结果
     */
    @PostMapping(value = "/save")
    @ResponseBody
    public Result<String> save(@RequestBody FlowIndex flowIndex) {
        return flowIndexService.saveEntity(flowIndex);
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
        return flowIndexService.delete(id);
    }

    /**
     * 点赞与收藏操作
     *
     * @param flowIndex 实体数据
     * @return Result<String> 响应结果
     */
    @PostMapping(value = "/operation")
    @ResponseBody
    public Result<String> operation(@RequestBody FlowIndex flowIndex) {
        return flowIndexService.operation(flowIndex);
    }

    /**
     * 查询用户名下产生的流量指标数据
     *
     * @param flowIndexQueryDto 查询参数
     * @return Result<List < FlowIndex>> 响应结果
     */
    @Pager
    @PostMapping(value = "/listUser")
    @ResponseBody
    public Result<List<FlowIndex>> listUser(@RequestBody FlowIndexQueryDto flowIndexQueryDto) {
        flowIndexQueryDto.setUserId(LocalThreadHolder.getUserId());
        return flowIndexService.listItem(flowIndexQueryDto);
    }

    /**
     * 列表查询
     *
     * @param flowIndexQueryDto 查询参数
     * @return Result<List < FlowIndex>> 响应结果
     */
    @Pager
    @PostMapping(value = "/list")
    @ResponseBody
    public Result<List<FlowIndex>> list(@RequestBody FlowIndexQueryDto flowIndexQueryDto) {
        return flowIndexService.listItem(flowIndexQueryDto);
    }

}