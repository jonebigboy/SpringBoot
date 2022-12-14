package com.kob.backend.consumer.utils;

import com.alibaba.fastjson2.JSONObject;
import com.kob.backend.consumer.WebSocketServer;
import com.kob.backend.pojo.Bot;
import com.kob.backend.pojo.Record;
import com.kob.backend.pojo.User;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

public class GameMap extends Thread{
    final private Integer rows;
    final private Integer cols;
    final private Integer inner_walls_count;
    final private int [][] g;
    final private int []dx={-1, 0, 1, 0};
    final private int []dy={0, 1, 0, -1};

    private ReentrantLock lock=new ReentrantLock();
    private final Play playA,playB;
    private Integer nextStepA=null;
    private Integer nextStepB=null;
    private String loser="";
    private String status="moving";//end是结束

    private final String sendCodeUrl="http://127.0.0.1:3002/add/bot/";

    public GameMap(Integer rows, Integer cols, Integer inner_walls_count, Integer aId, Bot botA, Integer bId,Bot botB){
        this.rows=rows;
        this.cols=cols;
        this.inner_walls_count=inner_walls_count;
        this.g = new int[rows][cols];

        Integer aBotId = -1, bBotId = -1;
        String aBotCode = "", bBotCode = "";//自己的时候就是空的

        if(botA!=null){
            aBotId= botA.getId();
            aBotCode=botA.getContent();
        }
        if(botB!=null){
            bBotId=botB.getId();
            bBotCode=botB.getContent();
        }
        this.playA=new Play(aId,11,1,aBotId,aBotCode,new ArrayList<>());//左下角
        this.playB=new Play(bId,1,12,bBotId,bBotCode,new ArrayList<>());//右上角
    }

    public Play getPlayA(){
        return this.playA;
    }

    public Play getPlayB(){
        return this.playB;
    }

    public int [][] getG(){
        return g;
    }

    public void setNextStepA(Integer nextStepA) {
        lock.lock();
        try{
            this.nextStepA = nextStepA;
        }finally {
            lock.unlock();
        }

    }
    public void setNextStepB(Integer nextStepB) {
        lock.lock();
        try{
            this.nextStepB = nextStepB;
        }finally {
            lock.unlock();
        }
    }
    String getInput(Play play){//区分是A还是B
        Play me,you;
        if(play.getId().equals(playA.getId())){
            me=playA;
            you=playB;
        }else{
            me=playB;
            you=playA;
        }
        return getStringMap()+"#"+
                me.getSx() + "#" +
                me.getSy() + "#(" +
                me.getStringStep() + ")#" +
                you.getSx() + "#" +
                you.getSy() + "#(" +
                you.getStringStep() + ")";

    }

    private void sendBotCode(Play player){
        if(player.getBotId().equals(-1)) return;
        MultiValueMap<String,String> data=new LinkedMultiValueMap<>();
        data.add("user_id",player.getId().toString());
        data.add("bot_code",player.getBotCode());
        data.add("input",getInput(player));

        System.out.println("sendBotCode "+player.getId());
        WebSocketServer.restTemplate.postForObject(sendCodeUrl,data,String.class);

    }
    //接收信息并且赋值
    public boolean nextStep()  {

        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


        sendBotCode(playA);
        sendBotCode(playB);

        for(int i=0;i<100;i++){
            try{
                Thread.sleep(100);
                lock.lock();
                try {
                    if(nextStepA!=null&&nextStepB!=null){
                        this.playA.getStep().add(nextStepA);
                        this.playB.getStep().add(nextStepB);
                        return true;
                    }
                }finally {
                    lock.unlock();
                }
            }catch (InterruptedException e){
                e.printStackTrace();
            }

        }
        return false;
    }

    private void group_send(String message){
        if (WebSocketServer.users.get(playA.getId()) != null)
            WebSocketServer.users.get(this.playA.getId()).sendMessage(message);
        if (WebSocketServer.users.get(playB.getId()) != null)
            WebSocketServer.users.get(this.playB.getId()).sendMessage(message);
    }

