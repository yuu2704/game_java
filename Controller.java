import javax.swing.*;
import java.awt.event.*;
import java.util.*;

class Main{
    public static void main(String argv[]) {
        ArrayList<Sounds> sounds = new ArrayList<Sounds>();
        Model model=new Model();
        View view=new View(model);
        AllController controller =new AllController(model,view);
    }
}

class AllController implements ActionListener {
    protected Model model;
    protected CPUController cpu;
    protected PlayerController player;
    protected View view;
    private javax.swing.Timer timer;
    protected JButton start;
    protected JButton replay;
    public AllController(Model model, View view) {
        this.model = model;
        this.view = view;
        //初期化はモデル
        start=view.getPanel().getStartButton();
        replay=view.getPanel().getReplayButton();
        start.addActionListener(this);
        replay.addActionListener(this);
        timer = new javax.swing.Timer(50, this);
        timer.start();
    }

    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==timer){
            if(model.getScene()==1){
                model.setTime(model.getTime()+1);
                int time=model.getTime();
                int max=model.getMaxTime();
                if(time==max){
                    model.setScene(2);
                }else{
                    player.action();
                    //後半用動作
                    if(time<max/2){
                        cpu.updateCPU();
                    }else{
                        int loop=model.getLoop();
                        cpu.hardupdateCPU(loop++);
                        if(loop>=model.getCount()){
                            loop=0;
                        }
                        model.setLoop(loop);
                    }
                    //-----
                }
                view.getPanel().setflag(model.getScene());
                if(model.getScene()==2){
                    model.clearCPU();
                }
            }
            view.getPanel().repaint();
            this.view.setFocusable(true);
        }else{
            if(model.getScene()!=1){
                model.initPlayer();
                for(int i=0;i<10;i++){
                    model.createCpu();
                }
                if(cpu==null){
                    cpu=new CPUController(model);
                    player=new PlayerController(model,view);
                }else{
                    player.init();
                }
                model.setLoop(0);;
                model.setScene(1);
                view.getPanel().setflag(model.getScene());
                model.setCount(0);
                model.setTime(0);
                view.repaint();
            }
        }
    }
}


class CPUController{
    protected Model model;
    public CPUController(Model m) {
        model=m;
    }
    public void updateCPU(){
        for(int i=0; i<model.getUOUOs().size(); i++){
            Cpu u=model.getUOUOs().get(i);
            u.setX(u.getX()+u.getSpeed()*u.getDirection());
            //もし範囲外に言ったら消すように指示
            if(u.getDirection()==-1&&u.getX()<0-u.getWidth()||u.getDirection()==1&&u.getX()>model.getFrameWidth()){
                model.destroyCPU(i);
                model.createCpu();
            }
            //ヒットフラグ立ってたら消す。
            if(model.checkCollision(i)==1){
                Player player=model.getPlayer();
                player.setPoint((int)(player.getPoint()+model.getUOUO(i).getPoint()));
                model.resizePlayer();
                model.destroyCPU(i);
                model.createCpu();
            }else if(model.checkCollision(i)==2){
                model.getPlayer().setHP(model.getPlayer().getHP()-1);
                if(model.getPlayer().getHP()<=0){
                    model.setScene(2);
                }
            }
        }
    }

    public void hardupdateCPU(int loop){
        if(loop==0){
            model.clearSpeedY();
            for(int i=0; i<model.getUOUOs().size(); i++){
                model.addSpeedY((Math.random()-0.5));
            }
            Random random=new Random();
            model.setCount(random.nextInt(37)+12);
        }
        for(int i=0; i<model.getUOUOs().size(); i++){
            Cpu u=model.getUOUOs().get(i);
            u.setX(u.getX()+u.getSpeed()*u.getDirection());

            u.setY(u.getY()+u.getSpeed()*model.getSpeedY(i));
            //もし範囲外に言ったら消すように指示
            if(u.getDirection()==-1&&u.getX()<0-u.getWidth()||u.getDirection()==1&&u.getX()>model.getFrameWidth()){
                model.destroyCPU(i);
                model.createCpu();
            }else if(u.getY()<0-u.getHeight()||u.getY()>model.getFrameHeight()){
                model.destroyCPU(i);
                model.createCpu();
            }
            //ヒットフラグ立ってたら消す。
            if(model.checkCollision(i)==1){
                Player player=model.getPlayer();
                player.setPoint((int)(player.getPoint()+model.getUOUO(i).getPoint()));
                model.resizePlayer();
                model.destroyCPU(i);
                model.createCpu();
            }else if(model.checkCollision(i)==2){
                model.getPlayer().setHP(model.getPlayer().getHP()-1);
                System.out.println(model.getPlayer().getHP());
                if(model.getPlayer().getHP()<=0){
                    model.setScene(2);
                }
            }
        }
    }
}


