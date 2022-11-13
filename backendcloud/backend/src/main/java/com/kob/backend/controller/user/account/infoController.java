package com.kob.backend.controller.user.account;


import com.kob.backend.service.user.account.infoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class infoController {

    @Autowired
    private infoService infoService;

    @GetMapping("/user/account/getinfo/")
    public Map<String,String> getInfo(){

        return infoService.getInfo();
    }
}
