package com.kob.backend.service.impl.record;


import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kob.backend.mapper.RecordMapper;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.Record;
import com.kob.backend.pojo.User;
import com.kob.backend.service.record.GetRecordListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class GetRecordListServiceImpl implements GetRecordListService {


    @Autowired
    private RecordMapper recordMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public JSONObject getRecordList(Integer page) {
        System.out.println(page);
        IPage<Record> recordIPage=new Page<>(page,10);
        QueryWrapper<Record> queryWrapper=new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        List<Record> records= recordMapper.selectPage(recordIPage,queryWrapper).getRecords();//获取一页中所有的record
        JSONObject resp=new JSONObject();
        List<JSONObject> items=new ArrayList<>();
        for(Record record:records){
            JSONObject item=new JSONObject();
            User userA=userMapper.selectById(record.getAId());
            User userB =userMapper.selectById(record.getBId());
            if("A".equals(record.getLoser())){
                item.put("result","B胜");
            }else if("B".equals(record.getLoser())){
                item.put("result","A胜");
            }else{
                item.put("result","all");
            }
            item.put("a_username",userA.getUsername());
            item.put("a_photo",userA.getPhoto());
            item.put("b_username",userB.getUsername());
            item.put("b_photo",userB.getPhoto());
            item.put("record",record);
            items.add(item);
        }
        resp.put("record",items);
        resp.put("records_number",recordMapper.selectCount(null));
        return resp;
    }
}
