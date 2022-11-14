package com.kob.matchingsystem.service.Impl.utils;


import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class matchPool extends Thread {

    private static List<Player> players= new ArrayList<>();
    private ReentrantLock lock=new ReentrantLock();
    private static RestTemplate restTemplate;
    private final String sendUrl="http://127.0.0.1:3000/pk/start/game/";

    @Autowired
    private void setRestTemplate(RestTemplate restTemplate){
        matchPool.restTemplate =restTemplate;
    }

    public void addPlayer(Integer userId,Integer rating ,Integer botId){
        lock.lock();
        try{
            players.add(new Player(userId,rating,0,botId));

        }finally {
            lock.unlock();
        }
    }

    public void removePlayer(Integer userId){
        lock.lock();
        try{
            Player remove_play=null;
            for(Player play:players){
                if(play.getId()==userId){
                    remove_play=play;
                    break;
                }
            }
            players.remove(remove_play);

        }finally {
            lock.unlock();
        }
    }

    private void increate_time(){
        for (Player player:players){
            player.setWaitingTime(player.getWaitingTime()+1);
        }
    }
    private boolean check_match(Player a,Player b){
        int ratingDelta=Math.abs(a.getRating()-b.getRating());
        int time=Math.min(a.getWaitingTime(),b.getWaitingTime());
        return ratingDelta<=time*10;
    }
    private void sendResult(Player a,Player b){
        System.out.println("send Result "+a.getId().toString()+" "+b.getId().toString());
        MultiValueMap<String,String> data=new LinkedMultiValueMap<>();
        data.add("a_id",a.getId().toString());
        data.add("a_bot_id",a.getBotId().toString());
        data.add("b_id",b.getId().toString());
        data.add("b_bot_id",b.getBotId().toString());
        restTemplate.postForObject(sendUrl,data,String.class);
    }

    private void match_player(){
        System.out.println("开始匹配 "+players.toString());
        boolean[] use=new boolean[players.size()];
        for (int i=0;i<players.size();i++){
            if(use[i]) continue;//匹配过就跳过
            for (int j=i+1;j<players.size();j++){
                if(use[j]) continue;
                Player a=players.get(i);
                Player b=players.get(j);
                if(check_match(a,b)){
                    use[i]=use[j]=true;
                    sendResult(a,b);
                    break;
                }
            }
        }
        List<Player> newPlay= new ArrayList<>();
        for (int i=0;i<players.size();i++){
            if(!use[i]){
                newPlay.add(players.get(i));
            }
        }
        players=newPlay;
    }

    @Override
    public void run() {
        while(true){
            try {
                Thread.sleep(1000);
                lock.lock();
                try{
                    increate_time();
                    match_player();//一秒匹配当前状态下的一组对局
                }finally {
                    lock.unlock();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }

        }

    }
}
