package com.kob.backend.consumer;

import com.alibaba.fastjson2.JSONObject;
import com.kob.backend.consumer.utils.GameMap;
import com.kob.backend.consumer.utils.JwtAuthentication;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

import java.util.concurrent.CopyOnWriteArraySet;

@Component
@ServerEndpoint("/websocket/{token}")  // 注意不要以'/'结尾
public class WebSocketServer {

    //用于存储id和链接的关系
    private static final ConcurrentHashMap<Integer,WebSocketServer> users=new ConcurrentHashMap<>();
    //匹配池子
    private static final CopyOnWriteArraySet<User> matchpool=new CopyOnWriteArraySet<>();
    //用户
    private User user;
    //会话链接
    private Session session=null;

    private static UserMapper userMapper;
    //无法注入全局的变量,所以用set来注入全局变量
    @Autowired
    public void setUserMapper(UserMapper userMapper){
        WebSocketServer.userMapper=userMapper;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) throws IOException {
        // 建立连接
        this.session=session;
        Integer userId= JwtAuthentication.getUserId(token);
        this.user=userMapper.selectById(userId);
        if(this.user!=null){
            users.put(userId,this);
        }else{
            this.session.close();
        }
        System.out.println(users);


    }

    @OnClose
    public void onClose() {
        System.out.println("disconnect");
        if(this.user!=null){
            users.remove(this.user.getId());
            matchpool.remove(this.user);
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) {//作为路由
        // 从Client接收消息
        JSONObject data =JSONObject.parseObject(message);
        System.out.println("get message"+message);
        String event=data.getString("event");
        if("start_matching".equals(event)){
            this.startMatching();
        }else if("cancel_matching".equals(event)){
            this.cancelMatching();
        }

    }

    private void startMatching() {
        System.out.println("start matching");
        matchpool.add(this.user);


        while(matchpool.size()>=2){
            GameMap gameMap=new GameMap(13,14,30);
            gameMap.createMap();
            Iterator<User> it=matchpool.iterator();
            User a=it.next();
            User b=it.next();
            JSONObject respA=new JSONObject();
            JSONObject respB=new JSONObject();
            //给A的信息
            respA.put("opponent_name",b.getUsername());
            respA.put("opponent_photo",b.getPhoto());
            respA.put("event","success_match");
            respA.put("gameMap",gameMap.getG());
            users.get(a.getId()).sendMessage(respA.toString());

            //给B的信息
            respB.put("opponent_name",a.getUsername());
            respB.put("opponent_photo",a.getPhoto());
            respB.put("event","success_match");
            respB.put("gameMap",gameMap.getG());
            users.get(b.getId()).sendMessage(respB.toString());

            matchpool.remove(a);
            matchpool.remove(b);

        }
    }

    private void cancelMatching() {
        System.out.println("cancel matching");
        matchpool.remove(this.user);
    }


    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

    public void sendMessage(String message){
        synchronized(this.session){
            try{
                this.session.getBasicRemote().sendText(message);
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
