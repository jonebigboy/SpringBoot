package com.kob.backend.service.impl.record;


import com.alibaba.fastjson2.JSONObject;
import com.kob.backend.mapper.RecordMapper;
import com.kob.backend.pojo.Record;
import com.kob.backend.service.record.RemoveRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class RemoveRecordServiceImpl implements RemoveRecordService {
    

    @Autowired
    private RecordMapper recordMapper;

    @Override
    public Map<String,String> removeRecord(Integer recordId) {
        System.out.println("recordId "+recordId);
        Map<String,String> resp=new HashMap<>();
        Record record=recordMapper.selectById(recordId);
        if(record==null){
            resp.put("message","不存在record");
            return resp;
        }
        recordMapper.deleteById(record);
        resp.put("message","success");

        return resp;
    }
}
