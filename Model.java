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

class Cpu extends UOUO{
    public Cpu(double x,double y,double w,double h,double s,int p,int d,int f){
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
    public int isCollision(){ return hitFlag; }
    public int getFig(){ return figNum; }
}

class Player extends Cpu{
    public Player(double x,double y,double w,double h,double s,int p,int d,int f){
        super(x,y,w,h,s,p,d,f);
    }
}

// Model
class Model extends Observable {
    protected ArrayList<Cpu> cpu;
    protected ArrayList<String> figures;
    protected Cpu newUOUO;
    protected Player player;
    protected static int MAX_CPU=10;
    protected int cpuNum, gameScene=0;
    public Model(){
        cpu = new ArrayList<Cpu>();
        figures = new ArrayList<String>();
        // pictures
        figures.add("fish_houbou.png");
        figures.add("fish_sakana_piranha.png");
        figures.add("uni_broccoli.png");
        newUOUO = null;
        cpuNum = 0;
    }

    public ArrayList<Cpu> getUOUOs(){
        return cpu;
    }
    public ArrayList<String> getFigures(){
        return figures;
    }
    public Cpu getUOUO(int idx){
        return cpu.get(idx);
    }
    public Player getPlayer(){
        return player;
    }
    public void setScene(int n){
        gameScene=n;
    }
    public int getScene(){
        return gameScene;
    }

    // cpuCreator
    public void createCpu(){
        Cpu uouo;
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
        uouo = new Cpu(x,y,w,h,s,p,d,f);
        cpu.add(uouo);
        newUOUO = uouo;
        cpuNum++;
        setChanged();
        notifyObservers();
    }
    // cpuDestroyer
    public void destroyCPU(int idx){
        cpu.remove(idx);
        cpuNum--;
    }

    // playerInitializer
    public void initPlayer(double x,double y,double w,double h,double s,int p,int d){
        player = new Player(x,y,w,h,s,p,d,0);
        setChanged();
        notifyObservers();
    }

    // CollisionChecker
    public int checkCollision(int idx){
        if(player.x<cpu.get(idx).x && player.x+player.width>cpu.get(idx).x+cpu.get(idx).width){
            if(player.y<cpu.get(idx).y && player.y+player.height>cpu.get(idx).y+cpu.get(idx).height){
                return 1;
            }
        }
        return 0;
    }
}