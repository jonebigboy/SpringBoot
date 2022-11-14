package com.kob.matchingsystem.service;

public interface matchingService {
    String addPlayer(Integer userId, Integer rating,Integer botId);

    String removePlayer(Integer userId);
}
