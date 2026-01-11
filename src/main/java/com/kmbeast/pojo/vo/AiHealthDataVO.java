package com.kmbeast.pojo.vo;

import java.util.Date;

/**
 * 专供 AI 分析使用的全新 VO，包含 10 项健康指标
 */
public class AiHealthDataVO {
    private Double weight;            // 体重
    private Integer heartRate;        // 心率
    private Double sleepDuration;     // 睡眠时长
    private Double bmi;               // 身体质量指数
    private Integer lowPressure;      // 血压【低压】
    private Integer highPressure;     // 血压【高压】
    private Double bloodSugar;        // 血糖
    private Double bloodOxygen;       // 血氧饱和度
    private Double bodyFatRate;       // 体脂率
    private Double napDuration;       // 午休时长
    private Date createTime;          // 记录时间

    // --- 手动创建所有 Getter 和 Setter (为了不覆盖老代码，咱们手动补全) ---

    public Double getWeight() { return weight; }
    public void setWeight(Double weight) { this.weight = weight; }

    public Integer getHeartRate() { return heartRate; }
    public void setHeartRate(Integer heartRate) { this.heartRate = heartRate; }

    public Double getSleepDuration() { return sleepDuration; }
    public void setSleepDuration(Double sleepDuration) { this.sleepDuration = sleepDuration; }

    public Double getBmi() { return bmi; }
    public void setBmi(Double bmi) { this.bmi = bmi; }

    public Integer getLowPressure() { return lowPressure; }
    public void setLowPressure(Integer lowPressure) { this.lowPressure = lowPressure; }

    public Integer getHighPressure() { return highPressure; }
    public void setHighPressure(Integer highPressure) { this.highPressure = highPressure; }

    public Double getBloodSugar() { return bloodSugar; }
    public void setBloodSugar(Double bloodSugar) { this.bloodSugar = bloodSugar; }

    public Double getBloodOxygen() { return bloodOxygen; }
    public void setBloodOxygen(Double bloodOxygen) { this.bloodOxygen = bloodOxygen; }

    public Double getBodyFatRate() { return bodyFatRate; }
    public void setBodyFatRate(Double bodyFatRate) { this.bodyFatRate = bodyFatRate; }

    public Double getNapDuration() { return napDuration; }
    public void setNapDuration(Double napDuration) { this.napDuration = napDuration; }

    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
}