package com.kob.botrunningsystem.service.Impl.utils;


import com.kob.botrunningsystem.utils.BotInterface;
import org.joor.Reflect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Component
public class Consumer extends Thread{

    private Bot bot;
    private static RestTemplate restTemplate;

    private final static String receiveBotMoveUrl = "http://127.0.0.1:3000/pk/receive/bot/move/";

    @Autowired
    private void setRestTemplate(RestTemplate restTemplate){
        Consumer.restTemplate =restTemplate;
    }

    public void startTimeOut(long time,Bot bot){
        this.bot=bot;
        this.start();//启动run函数

        try{
            this.join(time);//最多等到这个时候
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            this.interrupt();//中断线程
        }

    }

    private String addUid(String code,String uid){
        int k=code.indexOf(" implements com.kob.botrunningsystem.utils.BotInterface");
        return code.substring(0,k)+uid+code.substring(k,code.length());
    }

    @Override
    public void run() {
        UUID uuid= UUID.randomUUID();
        String uid = uuid.toString().substring(0, 8);

        BotInterface botInterface= Reflect.compile(
                "com.kob.botrunningsystem.utils.BotCode" + uid,
                addUid(bot.getBotCode(), uid)
        ).create().get();//获得一个接口
        Integer direction = botInterface.nextMove(bot.getInput());
        System.out.println("move-direction: " + bot.getUseId() + " " + direction);
        MultiValueMap<String,String> data=new LinkedMultiValueMap<>();
        data.add("user_id",bot.getUseId().toString());
        data.add("direction",direction.toString());
        restTemplate.postForObject(receiveBotMoveUrl,data,String.class);
    }
}
