package com.kob.backend.service.impl.user.bot;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kob.backend.mapper.BotMapper;
import com.kob.backend.pojo.Bot;
import com.kob.backend.pojo.User;
import com.kob.backend.service.impl.utils.UserDetailsImpl;
import com.kob.backend.service.user.bot.getlistBotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class getlistBotServiceImpl  implements getlistBotService {

    @Autowired
    private BotMapper botMapper;

    @Override
    public List<Bot> getlistBot() {
        UsernamePasswordAuthenticationToken authenticationToken=(UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser=(UserDetailsImpl) authenticationToken.getPrincipal();
        User user=loginUser.getUser();
        QueryWrapper queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("user_id",user.getId());
        List<Bot> list=new ArrayList<>();
        list=botMapper.selectList(queryWrapper);

        return list;
    }
}
