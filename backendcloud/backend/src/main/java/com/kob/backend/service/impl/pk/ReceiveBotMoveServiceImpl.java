package com.kob.backend.service.impl.pk;


import com.kob.backend.consumer.WebSocketServer;
import com.kob.backend.consumer.utils.GameMap;
import com.kob.backend.service.pk.ReceiveBotMoveService;
import org.springframework.stereotype.Service;

@Service
public class ReceiveBotMoveServiceImpl implements ReceiveBotMoveService {

    @Override
    public String receiveBotMove(Integer userId, Integer direction) {
        System.out.println("receiveBotMove "+userId+" "+direction);
        if(WebSocketServer.users.get(userId)!=null){//不空那么就设置方向
            GameMap game = WebSocketServer.users.get(userId).gameMap;
            if (game != null) {
                if (game.getPlayA().getId().equals(userId)) {
                    game.setNextStepA(direction);
                } else if (game.getPlayB().getId().equals(userId)) {
                    game.setNextStepB(direction);
                }
            }
        }
        return "success receive move";
    }
}
