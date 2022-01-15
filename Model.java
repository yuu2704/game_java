import java.util.*;
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
    public void initHitFlag(){ hitFlag=0; }

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
    static int MAX_HP=30;
    protected int hitPoint=MAX_HP;
    public Player(double x,double y,double w,double h,double s,int p,int d,int f){
        super(x,y,w,h,s,p,d,f);
    }

    // add HitPoint
    public void setHP(int hp){
        hitPoint=hp;
    }
    public int getHP(){
        return hitPoint;
    }
    public int getMaxHP(){
        return MAX_HP;
    }
}

// Model
class Model {
    protected ArrayList<Cpu> cpu;
    protected ArrayList<String> figures;
    protected ArrayList<String> sounds;
    protected ArrayList<Double> speedY;
    protected Cpu newUOUO;
    protected Player player;
    public static int WIDTH=1000, HEIGHT=800, MAX_TIME=1000;
    protected int cpuNum, gameScene=0, time=0, loop=0, count=0;
    public Model(){
        cpu = new ArrayList<Cpu>();
        figures = new ArrayList<String>();
        sounds = new ArrayList<String>();
        speedY = new ArrayList<Double>();
        // pictures
        figures.add("fish_aji.png");
        figures.add("fish_sakana_piranha.png");
        figures.add("fish_ookamiuo.png");
        figures.add("taiyaki.png");
        sounds.add("paku.mp3");
        newUOUO = null;
        cpuNum = 0;
    }

    // setter
    public void addSpeedY(double s){
        speedY.add(s);
    }
    public void deleteSpeedY(int idx){
        speedY.remove(idx);
    }
    public void clearSpeedY(){
        speedY.clear();
    }
    public void setTime(int t){
        time=t;
    }
    public void setLoop(int l){
        loop = l;
    }
    public void setCount(int c){
        count = c;
    }

    // getter
    public ArrayList<Cpu> getUOUOs(){
        return cpu;
    }
    public ArrayList<String> getFigures(){
        return figures;
    }
    public ArrayList<Double> getSpeedYs(){
        return speedY;
    }
    public double getSpeedY(int idx){
        return speedY.get(idx);
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
    public int getNum(){
        return cpuNum;
    }
    public int getMaxTime(){
        return MAX_TIME;
    }
    public int getFrameWidth(){
        return WIDTH;
    }
    public int getFrameHeight(){
        return HEIGHT;
    }
    public int getTime(){
        return time;
    }
    public int getLoop(){
        return loop;
    }
    public int getCount(){
        return count;
    }

    // cpuCreator
    public void createCpu(){
        Cpu uouo;
        double x,y,w,h,s;
        int d,p,f;
        x=Math.random()*WIDTH/2;
        y=Math.random()*HEIGHT*0.8+HEIGHT*0.1;
        w=Math.random()*100+50; // 50~150
        h=Math.random()*70+30;  // 30~100
        s=Math.random()*10+10;
        p=(int)w*(int)h/1500;
        if(Math.random()<0.5){
            d=-1;
            x+=WIDTH+w;
        }else{
            d=1;
            x-=WIDTH+w;
        }
        f=(int)(Math.random()*3)+1;
        uouo = new Cpu(x,y,w,h,s,p,d,f);
        cpu.add(uouo);
        newUOUO = uouo;
        cpuNum++;
    }
    // cpuDestroyer
    public void destroyCPU(int idx){
        cpu.remove(idx);
        cpuNum--;
    }
    public void clearCPU(){
        cpu.clear();
        cpuNum=0;
    }

    // playerInitializer
    public void initPlayer(){
        player = new Player(WIDTH/2,HEIGHT/2,60,40,15,0,1,0);
    }
    public void resizePlayer(){
        player.width = player.width+player.width*player.point/1500;
        player.height = player.height+player.height*player.point/1500;
    }

    // CollisionChecker
    public int checkCollision(int idx){ // notCollision:0, edible:1, notEdible:2
        if(cpu.get(idx).x<player.x+player.width/4 && player.x+player.width*3/4<cpu.get(idx).x+cpu.get(idx).width){
            if(cpu.get(idx).y<player.y+player.height/4 && player.y+player.height*3/4<cpu.get(idx).y+cpu.get(idx).height){
                if(player.point/20+3<cpu.get(idx).point){
                    return 2;
                }else{
                    return 1;
                }
            }
        }
        return 0;
    }
}