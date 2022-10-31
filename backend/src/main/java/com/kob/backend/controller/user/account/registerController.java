package com.kob.backend.controller.user.account;

import com.kob.backend.service.user.account.registerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class registerController {

    @Autowired
    private registerService registerService;

    @PostMapping("/user/account/register/")
    public Map<String,String> register(@RequestParam Map<String,String> map){
        String username=map.get("username");
        String password=map.get("password");
        String confirm_password=map.get("confirm_password");
        return registerService.register(username,password,confirm_password);
    }

}
