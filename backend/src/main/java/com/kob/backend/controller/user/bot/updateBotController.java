package com.kob.backend.controller.user.bot;


import com.kob.backend.service.user.bot.updateBotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class updateBotController {

    @Autowired
    private updateBotService updateBotService;

    @PostMapping("/user/bot/update")
    public Map<String,String> updateBot(@RequestParam Map<String,String> data){
        return updateBotService.updateBot(data);
    }

}
