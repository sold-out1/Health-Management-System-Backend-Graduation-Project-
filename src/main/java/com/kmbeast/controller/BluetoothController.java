package com.kmbeast.controller;

import com.kmbeast.pojo.api.Result;
import com.kmbeast.pojo.dto.BluetoothSyncDto;
import com.kmbeast.service.BluetoothService;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;

@RestController
@RequestMapping("/bluetooth")
public class BluetoothController {

    @Resource
    private BluetoothService bluetoothService; // [cite: 889]

    @PostMapping("/sync")
    public Result<String> syncData(@RequestBody BluetoothSyncDto dto) {
        // 接收前端蓝牙同步请求并处理 [cite: 897-900]
        return bluetoothService.processSyncData(dto);
    }
}