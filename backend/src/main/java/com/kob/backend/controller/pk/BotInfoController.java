package com.kob.backend.controller.pk;



import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

import java.util.Map;

@RestController
@RequestMapping("/")
public class BotInfoController {


    @RequestMapping("pk/getinfo/")
    public Map<String,String> get_info(){
        Map<String,String> bot =new HashMap<>();
        bot.put("name","fsw");
        bot.put("number","99999");
        return bot;
    }

}
