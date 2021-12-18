import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

class Main{
    public static void main(String argv[]) {
        UOUOModel model=new UOUOModel();
        UOUOFrame view=new UOUOFrame(model);
        AllController controller =new AllController(model,view);
    }
}

class AllController implements ActionListener {
    protected UOUOModel model;
    protected UOUOCPUController cpu;
    protected UOUOPlayerController player;
    protected UOUOFrame view;
    private javax.swing.Timer timer;
    public AllController(UOUOModel model, UOUOFrame view) {
        this.model = model;
        this.view = view;
        //model.createUOUOplayer(500.0,500.0,100.0,100.0,20.0,0,1);
        //初期化はモデル
        cpu=new UOUOCPUController(model);
        player=new UOUOPlayerController(model,view);
        timer = new javax.swing.Timer(40, this);
        timer.start();
    }
    public void actionPerformed(ActionEvent e) {
        cpu.updateCPU(); //同期しないがいいかも?
        view.repaint(); //fps25で更新
        //System.out.println(model.getPlayer().getX()+" "+model.getPlayer().getY());
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
}


class UOUOCPUController{
    protected UOUOModel model;
    //protected ArrayList<UOUOcpu> cpuList;
    public UOUOCPUController(UOUOModel m) {
        //cpuList = m.getUOUOs();
        model=m;
    }
    public void updateCPU(){
        for(int i=0; i<model.getUOUOs().size(); i++){
            //System.out.println(model.getUOUOs().get(i).getX());
            model.getUOUOs().get(i).setX(model.getUOUOs().get(i).getX()+model.getUOUOs().get(i).getSpeed()*model.getUOUOs().get(i).getDirection());
            //System.out.println(model.getUOUOs().get(i).getX());
            //もし範囲外に言ったら消すように指示
            UOUOcpu u=model.getUOUOs().get(i);
            if(u.getDirection()==-1&&u.getX()<0||u.getDirection()==1&&u.getX()>1500){
                model.destroyCPU(i);
                model.createUOUOcpu();
            }
            //ヒットフラグ立ってたら消す。
            if(u.getHit()==1){
                UOUOplayer player=model.getPlayer();
                player.setPoint(player.getPoint()+model.getUOUO(i).getPoint());
                model.destroyCPU(i);
                model.createUOUOcpu();
            }
            //総数が少なかったら生成(消した時に追加すればよくね？)
        }
    }
}


class UOUOPlayerController implements KeyListener {
    protected UOUOplayer model;
    protected UOUOFrame view;
    public UOUOPlayerController(UOUOModel m, UOUOFrame view) {
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