    //发送信息
    private void sendMove(){
        lock.lock();
        try{
            JSONObject resp=new JSONObject();
            resp.put("event","move");
            resp.put("a_d",nextStepA);
            resp.put("b_d",nextStepB);
            this.group_send(resp.toJSONString());
            nextStepA=null;
            nextStepB=null;
        }finally {
            lock.unlock();
        }
    }
    private void sendResult(){

        JSONObject resp=new JSONObject();
        resp.put("event","result");
        resp.put("loser",this.loser);
        this.saveToDB();
        this.group_send(resp.toJSONString());
    }
    private void updateUserRating(Play player,Integer rating){
        User user=WebSocketServer.userMapper.selectById(player.getId());
        user.setRating(rating);
        WebSocketServer.userMapper.updateById(user);

    }
    private void saveToDB() {
        Integer ratingA=WebSocketServer.userMapper.selectById(playA.getId()).getRating();
        Integer ratingB=WebSocketServer.userMapper.selectById(playB.getId()).getRating();

        if("A".equals(loser)){
            ratingA-=2;
            ratingB+=5;
        }else if("B".equals(loser)) {
            ratingA+=5;
            ratingB-=2;
        }

        updateUserRating(playA,ratingA);
        updateUserRating(playB,ratingB);

        Record record = new Record(
                null,
                this.playA.getId(),
                this.playA.getSx(),
                this.playA.getSy(),
                this.playB.getId(),
                this.playB.getSx(),
                this.playB.getSy(),
                this.playA.getStringStep(),
                this.playB.getStringStep(),
                this.getStringMap(),
                this.loser,
                new Date()
                );

        WebSocketServer.recordMapper.insert(record);
    }
    private String getStringMap(){
        StringBuilder res=new StringBuilder();
        for (int i=0;i<this.rows;i++){
            for(int j=0;j<this.cols;j++){
                res.append(g[i][j]);
            }
        }
        return res.toString();
    }

    private void judge(){
        List<Cell> snakeA= this.playA.getCell();
        List<Cell> snakeB =this.playB.getCell();

        boolean checkA=this.valid_snake(snakeA,snakeB);
        boolean checkB=this.valid_snake(snakeB,snakeA);
        System.out.println(checkA+" "+checkB);
        if(!checkA||!checkB){
            this.status="end";
            if(!checkA&&!checkB){
                this.loser="all";
            } else if (!checkA) {
                this.loser="A";
            } else {
                this.loser="B";
            }
        }


    }
    private boolean valid_snake(List<Cell> snakeA,List<Cell> snakeB){
        Cell head=snakeA.get(snakeA.size()-1);

        if(g[head.getX()][head.getY()]==1) {
            return false;
        }

        for (int i=0;i<snakeA.size()-1;i++){
            if(head.getX()==snakeA.get(i).getX()&&head.getY()==snakeA.get(i).getY()){

                return false;
            }
        }

        for (int i=0;i<snakeB.size()-1;i++) {
            if (head.getX() == snakeB.get(i).getX() && head.getY() == snakeB.get(i).getY()) {
                return false;
            }
        }
        return true;
    }

    private boolean check(int sx, int sy, int tx, int ty){
        if(sx==tx&&sy==ty){
            return true;
        }

        g[sx][sy]=1;

        for (int i=0;i<4;i++){
            int x=sx+dx[i];
            int y=sy+dy[i];
            if(x>=0&&x<this.rows&&y>=0&&y<this.cols&&g[x][y]==0){
                if(this.check(x,y,tx,ty)){
                    g[sx][sy]=0;
                    return true;
                }
            }
        }

        g[sx][sy]=0;//为false时候的还原现场
        return false;
    }

    private boolean draw(){
        for (int i=0;i<rows;i++){
            for (int j=0;j<cols;j++){
                g[i][j]=0;
            }
        }

        for(int i=0;i<rows;i++){
            g[i][0]=g[i][this.cols-1]=1;
        }
        for (int j=0;j<cols;j++){
            g[0][j]=g[this.rows-1][j]=1;
        }

        Random random=new Random();

        for(int i=1;i<=this.inner_walls_count/2;i++){
            for (int j=0;j<1000;j++){

                int r = random.nextInt(this.rows);
                int c = random.nextInt(this.cols);

                if(r==1&&c==this.cols-2||c==1&&r==this.rows-2) continue;
                if(g[r][c]==1||g[this.rows-r-1][this.cols-c-1]==1) continue;

                g[r][c]=g[this.rows-r-1][this.cols-c-1]=1;
                break;

            }
        }

        return check(1,cols-2,rows-2,1);

    }

    public void createMap(){
        for (int i=0;i<1000;i++){
            if(this.draw()){
                break;
            }
        }
    }


    @Override
    public void run() {
        for(int i=0;i<1000;i++){

            if(this.nextStep()){
                this.judge();
                System.out.println("check end "+this.status);
                if(this.status.equals("moving")) {
                    this.sendMove();
                }else{
                    this.sendResult();
                    break;
                }
            }else{
                status="end";
                lock.lock();
                try{
                    if(this.nextStepA==null&&this.nextStepB==null){
                        this.loser="all";
                    } else if (this.nextStepB==null) {
                        this.loser="B";
                    } else if (this.nextStepA==null) {
                        this.loser="A";
                    }


                }finally {
                    lock.unlock();
                }

                this.sendResult();
                break;
            }

        }


    }
}
