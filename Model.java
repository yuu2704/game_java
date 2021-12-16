import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.io.File;
import javax.imageio.*;

abstract class UOUO {
    protected double x,y,width,height,speed;
    protected int point,direction,hitFlag=0;
    public UOUO(double x,double y,double w,double h,double s,int p,int d){
        this.x=x; this.y=y;
        width=w; height=h; speed=s;
        point=p; direction=d;
    }
        
}

class UOUOcpu extends UOUO{
    public UOUOcpu(double x,double y,double w,double h,double s,int p,int d){
        super(x,y,w,h,s,p,d);
    }
    // setter
    public void setX(double x){ this.x=x; }
    public void setY(double y){ this.y=y; }
    public void setWidth(double w){ width=w; }
    public void setHeight(double h){ height=h; }
    public void setSpeed(double s){ speed=s; }
    public void setPoint(int p){ point=p; }
    public void setDirection(int d){ direction=d; }
    public void collision(){ hitFlag=1; }

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
}

class UOUOplayer extends UOUOcpu{
    public UOUOplayer(double x,double y,double w,double h,double s,int p,int d){
        super(x,y,w,h,s,p,d);
    }
}

// Model
class UOUOModel extends Observable {
    protected ArrayList<UOUOcpu> uouoCpu;
    protected UOUOcpu newUOUO;
    protected UOUOplayer uouoPlayer;
    protected int point;
    protected static int MAX_CPU=10;
    public UOUOModel(){
        uouoCpu = new ArrayList<UOUOcpu>();
        createUOUOcpu(100.0, 100.0, 100.0, 100.0, 1.0, 0, 1);
        newUOUO = null;
        point = 0;
        // pictures
        try{
            BufferedImage figure = ImageIO.read(new File("fish_houbou.png"));
            figure = ImageIO.read(new File("fish_sakana_piranha.png"));
            figure = ImageIO.read(new File("uni_broccoli.png"));
        }catch(Exception e){

        }
    }

    public ArrayList<UOUOcpu> getUOUOs(){
        return uouoCpu;
    }
    public UOUOcpu getUOUO(int idx){
        return uouoCpu.get(idx);
    }
    public UOUOplayer getPlayer(){
        return uouoPlayer;
    }

    //cpuのUOUOを作成
    public void createUOUOcpu(double x,double y,double w,double h,double s,int p,int d){
        UOUOcpu uo;
        uo = new UOUOcpu(x,y,w,h,s,p,d);
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
        uouoPlayer = new UOUOplayer(x,y,w,h,s,p,d);
        setChanged();
        notifyObservers();
    }
}