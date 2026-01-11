package com.kmbeast.mapper;

import com.kmbeast.pojo.entity.HealthRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 蓝牙同步数据持久化接口
 */
@Mapper
public interface BluetoothMapper {

    /**
     * 批量插入健康记录
     * @param recordList 转换后的健康记录列表
     * @return 插入成功的行数
     */
    int insertBatch(@Param("list") List<HealthRecord> recordList);
}