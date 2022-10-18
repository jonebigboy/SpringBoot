package com.kob.backend.controller.pk;



import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping("/pk/")
public class BotInfoController {


    @RequestMapping("getinfo/")
    public List<String> get_info(){
        List<String> a=new LinkedList<>();
        a.add("1");
        a.add("2");
        a.add("3");
        return a;
    }
}
