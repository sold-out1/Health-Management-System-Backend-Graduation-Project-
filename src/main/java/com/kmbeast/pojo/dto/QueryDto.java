package com.kmbeast.pojo.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 查询参数接收实体类基类，含有五项基础参数，实际使用时可以根据实际情况进行拓展
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class QueryDto {
    private Integer id; // ID
    private List<Integer> ids; // ID列表
    private Integer current; // 当前页
    private Integer size; //页面数据大小
    private LocalDateTime startTime; //开始时间
    private LocalDateTime endTime; //结束时间
}

