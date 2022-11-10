package com.kob.backend.consumer.utils;

import java.util.Random;

public class GameMap {

    final private Integer rows;
    final private Integer cols;
    final private Integer inner_walls_count;
    final private int [][] g;

    final private int []dx={0, 1, 0, -1};
    final private int []dy={1, 0, -1, 0};

    public GameMap(Integer rows, Integer cols, Integer inner_walls_count){
        this.rows=rows;
        this.cols=cols;
        this.inner_walls_count=inner_walls_count;
        this.g = new int[rows][cols];
    }

    public int [][] getG(){
        return g;
    }

    private boolean check(int sx,int sy,int tx,int ty){
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


}
