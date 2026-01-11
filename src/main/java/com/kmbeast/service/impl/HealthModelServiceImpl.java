package com.kmbeast.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kmbeast.context.LocalThreadHolder;
import com.kmbeast.mapper.HealthModelMapper;
import com.kmbeast.mapper.HealthRecordMapper;
import com.kmbeast.pojo.api.ApiResult;
import com.kmbeast.pojo.api.Result;
import com.kmbeast.pojo.dto.BMIDto;
import com.kmbeast.pojo.dto.HealthModelQueryDto;
import com.kmbeast.pojo.em.IsGlobalEnum;
import com.kmbeast.pojo.em.RoleEnum;
import com.kmbeast.pojo.entity.HealthModel;
import com.kmbeast.pojo.entity.HealthRecord;
import com.kmbeast.pojo.vo.BMIResultVO;
import com.kmbeast.pojo.vo.HealthModelCountVO;
import com.kmbeast.pojo.vo.HealthModelToolTipVO;
import com.kmbeast.service.HealthModelService;
import com.kmbeast.utils.AssertUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * 健康模型业务逻辑接口实现类
 */
@Service
public class HealthModelServiceImpl extends ServiceImpl<HealthModelMapper, HealthModel> implements HealthModelService {

    @Resource
    private HealthRecordMapper healthRecordMapper;

    /**
     * 新增
     *
     * @param healthModel 实体数据
     * @return Result<String> 响应结果
     */
    @Override
    public Result<String> saveEntity(HealthModel healthModel) {
        // 参数校验
        validParam(healthModel);
        // 设置用户权限隔离 - 后期适配：课程遗漏的点
        setUserInfo(healthModel);
        healthModel.setCreateTime(LocalDateTime.now()); // 设置上当前新增的时间
        // 阈值处理
        validNormalValue(healthModel.getNormalValue());
        // 模型权限处理
        validUpdateAuth(healthModel);
        // 数据新增
        save(healthModel);
        return ApiResult.success("健康模型新增成功");
    }

    private void setUserInfo(HealthModel healthModel) {
        // 如果是管理员新增，则为全局模型
        if (Objects.equals(LocalThreadHolder.getRoleId(),RoleEnum.ADMIN.getRole())){
            healthModel.setIsGlobal(IsGlobalEnum.PUBLIC.getStatus());
            return;
        }
        // 设置为私有模型
        healthModel.setIsGlobal(IsGlobalEnum.PRIVATE.getStatus());
        // 设置上用户ID
        healthModel.setUserId(LocalThreadHolder.getUserId());
    }

    /**
     * 检验修改时权限 - 如果是公共模型，需要检验身份信息
     *
     * @param healthModel 健康模型
     */
    private void validUpdateAuth(HealthModel healthModel) {
        // 如果是公共模型，只有管理员能够修改
        if (Objects.equals(healthModel.getIsGlobal(), IsGlobalEnum.PUBLIC.getStatus())) {
            AssertUtils.isTrue(
                    Objects.equals(LocalThreadHolder.getRoleId(), RoleEnum.ADMIN.getRole()),
                    "无操作权限"
            );
        }
    }

    /**
     * 处理模型权限
     *
     * @param healthModel 实体
     */
    private void modelValidAuth(HealthModel healthModel) {
        // 看是谁人创建的模型（角色：LocalThreadHolder.getRole()） -> 公有模型？私人模型？
        healthModel.setIsGlobal(Objects.equals(LocalThreadHolder.getRoleId(), RoleEnum.ADMIN.getRole())
                ? IsGlobalEnum.PUBLIC.getStatus()
                : IsGlobalEnum.PRIVATE.getStatus());
        // 设置上用户ID
        healthModel.setUserId(LocalThreadHolder.getUserId()); // 设置上用户ID
    }

    /**
     * 模型的阈值处理
     *
     * @param normalValue 正常阈值
     */
    private void validNormalValue(String normalValue) {
        // 做兼容，如果是出现中文逗号，兼容成英文逗号：“，” -> “,”
        if (normalValue.contains("，")) {
            normalValue = normalValue.replace("，", ",");
        }
        // 要求格式必须是：xxx（最小值）,xxx（最大值）
        AssertUtils.isTrue(normalValue.contains(","), "模型阈值非法，请重新输入");
    }

