package com.kob.matchingsystem.service.Impl;

import com.kob.matchingsystem.service.Impl.utils.matchPool;
import com.kob.matchingsystem.service.matchingService;
import org.springframework.stereotype.Service;


@Service
public class matchingServiceImpl implements matchingService {

    public final static matchPool matchPool=new matchPool();
    @Override
    public String addPlayer(Integer userId, Integer rating,Integer botId) {
        System.out.println("add "+userId+" "+rating+" "+botId);
        matchPool.addPlayer(userId,rating,botId);
        return "success add player";
    }

    @Override
    public String removePlayer(Integer userId) {
        System.out.println("remove "+userId);
        matchPool.removePlayer(userId);
        return "success remove player";
    }
}
