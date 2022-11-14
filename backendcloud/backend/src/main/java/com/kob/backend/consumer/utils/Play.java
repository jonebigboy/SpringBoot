package com.kob.backend.consumer.utils;


import com.kob.backend.pojo.Bot;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.parameters.P;

import javax.naming.ldap.PagedResultsControl;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Play {
    private Integer id;
    private Integer sx;
    private Integer sy;

    private Integer botId;

    private String botCode;
    private List<Integer> step;//用这个数组来生成蛇的身体

    private boolean check_increace(int step){
        if (step <= 10) return true;
        if ((step - 10) % 3 == 0) return true;
        return false;
    }

    public String getStringStep(){
        StringBuilder res=new StringBuilder();
        for(int d:step){
            res.append(d);
        }
        return res.toString();
    }

    public List<Cell> getCell(){
        List<Cell> res=new ArrayList<>();
        int [] dx={-1, 0, 1, 0};
        int [] dy={0, 1, 0, -1};
        int x=sx,y=sy;
        res.add(new Cell(x,y));
        int s=0;
        for (Integer d:step){
            x += dx[d];
            y += dy[d];
            res.add(new Cell(x,y));
            if(!check_increace(++s)){
                res.remove(0);
            }
        }
        return res;

    }


}
