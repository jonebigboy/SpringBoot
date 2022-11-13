package com.kob.backend.service.impl.user.bot;


import com.kob.backend.mapper.BotMapper;
import com.kob.backend.pojo.Bot;
import com.kob.backend.pojo.User;
import com.kob.backend.service.impl.utils.UserDetailsImpl;
import com.kob.backend.service.user.bot.updateBotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class updateBotServiceImpl  implements updateBotService {

    @Autowired
    private BotMapper botMapper;

    @Override
    public Map<String, String> updateBot(Map<String, String> data) {
        Map<String,String> map=new HashMap<>();
        UsernamePasswordAuthenticationToken authenticationToken=(UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser=(UserDetailsImpl) authenticationToken.getPrincipal();
        User user=loginUser.getUser();
        int bot_id=Integer.parseInt(data.get("id"));

        String title=data.get("title");
        String description=data.get("description");
        String content =data.get("content");
        if(title==null||title.length()==0){
            map.put("message","标题不可以为空");
            return map;
        }
        if(title.length()>100){
            map.put("message","标题太长啦");
            return map;
        }
        if(description==null||description.length()==0){
            description="这个用户太tm懒惰了";
        }
        if(description.length()>300){
            map.put("message","描述太长啦");
            return map;
        }
        if(content==null||content.length()==0){
            map.put("message","内容不可以为空哦");
            return map;
        }
        if(content.length()>10000){
            map.put("message","内容太长啦");
            return map;
        }
        Date now=new Date();
        Bot bot=botMapper.selectById(bot_id);
        if(bot==null){
            map.put("message","bot不存在");
            return map;
        }
        if(!bot.getUserId().equals(user.getId())){
            map.put("message","无权修改");
            return map;
        }
        Bot new_bot=new Bot( bot.getId(),
                user.getId(),
                title,
                description,
                content,
                bot.getCreateTime(),
                now
        );
        botMapper.updateById(new_bot);
        map.put("message","success");

        return map;
    }
}
