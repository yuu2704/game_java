import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

class Main{
    public static void main(String argv[]) {
        UOUOModel model=new UOUOModel();
        uouoFrame view=new uouoFrame();
        new AllController(model,view);
    }
}

class AllController implements ActionListener {
    protected UOUOModel model;
    protected UOUOCPUController cpu;
    protected UOUOPlayerController player;
    protected uouoFrame view;
    private javax.swing.Timer timer;
    public AllController(UOUOModel model, uouoFrame view) {
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
        cpu.updateCPU();
        view.repaint(); //fps25で更新
        System.out.println(model.getPlayer().getX()+" "+model.getPlayer().getY());
    }
}


class UOUOCPUController{
    protected UOUOModel model;
    //protected ArrayList<UOUOcpu> cpuList;
    public UOUOCPUController(UOUOModel m) {
        //cpuList = m.getUOUOs();
        model=m;
    }
    public void updateCPU(){
        ArrayList<UOUOcpu> cpuList =model.getUOUOs();
        for(int i=0; i<cpuList.size(); i++){
            UOUOcpu u=cpuList.get(i);
            u.setX(u.getX()+u.getSpeed()*u.getDirection());
            //もし範囲外に言ったら消すように指示UOUOdelete
            if(u.getDirection()==1&&u.getX()<0||u.getDirection()==-1&&u.getX()>1000){
                UOUODelete(i);
            }
            //ヒットフラグ立ってたら消す。
            if(u.getHit()){
                UOUOdelete(i);
                createUOUOcpu;
            }
            //総数が少なかったら生成
        }
    }
}


class UOUOPlayerController implements KeyListener {
    protected UOUOplayer model;
    protected uouoFrame view;
    public UOUOPlayerController(UOUOModel m, uouoFrame view) {
        model=m.getPlayer();
        this.view=view;
        //view.getPanel().setFocusable(true);
        //System.out.println(view.isFocusable());
        view.getPanel().addKeyListener(this);
    }
    public void keyPressed(KeyEvent e){
        // カーソルキーのイベントはkeyPressedで取得します．
        int c=e.getKeyCode();
        switch (c) {
            case KeyEvent.VK_LEFT: // A or ←
            model.setX(model.getX()-model.getSpeed());
            break;
            case KeyEvent.VK_RIGHT: // D or →
            model.setX(model.getX()+model.getSpeed());
            break;
            case KeyEvent.VK_UP: // W or ↑
            model.setY(model.getY()-model.getSpeed());
            System.out.println("aaaaaaaaaaa");
            break;
            case KeyEvent.VK_DOWN: // S
            model.setY(model.getY()+model.getSpeed());
            break;
        }
    }
    public void keyTyped(KeyEvent e) {}
    public void keyReleased(KeyEvent e){}
}