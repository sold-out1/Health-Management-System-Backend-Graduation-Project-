package com.kmbeast.service.impl;

import com.kmbeast.mapper.BluetoothMapper;
import com.kmbeast.pojo.api.ApiResult;
import com.kmbeast.pojo.api.Result;
import com.kmbeast.pojo.dto.BluetoothSyncDto;
import com.kmbeast.pojo.entity.HealthRecord;
import com.kmbeast.service.BluetoothService;
import com.kmbeast.context.LocalThreadHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 蓝牙同步业务逻辑实现类
 */
@Service
public class BluetoothServiceImpl implements BluetoothService {

    @Resource
    private BluetoothMapper bluetoothMapper; // 注入刚才创建的 Mapper 接口

    @Override
    public Result<String> processSyncData(BluetoothSyncDto dto) {
        System.out.println(">>> 后端接收到的原始数据: " + dto.toString());
        // 1. 准备一个集合，用来存放拆分后的多条健康记录
        List<HealthRecord> records = new ArrayList<>();

        // 获取当前时间（若DTO中没有，则用系统当前时间）
        LocalDateTime now = dto.getCreateTime() != null ? dto.getCreateTime() : LocalDateTime.now();

        // 获取用户ID
        Integer userId = LocalThreadHolder.getUserId();

        // 2. 数据转换：将 DTO 中的字段逐一对应到数据库的健康模型 ID (healthModelId)
        addRecordIfNotNull(records, userId, 2, dto.getHeartRate(), now);          // 心率
        addRecordIfNotNull(records, userId, 4, dto.getWeight(), now);             // 体重
        addRecordIfNotNull(records, userId, 5, dto.getSleepDuration(), now);     // 睡眠时长
        addRecordIfNotNull(records, userId, 6, dto.getBmi(), now);               // BMI
        addRecordIfNotNull(records, userId, 9, dto.getLowerbloodPressure(), now); // 舒张压
        addRecordIfNotNull(records, userId, 10, dto.getHighbloodPressure(), now);  // 收缩压
        addRecordIfNotNull(records, userId, 11, dto.getBloodSugar(), now);        // 血糖
        addRecordIfNotNull(records, userId, 12, dto.getBloodOxygen(), now);       // 血氧
        addRecordIfNotNull(records, userId, 13, dto.getBodyFatPercentage(), now);  // 体脂率
        addRecordIfNotNull(records, userId, 14, dto.getNapDuration(), now);       // 午睡时长

        // 3. 判空校验
        if (records.isEmpty()) {
            return ApiResult.error("未检测到有效的健康同步数据");
        }

        // 4. 调用 Mapper 执行真正的 SQL 批量插入（XML中实现的逻辑）
        int rows = bluetoothMapper.insertBatch(records);

        if (rows > 0) {
            return ApiResult.success("同步成功，已自动导入 " + rows + " 项健康指标");
        } else {
            return ApiResult.error("数据导入失败，请重试");
        }
    }

    /**
     * 辅助转换方法：如果某个指标有值，就创建一条 HealthRecord 对象并放入列表
     *
     * @param list    存放结果的集合
     * @param userId  用户ID
     * @param modelId 数据库中对应的指标ID（如 1代表心率）
     * @param value   手环传过来的具体数值
     * @param time    记录时间
     */
    private void addRecordIfNotNull(List<HealthRecord> list, Integer userId, Integer modelId, Number value, LocalDateTime time) {
        if (value != null) {
            // 使用 Builder 模式创建实体对象（你提供的实体类中有 @Builder 注解）
            HealthRecord record = HealthRecord.builder()
                    .userId(userId)
                    .healthModelId(modelId)
                    .value(value.doubleValue()) // 统一转为 Double 存入数据库
                    .createTime(time)
                    .build();
            list.add(record);
        }
    }
}