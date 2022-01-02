import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

class Main{
    public static void main(String argv[]) {
        Model model=new Model();
        UOUOFrame view=new UOUOFrame(model);
        AllController controller =new AllController(model,view);
    }
}

class AllController implements ActionListener {
    protected Model model;
    protected CPUController cpu;
    protected PlayerController player;
    protected UOUOFrame view;
    private javax.swing.Timer timer;
    //int loop;
    public AllController(Model model, UOUOFrame view) {
        this.model = model;
        this.view = view;
        //model.createUOUOplayer(500.0,500.0,100.0,100.0,20.0,0,1);
        //初期化はモデル
        cpu=new CPUController(model);
        //cpu=new HardCPUCOntroller(model);
        player=new PlayerController(model,view);
        timer = new javax.swing.Timer(40, this);
        //loop=0;
        timer.start();
    }
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==timer){
            if(model.getScene==1){
                cpu.updateCPU(/*loop++*/); //同期しないがいいかも?
                /*if(loop>=cpu.getCount()){
                    loop==0;
                }*/
            }
            view.repaint(); //fps25で更新
            //System.out.println(model.getPlayer().getX()+" "+model.getPlayer().getY());
        }else{
            if(model.getScene()==2){
                model.initPlayer(500,500,20,10,5,0,1); //常に中心からでいいので勝手にやってほしい
                for(int i=0;i<10;i++){
                    model.createCpu();
                }
                model.setFlag(1);
                view.setFlag(model.getScene());
            }else{
                model.setFlag(model.getScene()++);
                view.setflag(model.getFlag);
            }
            if(model.getScene()==2){
                timer.stop();
            }
        }
    }
}


class CPUController{
    protected Model model;
    //protected ArrayList<UOUOcpu> cpuList;
    public CPUController(Model m) {
        model=m;
    }
    public void updateCPU(){
        for(int i=0; i<model.getUOUOs().size(); i++){
            //System.out.println(model.getUOUOs().get(i).getX());
            model.getUOUOs().get(i).setX(model.getUOUOs().get(i).getX()+model.getUOUOs().get(i).getSpeed()*model.getUOUOs().get(i).getDirection());
            //System.out.println(model.getUOUOs().get(i).getX());
            //もし範囲外に言ったら消すように指示
            Cpu u=model.getUOUOs().get(i);
            if(u.getDirection()==-1&&u.getX()<0||u.getDirection()==1&&u.getX()>1500){
                model.destroyCPU(i);
                model.createCpu();
            }
            //ヒットフラグ立ってたら消す。
            if(u.isCollision()==1){
                Player player=model.getPlayer();
                player.setPoint((int)(player.getPoint()+model.getUOUO(i).getPoint()));
                model.destroyCPU(i);
                model.createCpu();
            }
            //消した時に追加
        }
    }
}


class PlayerController implements KeyListener {
    protected Player model;
    protected UOUOFrame view;
    public PlayerController(Model m, UOUOFrame view) {
        model=m.getPlayer();
        this.view=view;
        this.view.setFocusable(true);
        //System.out.println(view.isFocusable());
        this.view.addKeyListener(this);
    }
    public void keyPressed(KeyEvent e){
        // カーソルキーのイベントはkeyPressedで取得します．
        //System.out.println("aaa");
        int c=e.getKeyCode();
        switch (c) {
            case KeyEvent.VK_LEFT: // A or ←
            model.setX(model.getX()-model.getSpeed());
            //System.out.println("aaa");
            break;
            case KeyEvent.VK_RIGHT: // D or →
            model.setX(model.getX()+model.getSpeed());
            //System.out.println("aaa");
            break;
            case KeyEvent.VK_UP: // W or ↑
            model.setY(model.getY()-model.getSpeed());
            //System.out.println("aaa");
            break;
            case KeyEvent.VK_DOWN: // S
            model.setY(model.getY()+model.getSpeed());
            //System.out.println("aaa");
            break;
        }
    }
    public void keyTyped(KeyEvent e) {}
    public void keyReleased(KeyEvent e){}
}



