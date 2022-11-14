package com.kob.botrunningsystem.service.Impl;

import com.kob.botrunningsystem.service.BotRunningService;
import com.kob.botrunningsystem.service.Impl.utils.BotPool;
import org.springframework.stereotype.Service;

@Service
public class BotRunningServiceImpl implements BotRunningService {

    public static BotPool botPool=new BotPool();
    @Override
    public String addBot(Integer userId, String botCode, String input) {

        System.out.println("add "+userId+" "+botCode);
        botPool.addBot(userId, botCode, input);
        return "add bot success";
    }
}