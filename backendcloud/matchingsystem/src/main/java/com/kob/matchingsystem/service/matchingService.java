package com.kob.matchingsystem.service;

public interface matchingService {
    String addPlayer(Integer userId, Integer rating);

    String removePlayer(Integer userId);
}
