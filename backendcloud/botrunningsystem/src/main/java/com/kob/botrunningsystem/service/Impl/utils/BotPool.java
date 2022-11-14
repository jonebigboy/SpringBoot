package com.kob.botrunningsystem.service.Impl.utils;


import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class BotPool extends Thread{

    private final ReentrantLock lock =new ReentrantLock();

    private final Condition condition = lock.newCondition();

    private Queue<Bot> bots =new LinkedList<>();

    public void addBot(Integer userId,String botCode,String input){
        lock.lock();
        try{
            bots.add(new Bot(userId,botCode,input));
            condition.signalAll();
        }finally {
            lock.unlock();
        }
    }
    private void consumer(Bot bot){
        Consumer consumer=new Consumer();
        consumer.startTimeOut(2000,bot);//多线程执行代码
    }

    @Override
    public void run() {//多线程找到所有不同的代码
        while(true){
            lock.lock();
            if(bots.isEmpty()){
                try {
                    condition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    lock.unlock();//万一没有执行上面解锁
                    break;
                }
            }else{
                Bot bot=bots.remove();
                lock.unlock();
                consumer(bot);
            }
        }


    }
}