    /**
     * 参数校验
     *
     * @param healthModel 实体数据
     */
    private void validParam(HealthModel healthModel) {
        AssertUtils.notNull(healthModel, "实体不能为空");
        AssertUtils.hasText(healthModel.getName(), "模型名不能为空");
        AssertUtils.hasText(healthModel.getDetail(), "模型介绍不能为空");
        AssertUtils.hasText(healthModel.getUnit(), "模型单位不能为空");
        AssertUtils.hasText(healthModel.getSymbol(), "模型符号不能为空");
        AssertUtils.hasText(healthModel.getNormalValue(), "模型正常阈值不能为空");
        AssertUtils.isTrue(healthModel.getName().length() < 100, "模型名称请控制在100字以内");
        AssertUtils.isTrue(healthModel.getDetail().length() < 200, "模型介绍请控制在200字以内");
    }

    /**
     * 修改
     *
     * @param healthModel 实体数据
     * @return Result<String> 响应结果
     */
    @Override
    public Result<String> updateEntity(HealthModel healthModel) {
        // 参数校验
        validParam(healthModel);
        // 阈值处理
        validNormalValue(healthModel.getNormalValue());
        // 模型权限处理
        modelValidAuth(healthModel);
        // 数据修改
        updateById(healthModel);
        return ApiResult.success("健康模型修改成功");
    }

    /**
     * 删除
     *
     * @param id 主键ID
     * @return Result<String>
     */
    @Override
    public Result<String> delete(Integer id) {
        HealthModel healthModel = getById(id);
        // 为了确保功能的完整使用，公共模型的删除，暂时不支持了。
        AssertUtils.isFalse(Objects.equals(healthModel.getIsGlobal(), IsGlobalEnum.PUBLIC.getStatus()), "删除公共模型可能会影响系统其他功能板块，暂时不支持公共模型的删除，祝您生活愉快");
        // 私人模型删除权限
        Assert.isTrue(Objects.equals(LocalThreadHolder.getUserId(), healthModel.getUserId()), "无删除权限");
        removeById(id);
        return ApiResult.success("删除成功");
    }

    /**
     * 列表查询
     *
     * @param healthModelQueryDto 查询参数
     * @return Result<List < healthModelList>> 响应结果
     */
    @Override
    public Result<List<HealthModel>> list(HealthModelQueryDto healthModelQueryDto) {
        AssertUtils.notNull(healthModelQueryDto.getCurrent(), "当前页参数不能为空");
        AssertUtils.notNull(healthModelQueryDto.getSize(), "页面大小参数不能为空");
        // 如果是私人模型，并且设置上去对应的用户ID即可
        if (healthModelQueryDto.getIsGlobal() != null && healthModelQueryDto.getIsGlobal().equals(IsGlobalEnum.PRIVATE.getStatus())) {
            // 如果当前操作是管理员的操作，不需要处理上用户ID
            if (!Objects.equals(LocalThreadHolder.getRoleId(), RoleEnum.ADMIN.getRole())) {
                healthModelQueryDto.setUserId(LocalThreadHolder.getUserId());
            }
        }
        List<HealthModel> healthModelList = this.baseMapper.list(healthModelQueryDto);// 查列表
        Integer count = this.baseMapper.listPageCount(healthModelQueryDto);// 查总页数
        return ApiResult.success(healthModelList, count);
    }

    /**
     * 查询所有公共模型，并构造选择器
     *
     * @return Result<List < OptionsVO>> 响应结果
     */
    @Override
    public Result<List<HealthModelToolTipVO>> options() {
        List<HealthModelToolTipVO> optionsVOS = this.baseMapper.options(
                IsGlobalEnum.PUBLIC.getStatus() ? 1 : 0,
                null
        );
        return ApiResult.success(optionsVOS);
    }

