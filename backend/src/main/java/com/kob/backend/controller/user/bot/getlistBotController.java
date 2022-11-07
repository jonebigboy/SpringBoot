package com.kob.backend.controller.user.bot;

import com.kob.backend.pojo.Bot;
import com.kob.backend.service.user.bot.getlistBotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class getlistBotController {

    @Autowired
    private getlistBotService getlistBotService;

    @GetMapping("/user/bot/getlist/")
    public List<Bot> getlist(){
        return getlistBotService.getlistBot();
    }
}
