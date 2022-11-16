package com.kob.backend.service.record;

import com.alibaba.fastjson2.JSONObject;

import java.util.Map;

public interface RemoveRecordService {
    Map<String,String> removeRecord(Integer recordId);
}
