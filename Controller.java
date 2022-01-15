import javax.swing.*;
import java.awt.event.*;
import java.util.*;

class Main{
    public static void main(String argv[]) {
        Model model=new Model();
        View view=new View(model);
        AllController controller =new AllController(model,view);
    }
}

class AllController implements ActionListener {
    protected Model model;
    protected CPUController cpu;
    //hard用
    //protected HardCPUController cpu;
    //-----
    protected PlayerController player;
    protected View view;
    private javax.swing.Timer timer;
    protected JButton start;
    protected JButton replay;
    //protected int count,loop;
    public AllController(Model model, View view) {
        this.model = model;
        this.view = view;
        //初期化はモデル
        start=view.getPanel().getStartButton();
        replay=view.getPanel().getReplayButton();
        start.addActionListener(this);
        replay.addActionListener(this);
        timer = new javax.swing.Timer(50, this);
        //hard用
        //loop=0;
        //-----
        timer.start();
    }

    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==timer){
            if(model.getScene()==1){
                model.setTime(model.getTime()+1);
                int time=model.getTime();
                if(time==model.getMaxTime()){
                    model.setScene(2);
                }else{
                    player.action();
                    //hard用
                    if(time<model.getMaxTime()/2){
                        cpu.updateCPU();
                    }else{
                        int loop=model.getLoop();
                        cpu.hardupdateCPU(loop++); //同期しないがいいかも?
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
            //runtime();
            view.getPanel().repaint(); //fps25で更新
            this.view.setFocusable(true);
            //System.out.println(model.getPlayer().getX()+" "+model.getPlayer().getY());
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
            }/*else{
                model.setScene(model.getScene()+1);
                view.getPanel().setflag(model.getScene());
                //view.repaint();
            }*/
            /*if(model.getScene()==2){
                //timer.stop();
            }*/
        }
    }
}


class CPUController{
    protected Model model;
    //protected ArrayList<UOUOcpu> cpuList;
    //protected ArrayList<Double> cpuyspeed;
    //int count;
    public CPUController(Model m) {
        model=m;
        //count = 0;
        //cpuyspeed=new ArrayList<Double>();
    }
    public void updateCPU(){
        for(int i=0; i<model.getUOUOs().size(); i++){
            //System.out.println(model.getUOUOs().get(i).getX());
            model.getUOUOs().get(i).setX(model.getUOUOs().get(i).getX()+model.getUOUOs().get(i).getSpeed()*model.getUOUOs().get(i).getDirection());
            //System.out.println(model.getUOUOs().get(i).getX());
            //もし範囲外に言ったら消すように指示
            Cpu u=model.getUOUOs().get(i);
            if(u.getDirection()==-1&&u.getX()<0-u.getWidth()||u.getDirection()==1&&u.getX()>model.getFrameWidth()){
                model.destroyCPU(i);
                model.createCpu();
            }
            //ヒットフラグ立ってたら消す。
            if(model.checkCollision(i)==1){
                Player player=model.getPlayer();
                player.setPoint((int)(player.getPoint()+model.getUOUO(i).getPoint()));
                model.destroyCPU(i);
                model.createCpu();
            }else if(model.checkCollision(i)==2){
                model.getPlayer().setHP(model.getPlayer().getHP()-1);
                //System.out.println(model.getPlayer().getHP());
                if(model.getPlayer().getHP()<=0){
                    model.setScene(2);
                }
            }
            //消した時に追加
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
            //System.out.println(model.getUOUOs().get(i).getX());
            model.getUOUOs().get(i).setX(model.getUOUOs().get(i).getX()+model.getUOUOs().get(i).getSpeed()*model.getUOUOs().get(i).getDirection()*Math.random());

            model.getUOUOs().get(i).setY(model.getUOUOs().get(i).getY()+model.getUOUOs().get(i).getSpeed()*model.getSpeedY(i));
            //System.out.println(model.getUOUOs().get(i).getX());
            //もし範囲外に言ったら消すように指示
            Cpu u=model.getUOUOs().get(i);
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
    protected int[] move;
    public PlayerController(Model m, View view) {
        move=new int[]{0,0};
        model=m;
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
            move[0]=-1;
            //model.setX(model.getX()-model.getSpeed());
            //System.out.println("aaa");
            break;
            case KeyEvent.VK_RIGHT: // D or →
            move[0]=1;
            //model.setX(model.getX()+model.getSpeed());
            //System.out.println("aaa");
            break;
            case KeyEvent.VK_UP: // W or ↑
            move[1]=-1;
            //model.setY(model.getY()-model.getSpeed());
            //System.out.println("aaa");
            break;
            case KeyEvent.VK_DOWN: // S
            move[1]=1;
            //model.setY(model.getY()+model.getSpeed());
            //System.out.println("aaa");
            break;
            case KeyEvent.VK_A: // A or ←
            move[0]=-1;
            //model.setX(model.getX()-model.getSpeed());
            //System.out.println("aaa");
            break;
            case KeyEvent.VK_D: // D or →
            move[0]=1;
            //model.setX(model.getX()+model.getSpeed());
            //System.out.println("aaa");
            break;
            case KeyEvent.VK_W: // W or ↑
            move[1]=-1;
            //model.setY(model.getY()-model.getSpeed());
            //System.out.println("aaa");
            break;
            case KeyEvent.VK_S: // S
            move[1]=1;
            //model.setY(model.getY()+model.getSpeed());
            //System.out.println("aaa");
            break;
        }
    }
    public void keyTyped(KeyEvent e) {}
    public void keyReleased(KeyEvent e){
        int c=e.getKeyCode();
        switch (c) {
            case KeyEvent.VK_LEFT: // A or ←
            if(move[0]==-1){
                move[0]=0;
            }
            //model.setX(model.getX()-model.getSpeed());
            //System.out.println("aaa");
            break;
            case KeyEvent.VK_RIGHT: // D or →
            if(move[0]==1){
                move[0]=0;
            }
            //model.setX(model.getX()+model.getSpeed());
            //System.out.println("aaa");
            break;
            case KeyEvent.VK_UP: // W or ↑
            if(move[1]==-1){
                move[1]=0;
            }
            //model.setY(model.getY()-model.getSpeed());
            //System.out.println("aaa");
            break;
            case KeyEvent.VK_DOWN: // S
            if(move[1]==1){
                move[1]=0;
            }
            //model.setY(model.getY()+model.getSpeed());
            //System.out.println("aaa");
            break;
            case KeyEvent.VK_A: // A or ←
            if(move[0]==-1){
                move[0]=0;
            }
            //model.setX(model.getX()-model.getSpeed());
            //System.out.println("aaa");
            break;
            case KeyEvent.VK_D: // D or →
            if(move[0]==1){
                move[0]=0;
            }
            //model.setX(model.getX()+model.getSpeed());
            //System.out.println("aaa");
            break;
            case KeyEvent.VK_W: // W or ↑
            if(move[1]==-1){
                move[1]=0;
            }
            //model.setY(model.getY()-model.getSpeed());
            //System.out.println("aaa");
            break;
            case KeyEvent.VK_S: // S
            if(move[1]==1){
                move[1]=0;
            }
            //model.setY(model.getY()+model.getSpeed());
            //System.out.println("aaa");
            break;
        }
    }


    public void action() {
        Player player=model.getPlayer();
        if(move[0]!=0){
            player.setDirection(move[0]);
        }
        double x=player.getX()+move[0]*player.getSpeed();
        double y=player.getY()+move[1]*player.getSpeed();
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
        move[0]=0;
        move[1]=0;
        this.view.setFocusable(true);
        //System.out.println(view.isFocusable());
    }
}