    /**
     * 查询所有公共模型及用户自己配置的私有模型
     *
     * @return Result<List < HealthModelToolTipVO>> 响应结果
     */
    @Override
    public Result<List<HealthModelToolTipVO>> optionsUser() {
        // 查的是公共模型
        List<HealthModelToolTipVO> optionsVOS = this.baseMapper.options(
                IsGlobalEnum.PUBLIC.getStatus() ? 1 : 0,
                null
        );
        textPlus(optionsVOS, "（公共模型）");
        // 查询用户自己配置的私人模型
        List<HealthModelToolTipVO> optionsUserVOS = this.baseMapper.options(
                IsGlobalEnum.PRIVATE.getStatus() ? 1 : 0,
                LocalThreadHolder.getUserId()
        );
        textPlus(optionsUserVOS, "（私人模型）");
        optionsVOS.addAll(optionsUserVOS);
        return ApiResult.success(optionsVOS);
    }

    private void textPlus(List<HealthModelToolTipVO> optionsUserVOS, String text) {
        for (HealthModelToolTipVO optionsUserVO : optionsUserVOS) {
            optionsUserVO.setLabel(optionsUserVO.getLabel() + text);
        }
    }

    /**
     * 测算BMI值含量
     *
     * @return Result<BMIResultVO> 响应结果
     */
    @Override
    public Result<BMIResultVO> computeBMI(BMIDto bmiDto) {
        // 参数校验
        AssertUtils.notNull(bmiDto, "参数不能为空");
        AssertUtils.notNull(bmiDto.getHeight(), "身高不能为空");
        AssertUtils.notNull(bmiDto.getWeight(), "体重不能为空");
        AssertUtils.isTrue(bmiDto.getHeight() > 0, "身高必须大于0");
        AssertUtils.isTrue(bmiDto.getWeight() > 0, "体重必须大于0");

        // 身高单位转换：厘米 → 米
        double heightInMeter = bmiDto.getHeight() / 100.0;

        // 正确公式：BMI = 体重(kg) ÷ (身高(m) × 身高(m))
        double bmiValue = bmiDto.getWeight() / (heightInMeter * heightInMeter);

        // 保留两位小数
        bmiValue = Math.round(bmiValue * 100) / 100.0;

        // 构建返回结果
        BMIResultVO bmiResultVO = new BMIResultVO();
        bmiResultVO.setResult(bmiValue);

        // 可补充BMI分类判断
        if (bmiValue < 18.5) {
            bmiResultVO.setInfo("偏瘦");
        } else if (bmiValue < 24) {
            bmiResultVO.setInfo("正常");
        } else if (bmiValue < 28) {
            bmiResultVO.setInfo("超重");
        } else {
            bmiResultVO.setInfo("肥胖");
        }

        // 将BMI测算结果存储至数据库
        saveBMI(bmiResultVO);

        return ApiResult.success(bmiResultVO);
    }

    private void saveBMI(BMIResultVO bmiResultVO) {
        // 1. 先找到BMI公共模型，有可能有，也有可能没有
        HealthModelQueryDto healthModelQueryDto = new HealthModelQueryDto();
        healthModelQueryDto.setName("BMI"); // BMI模型
        healthModelQueryDto.setIsGlobal(IsGlobalEnum.PUBLIC.getStatus()); // 公共模型
        List<HealthModel> healthModelList = this.baseMapper.list(healthModelQueryDto);
        // 2. 存在BMI模型且为公共模型时才需要记录BMI健康值
        if (!healthModelList.isEmpty()) {
            HealthModel healthModel = healthModelList.get(0);
            // 记录健康值
            HealthRecord healthRecord = new HealthRecord();
            healthRecord.setHealthModelId(healthModel.getId()); // 设置模型：BMI模型
            healthRecord.setUserId(LocalThreadHolder.getUserId()); // 设置操作者（测算者）用户ID
            healthRecord.setValue(bmiResultVO.getResult()); // 设置BMI测算结果
            healthRecord.setCreateTime(LocalDateTime.now()); // 设置当前时间
            healthRecordMapper.insert(healthRecord);
        }
    }

    /**
     * 模型统计
     *
     * @return Result<HealthModelCountVO> 响应结果
     */
    @Override
    public Result<HealthModelCountVO> modelCount() {
        HealthModelCountVO healthModelCountVO = this.baseMapper.modelCount(
                IsGlobalEnum.PRIVATE.getStatus() ? 0 : 1,
                LocalThreadHolder.getUserId()
        );
        return ApiResult.success(healthModelCountVO);
    }
}