/*public void getIntersection(UOUOplayer player,UOUOcpu cpu){
        double x2 = cpu.getX();
        double y2 = cpu.getY();
        double w2 = cpu.getWidth();
        double h2 = cpu.getHeight();
        double f1x2 = x1+Math.sqrt(w1*w1-h1*h1)/2;//焦点
        double f2x2 = x1-Math.sqrt(w1*w1-h1*h1)/2;//焦点
        double ang2 = 0.0;
        //--------------------------------------------X2
        double x1 = player.getX();
        double y1 = player.getY();
        double w1 = player.getWidth();
        double h1 = player.getHeight();
        double f1x1 = x1+Math.sqrt(w1*w1-h1*h1)/2;//焦点
        double f2x1 = x1-Math.sqrt(w1*w1-h1*h1)/2;//焦点
        double ang1 = 0.0;
        //--------------------------------------------X1


        //STEP 1
        double dang = ang1-ang2;
        double cos = Math.cos(dang);
        double sin = Math.sin(dang);
        double nx = w2*cos;
        double ny = -1*w2*sin;
        double px = h2*sin;
        double py = h2*cos;
        double ox = Math.cos(ang1)*(x2-x1)+Math.sin(ang1)*(y2-y1);
        double oy = -1*Math.sin(ang1)*(x2-x1)+Math.cos(ang1)*(y2-y1);

        //STEP 2
        double rx_pow2 = 1/(w1*w1);
        double ry_pow2 = 1/(h1*h1);
        //http://www.marupeke296.com/COL_2D_No7_EllipseVsEllipse.html
    }*/


//getFlag,setFlag(場面判断用)ほしい
//initCPU,initPlayerで初期設定を出来れば勝手にやってほしい。厳しければこちれでも出来るけど、拡張性が低くなりそう

class HardCPUController extends CPUController{
    protected ArrayList<Double> cpuyspeed;
    int count;
    public HardCPUController(Model m){
        super(m);
        count = 0;
        for(int i=0;i<model.getUOUOs().size();i++){
            cpuyspeed.add(0.0);
        }
    }

    public void updateCPU(int loop){
        if(loop==0){
            cpuyspeed.clear();
            for(int i=0; i<model.getUOUOs().size(); i++){
                cpuyspeed.add((Math.random()-0.5)*2);
            }
            Random random=new Random();
            count=random.nextInt(37)+12;
        }
        for(int i=0; i<model.getUOUOs().size(); i++){
            //System.out.println(model.getUOUOs().get(i).getX());
            model.getUOUOs().get(i).setX(model.getUOUOs().get(i).getX()+model.getUOUOs().get(i).getSpeed()*model.getUOUOs().get(i).getDirection()*Math.random());
            model.getUOUOs().get(i).setY(model.getUOUOs().get(i).getY()+model.getUOUOs().get(i).getSpeed()*model.getUOUOs().get(i).getDirection());
            //System.out.println(model.getUOUOs().get(i).getX());
            //もし範囲外に言ったら消すように指示
            Cpu u=model.getUOUOs().get(i);
            if(u.getDirection()==-1&&u.getX()<0||u.getDirection()==1&&u.getX()>1000){
                model.destroyCPU(i);
                model.createCpu();
            }else if(u.getY()<0||u.getY()>1000){
                model.destroyCPU(i);
                model.createCpu();
            }
            //ヒットフラグ立ってたら消す。
            if(u.isCollision()==1){
                Player player=model.getPlayer();
                player.setPoint((int)(player.getPoint()+model.getUOUO(i).getPoint()));
                model.destroyCPU(i);
                model.createCpu();
            }
            //消した時に追加
        }
    }
    public int getCount(){
        return count;
    }
}