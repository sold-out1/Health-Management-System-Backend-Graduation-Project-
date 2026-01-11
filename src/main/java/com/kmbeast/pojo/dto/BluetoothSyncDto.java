package com.kmbeast.pojo.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 蓝牙手环同步数据接收类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BluetoothSyncDto {
    private String deviceName; // 手环名称
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime; // 数据创建时间
    private Integer heartRate; // 心率
    private Double weight;           //体重
    private Double  sleepDuration;   //睡眠时长（小时）
    @JsonProperty("bmi")
    @JsonAlias({"BMI", "Bmi"})
    private Double   bmi;             //身体质量指数
    private Double   lowerbloodPressure; //舒张压
    private Double   highbloodPressure;  //收缩压
    private Double  bloodSugar;        //血糖
    private Double  bloodOxygen;       //血氧
    private Double  bodyFatPercentage; //体脂率
    private Double napDuration;      //午睡时长（小时）
    private Integer userId;    // 用户ID
    public Double getBmi() {
        return this.bmi;
    }
}