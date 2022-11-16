package com.kob.backend.consumer;

import com.alibaba.fastjson2.JSONObject;
import com.kob.backend.consumer.utils.GameMap;
import com.kob.backend.consumer.utils.JwtAuthentication;
import com.kob.backend.mapper.BotMapper;
import com.kob.backend.mapper.RecordMapper;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.Bot;
import com.kob.backend.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

import java.util.concurrent.ConcurrentHashMap;


@Component
@ServerEndpoint("/websocket/{token}")  // 注意不要以'/'结尾
public class WebSocketServer {

    //用于存储id和链接的关系
    public static final ConcurrentHashMap<Integer,WebSocketServer> users=new ConcurrentHashMap<>();
    //匹配池子

    //用户
    private User user;
    //会话链接
    private Session session=null;
    public GameMap gameMap=null;

    public static UserMapper userMapper;
    //无法注入全局的变量,所以用set来注入全局变量

    public static RecordMapper recordMapper;

    public static BotMapper botMapper;

    public static RestTemplate restTemplate;

    private final String addUrl="http://127.0.0.1:3001/add/player/";

    private final String removeUrl="http://127.0.0.1:3001/remove/player/";

    @Autowired
    public void setUserMapper(UserMapper userMapper){
        WebSocketServer.userMapper=userMapper;
    }

    @Autowired
    public void setRcordMapper(RecordMapper recordMapper){
        WebSocketServer.recordMapper = recordMapper;
    }

    @Autowired
    public void setBotMapper(BotMapper botMapper){
        WebSocketServer.botMapper=botMapper;
    }

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate){
        WebSocketServer.restTemplate=restTemplate;
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

        }
    }

    @OnMessage
    public void onMessage(String message, Session session) {//作为路由
        // 从Client接收消息
        JSONObject data =JSONObject.parseObject(message);
        System.out.println("get message "+message);
        String event= data.getString("event");
        String botId= data.getString("bot_id");
        if("start_matching".equals(event)){
            this.startMatching(botId);
        }else if("cancel_matching".equals(event)){
            this.cancelMatching();
        } else if ("move".equals(event)) {
            this.setmove(data.getInteger("direction"));
        }

    }
    
    private void setmove(Integer d){
        if(gameMap.getPlayA().getId().equals(user.getId())){
            if(gameMap.getPlayA().getBotId().equals(-1))
                gameMap.setNextStepA(d);
        } else if (gameMap.getPlayB().getId().equals(user.getId())) {
            if(gameMap.getPlayB().getBotId().equals(-1))
                gameMap.setNextStepB(d);
        }
    }

    public void startGame(Integer aId,Integer aBotId,Integer bId,Integer bBotId){

        User a= userMapper.selectById(aId);
        User b= userMapper.selectById(bId);
        Bot BotA=botMapper.selectById(aBotId);
        Bot BotB=botMapper.selectById(bBotId);
        GameMap gameMap=new GameMap(
                13,
                14,
                10,
                a.getId(),
                BotA,
                b.getId(),
                BotB
        );

        gameMap.createMap();
        gameMap.start();//创建新的线程
        if (users.get(a.getId()) != null)
            users.get(a.getId()).gameMap=gameMap;//获取地图的2个play的信息
        if (users.get(b.getId()) != null)
            users.get(b.getId()).gameMap=gameMap;

        //把地图上的玩家信息也传输给前端
        JSONObject gameInfo =new JSONObject();

        gameInfo.put("a_id",gameMap.getPlayA().getId());
        gameInfo.put("a_sx",gameMap.getPlayA().getSx());
        gameInfo.put("a_sy",gameMap.getPlayA().getSy());
        gameInfo.put("b_id",gameMap.getPlayB().getId());
        gameInfo.put("b_sx",gameMap.getPlayB().getSx());
        gameInfo.put("b_sy",gameMap.getPlayB().getSy());
        gameInfo.put("map",gameMap.getG());


        JSONObject respA=new JSONObject();
        JSONObject respB=new JSONObject();

        //给A的信息
        respA.put("opponent_name",b.getUsername());
        respA.put("opponent_photo",b.getPhoto());
        respA.put("event","success_match");
        respA.put("gameInfo",gameInfo);
        if (users.get(a.getId()) != null)
            users.get(a.getId()).sendMessage(respA.toString());

        //给B的信息
        respB.put("opponent_name",a.getUsername());
        respB.put("opponent_photo",a.getPhoto());
        respB.put("event","success_match");
        respB.put("gameInfo",gameInfo);
        if (users.get(b.getId()) != null)
            users.get(b.getId()).sendMessage(respB.toString());


    }

    private void startMatching(String botId) {
        System.out.println("start matching");
        MultiValueMap<String,String> data=new LinkedMultiValueMap<>();
        data.add("user_id",this.user.getId().toString());
        data.add("rating",this.user.getRating().toString());
        data.add("bot_id",botId);
        restTemplate.postForObject(addUrl,data,String.class);

    }

    private void cancelMatching() {
        System.out.println("cancel matching");
        MultiValueMap<String,String> data=new LinkedMultiValueMap<>();
        data.add("user_id",this.user.getId().toString());

        restTemplate.postForObject(this.removeUrl,data,String.class);
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
