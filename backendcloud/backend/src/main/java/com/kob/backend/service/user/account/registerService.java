package com.kob.backend.service.user.account;

import java.util.Map;

public interface registerService {
    public Map<String,String> register(String username,String password,String confirm_password);
}
