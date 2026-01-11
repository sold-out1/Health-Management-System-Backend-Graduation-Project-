package com.kmbeast.service;

import com.kmbeast.pojo.api.Result;
import com.kmbeast.pojo.dto.BluetoothSyncDto;

/**
 * 蓝牙同步业务逻辑接口 [cite: 1096]
 */
public interface BluetoothService {
    Result<String> processSyncData(BluetoothSyncDto dto);
}