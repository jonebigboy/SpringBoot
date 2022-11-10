package com.kob.backend.consumer.utils;

import com.kob.backend.utils.JwtUtil;
import io.jsonwebtoken.Claims;

public class JwtAuthentication {

    public static Integer getUserId(String token){
        Integer userId = null;
        try{
            Claims claims= JwtUtil.parseJWT(token);
            userId=Integer.parseInt(claims.getSubject());

        }catch (Exception e){
            e.printStackTrace();
        }
        return userId;
    }
}
