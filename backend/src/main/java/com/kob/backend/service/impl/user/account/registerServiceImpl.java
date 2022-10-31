package com.kob.backend.service.impl.user.account;



import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.User;
import com.kob.backend.service.user.account.registerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.jws.soap.SOAPBinding;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class registerServiceImpl implements registerService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    PasswordEncoder passwordEncoder;


    @Override
    public Map<String, String> register(String username, String password, String confirm_password) {
        Map<String,String> map=new HashMap<>();
        if(username==null){
            map.put("message","用户名不能为空");
            return map;
        }
        if(password==null||confirm_password==null){
            map.put("message","密码不可以为空");
            return map;
        }
        username=username.trim();
        if(username.length()==0){
            map.put("message","用户名不能为空");
            return map;
        }
        if(username.length()>100||password.length()>100){
            map.put("message","用户名或者密码不可以太长");
            return map;
        }
        if(!password.equals(confirm_password)) {
            map.put("message", "2次密码不相等");
            return map;
        }
        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("username",username);
        List<User> list= userMapper.selectList(queryWrapper);
        if(!list.isEmpty()){
            map.put("message","用户已经存在");
            return map;
        }
        String encodePassword=passwordEncoder.encode(password);
        String photo="https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fc-ssl.duitang.com%2Fuploads%2Fblog%2F202107%2F23%2F20210723125859_f6b2f.thumb.1000_0.jpeg&refer=http%3A%2F%2Fc-ssl.duitang.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1669719413&t=0ac657bd78ab60211d632ceac0a50188";
        User user=new User(null,username,encodePassword,photo);
        userMapper.insert(user);
        map.put("message","success");
        return map;
    }
}
