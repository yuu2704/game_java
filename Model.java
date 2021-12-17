import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.io.File;
import java.lang.Math;

abstract class UOUO {
    protected double x,y,width,height,speed;
    protected int point,direction,hitFlag=0,figNum;
    public UOUO(double x,double y,double w,double h,double s,int p,int d,int f){
        this.x=x; this.y=y;
        width=w; height=h; speed=s;
        point=p; direction=d; figNum=f;
    }
        
}

class UOUOcpu extends UOUO{
    public UOUOcpu(double x,double y,double w,double h,double s,int p,int d,int f){
// class CPU extends UOUO{
//     public CPU(double x,double y,double w,double h,double s,int p,int d,int f){
        super(x,y,w,h,s,p,d,f);
    }
    // setter
    public void setX(double x){ this.x=x; }
    public void setY(double y){ this.y=y; }
    public void setWidth(double w){ width=w; }
    public void setHeight(double h){ height=h; }
    public void setSpeed(double s){ speed=s; }
    public void setPoint(int p){ point=p; }
    public void setDirection(int d){ direction=d; }
    public void initHitFlag(){ hitFlag=1; }

    public void setLocation(double x,double y){ this.x=x; this.y=y; }
    public void setSize(double w,double h){ width=w; height=h; }
    
    // getter
    public double getX(){ return x; }
    public double getY(){ return y; }
    public double getWidth(){ return width; }
    public double getHeight(){ return height; }
    public double getSpeed(){ return speed; }
    public double getPoint(){ return point; }
    public double getDirection(){ return direction; }
    public int getHit(){ return hitFlag; }
    public int getFig(){ return figNum; }
}

class UOUOplayer extends UOUOcpu{
    public UOUOplayer(double x,double y,double w,double h,double s,int p,int d,int f){
// class Player extends CPU{
//     public Player(double x,double y,double w,double h,double s,int p,int d,int f){
        super(x,y,w,h,s,p,d,f);
    }
}

// Model
class UOUOModel extends Observable {
    protected ArrayList<UOUOcpu> uouoCpu;
    // protected ArrayList<CPU> cpu;
    protected ArrayList<String> uouoFigures;
    protected UOUOcpu newUOUO;
    protected UOUOplayer uouoPlayer;
    protected static int MAX_CPU=10;
    protected int cpuNum;
    public UOUOModel(){
        uouoCpu = new ArrayList<UOUOcpu>();
        uouoFigures = new ArrayList<String>();
        // pictures
        uouoFigures.add("fish_houbou.png");
        uouoFigures.add("fish_sakana_piranha.png");
        uouoFigures.add("uni_broccoli.png");
        newUOUO = null;
        cpuNum = 0;
    }

    public ArrayList<UOUOcpu> getUOUOs(){
        return uouoCpu;
    }
    public ArrayList<String> getFigures(){
        return uouoFigures;
    }
    public UOUOcpu getUOUO(int idx){
        return uouoCpu.get(idx);
    }
    public UOUOplayer getPlayer(){
        return uouoPlayer;
    }

    //cpuのUOUOを作成
    public void createUOUOcpu(double x,double y,double w,double h,double s,int p,int d,int f){
        UOUOcpu uo;
        uo = new UOUOcpu(x,y,w,h,s,p,d,f);
        uouoCpu.add(uo);
        newUOUO = uo;
        setChanged();
        notifyObservers();
    }
    public void createUOUOcpu(){
        UOUOcpu uo;
        double x,y,w,h,s;
        int d,p,f,WIDTH=1000,HIGHT=1000;
        x=Math.random()*WIDTH/2;
        y=Math.random()*HIGHT*0.8+HIGHT*0.1;
        w=Math.random()*100+50;
        h=Math.random()*70*30;
        s=Math.random()*20+10;
        p=(int)w*(int)h;
        if(Math.random()<0.5){
            d=-1;
            x+=WIDTH+w;
        }else{
            d=1;
            x-=w;
        }
        f=(int)Math.random()+1;
        uo = new UOUOcpu(x,y,w,h,s,p,d,f);
        uouoCpu.add(uo);
        newUOUO = uo;
        setChanged();
        notifyObservers();
    }
    // cpuDestroyer
    public void destroyCPU(int idx){
        uouoCpu.remove(idx);
    }

    //playerのUOUOを作成
    public void createUOUOplayer(double x,double y,double w,double h,double s,int p,int d){
        uouoPlayer = new UOUOplayer(x,y,w,h,s,p,d,0);
        setChanged();
        notifyObservers();
    }
}