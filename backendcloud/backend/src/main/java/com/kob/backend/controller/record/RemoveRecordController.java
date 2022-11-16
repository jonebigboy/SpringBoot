package com.kob.backend.controller.record;


import com.alibaba.fastjson2.JSONObject;
import com.kob.backend.service.record.RemoveRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class RemoveRecordController {

    @Autowired
    private RemoveRecordService removeRecordService;


    @PostMapping("/record/remove/")
    private Map<String,String> removeRecord(@RequestParam Map<String,String>data){
        Integer recordId=Integer.parseInt(data.get("record_id"));
        return removeRecordService.removeRecord(recordId);
    }

}
