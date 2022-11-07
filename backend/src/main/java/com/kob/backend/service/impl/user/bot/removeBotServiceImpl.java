package com.kob.backend.service.impl.user.bot;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kob.backend.mapper.BotMapper;
import com.kob.backend.pojo.Bot;
import com.kob.backend.pojo.User;
import com.kob.backend.service.impl.utils.UserDetailsImpl;
import com.kob.backend.service.user.bot.removeBotService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class removeBotServiceImpl implements removeBotService {

    @Autowired
    private BotMapper botMapper;

    @Override
    public Map<String, String> removeBot(int id) {
        UsernamePasswordAuthenticationToken authenticationToken=(UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser=(UserDetailsImpl) authenticationToken.getPrincipal();
        User user=loginUser.getUser();

        Map<String,String> map=new HashMap<>();
        Bot bot=botMapper.selectById(id);

        if(bot==null){
            map.put("message","bot不存在");
            return map;
        }
        if(!bot.getUserId().equals(user.getId())){
            map.put("message","用户没有权限删除");
            return map;
        }
        botMapper.deleteById(id);
        map.put("message","success");

        return map;
    }
}