class PlayerController implements KeyListener {
    protected Model model;
    protected View view;
    public PlayerController(Model m, View view) {
        model=m;
        this.view=view;
        this.view.setFocusable(true);
        this.view.addKeyListener(this);
    }
    public void keyPressed(KeyEvent e){
        // カーソルキーのイベントはkeyPressedで取得
        int c=e.getKeyCode();
        Player player=model.getPlayer();
        switch (c) {
            case KeyEvent.VK_LEFT: // ←
            player.setMove(-1, 0);
            break;
            case KeyEvent.VK_RIGHT: // →
            player.setMove(1, 0);
            break;
            case KeyEvent.VK_UP: // ↑
            player.setMove(-1, 1);
            break;
            case KeyEvent.VK_DOWN: // ↓
            player.setMove(1, 1);
            break;
            case KeyEvent.VK_A: // A
            player.setMove(0, -1);
            break;
            case KeyEvent.VK_D: // D
            player.setMove(1, 0);
            break;
            case KeyEvent.VK_W: // W
            player.setMove(-1, 1);
            break;
            case KeyEvent.VK_S: // S
            player.setMove(1, 1);
            break;
        }
    }
    public void keyTyped(KeyEvent e) {}
    public void keyReleased(KeyEvent e){
        int c=e.getKeyCode();
        Player player=model.getPlayer();
        switch (c) {
            case KeyEvent.VK_LEFT: // ←
            if(player.getMove(0)==-1){
                player.setMove(0, 0);
            }
            break;
            case KeyEvent.VK_RIGHT: // →
            if(player.getMove(0)==1){
                player.setMove(0, 0);
            }
            break;
            case KeyEvent.VK_UP: // ↑
            if(player.getMove(1)==-1){
                player.setMove(0, 1);
            }
            break;
            case KeyEvent.VK_DOWN: // ↓
            if(player.getMove(1)==1){
                player.setMove(0, 1);
            }
            break;
            case KeyEvent.VK_A: // A
            if(player.getMove(0)==-1){
                player.setMove(0, 0);
            }
            break;
            case KeyEvent.VK_D: // D
            if(player.getMove(0)==1){
                player.setMove(0, 0);
            }
            break;
            case KeyEvent.VK_W: // W
            if(player.getMove(1)==-1){
                player.setMove(0, 1);
            }
            break;
            case KeyEvent.VK_S: // S
            if(player.getMove(1)==1){
                player.setMove(0, 1);
            }
            break;
        }
    }


    public void action() {
        Player player=model.getPlayer();
        int move_x=player.getMove(0);
        int move_y=player.getMove(1);
        double ratio_x=player.getRatio(0);
        double ratio_y=player.getRatio(1);
        if(move_x==0){
            if(ratio_x>0){
                ratio_x=ratio_x-0.1;
            }else if(ratio_x<0){
                ratio_x=ratio_x+0.1;
            }
            if(Math.abs(ratio_x)<0.1){
                ratio_x=0;
            }

            if(ratio_y>0){
                ratio_y=ratio_y-0.1;
            }else if(ratio_y<0){
                ratio_y=ratio_y+0.1;
            }
            if(Math.abs(ratio_y)<0.1){
                ratio_y=0;
            }
        }
        if(Math.abs(ratio_x)<1.0||ratio_x*(double)move_x<0){
            ratio_x+=0.2*(double)move_x;
        }
        if(Math.abs(ratio_y)<1.0||ratio_y*(double)move_y<0){
            ratio_y+=0.2*(double)move_y;
        }

        if(move_x!=0){
            player.setDirection(move_x);
        }
        double x=player.getX()+ratio_x*player.getSpeed();
        double y=player.getY()+ratio_y*player.getSpeed();
        player.setMove(move_x,0);
        player.setMove(move_y,1);
        player.setRatio(ratio_x,0);
        player.setRatio(ratio_y,1);
        if(x<0){
            player.setX(0.0);
        }else if(x>model.getFrameWidth()-player.getWidth()){
            player.setX(model.getFrameWidth()-player.getWidth());
        }else{
            player.setX(x);
        }
        if(y<0){
            player.setY(0.0);
        }else if(y>model.getFrameHeight()-player.getHeight()){
            player.setY(model.getFrameHeight()-player.getHeight());
        }else{
            player.setY(y);
        }
    }

    public void init(){
        Player player=model.getPlayer();
        player.setMove(0,0);
        player.setMove(0,1);
        player.setRatio(0.0,0);
        player.setRatio(0.0,1);
        this.view.setFocusable(true);
    }
}