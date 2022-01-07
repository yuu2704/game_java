import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.plaf.ProgressBarUI;
import java.util.*;

class UOUOPanel extends JPanel{
    int flag;  //0 = startPanel    1 = playPanel    2 = resultPanel
    protected Model model;
    protected ArrayList<Cpu> Cpu;
    protected Player player;
    protected int maxHP;
    protected int maxTime;
    protected ArrayList<String> figures;
    protected int frame_width;
    protected int frame_height;
    protected BufferedImage backgroundImage;

    protected JButton startButton;
    protected JButton replayButton;
    protected JLabel scorelabel;
    protected JLabel score_num_Label;
    protected JLabel resultscoreLabel;
    protected JProgressBar hpbar;
    protected JProgressBar timebar;

    public UOUOPanel(Model model,int frame_width,int frame_height){
        this.model = model;
        this.frame_width = frame_width;
        this.frame_height = frame_height;
        this.setLayout(null);
        flag = 0;
        setLAF();
        createLabelandButton();
        initScene();
        repaint();
    }

    protected void setLAF(){
        try{
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        }catch(Exception e){
            System.out.println("LAF Metal is not found.");
            e.printStackTrace();
        }
    }

    public void paintComponent(Graphics g){     //paint method
        super.paintComponent(g);
        if(flag == 0){
            startPanel(g);
        }else if(flag == 1){
            playPanel(g);
        }else if(flag == 2){
            resultPanel(g);
        }
    }

    public void createLabelandButton(){      //使うボタンを全部生成
        //背景
        try{
            this.backgroundImage = ImageIO.read(new File("./background_umi.jpg"));
        }catch(IOException e){
            System.out.println("background image file is not found.");
            e.printStackTrace();
        }
        //スタートボタン
        Image startbuttonicon;
        try{
            startbuttonicon = ImageIO.read(new File("./start_bottun.png"));
            startbuttonicon = startbuttonicon.getScaledInstance(400,300,java.awt.Image.SCALE_SMOOTH);
            startButton = new JButton(new ImageIcon(startbuttonicon));
            startButton.setContentAreaFilled(false);   //ボタン透過
            startButton.setMargin(new Insets(1,1,1,1));            //ボタンと画像の間の余白
            startButton.setBorderPainted(false);       //境界線消し
            startButton.setFocusPainted(false);        //謎
            startButton.setBounds(350,400,280,140);    //ボタン位置指定 真ん中
        }catch(IOException e){
            System.out.println("button image file is not found.");
            e.printStackTrace();
        }
        this.add(startButton);
        //途中スコア
        score_num_Label = new JLabel();
        score_num_Label.setFont(new Font(Font.SANS_SERIF,Font.BOLD,120));
        score_num_Label.setBounds(0,frame_height-100,400,80);
        this.add(score_num_Label);
        //HPbar
        hpbar = new JProgressBar();
        hpbar.setForeground(Color.green);
        hpbar.setBackground(Color.white);
        hpbar.setStringPainted(true);
        hpbar.setBorderPainted(true);
        hpbar.setValue(100);
        hpbar.setFont(new Font(Font.SANS_SERIF,Font.BOLD,20));
        hpbar.setBounds(frame_width-300,frame_height-100,200,40);
        this.add(hpbar);
        //timebar
        timebar = new JProgressBar();
        timebar.setForeground(Color.black);
        timebar.setBackground(Color.white);
        timebar.setBorderPainted(true);
        timebar.setValue(100);
        timebar.setBounds(frame_width/4,0,frame_width/2,40);
        this.add(timebar);
        //"スコア"ラベル
        scorelabel = new JLabel("スコア");
        scorelabel.setFont(new Font(Font.SANS_SERIF,Font.BOLD,120));
        scorelabel.setBounds(330,100,400,140);    //ボタン位置指定 真ん中
        this.add(scorelabel);
        //リザルトスコア
        resultscoreLabel = new JLabel();
        resultscoreLabel.setFont(new Font(Font.SANS_SERIF,Font.BOLD,120));
        resultscoreLabel.setBounds(330,300,400,140);    //ボタン位置指定 真ん中
        this.add(resultscoreLabel);
        //リプレイボタン
        Image replaybuttonicon;
        try{
            replaybuttonicon = ImageIO.read(new File("./replay_button.png"));
            replaybuttonicon = replaybuttonicon.getScaledInstance(400,300,java.awt.Image.SCALE_SMOOTH);
            replayButton = new JButton(new ImageIcon(replaybuttonicon));
            replayButton.setContentAreaFilled(false);   //ボタン透過
            replayButton.setMargin(new Insets(1,1,1,1));            //ボタンと画像の間の余白
            replayButton.setBorderPainted(false);       //境界線消し
            replayButton.setFocusPainted(false);        //謎
            replayButton.setBounds(350,500,280,140);    //ボタン位置指定 真ん中
        }catch(IOException e){
            System.out.println("button image file is not found.");
            e.printStackTrace();
        }
        this.add(replayButton);
    }

    public void initScene(){
        if(flag == 0){
            scorelabel.setVisible(false);
            resultscoreLabel.setVisible(false);
            replayButton.setEnabled(false);
            replayButton.setVisible(false);
            hpbar.setVisible(false);
            timebar.setVisible(false);

            startButton.setEnabled(true);
            startButton.setVisible(true);
        }else if(flag == 1){
            startButton.setEnabled(false); 
            startButton.setVisible(false);
            scorelabel.setVisible(false);
            resultscoreLabel.setVisible(false);
            replayButton.setEnabled(false);
            replayButton.setVisible(false);

            score_num_Label.setVisible(true);
            hpbar.setVisible(true);
            timebar.setVisible(true);

            player = model.getPlayer();
            maxHP = player.getHP();
            maxTime = model.getMaxTime();
        }else if(flag == 2){
            startButton.setEnabled(false); 
            startButton.setVisible(false);
            score_num_Label.setVisible(false);
            hpbar.setVisible(false);
            timebar.setVisible(false);

            scorelabel.setVisible(true);
            resultscoreLabel.setVisible(true);
            replayButton.setVisible(true);
            replayButton.setEnabled(true);
        }
    }

    public void startPanel(Graphics g){     //スタート画面
        g.drawImage(backgroundImage, 0, 0, frame_width, frame_height,this);
    }

    public void playPanel(Graphics g){

        g.drawImage(backgroundImage,0,0,frame_width,frame_height,this);
        Graphics2D g2d = (Graphics2D) g;
        File file;
        Cpu = model.getUOUOs();
        figures = model.getFigures();
        score_num_Label.setText((int)player.getPoint() + "");
        hpbar.setValue(player.getHP()*100/maxHP);
        hpbar.setString(player.getHP() + "/" + maxHP);
        timebar.setValue((maxTime - model.getTime())*100/maxTime);
        for(Cpu cpu : Cpu){
            try{
                file = new File(figures.get(cpu.getFig()));
                BufferedImage image = ImageIO.read(file);
                int x = (int)cpu.getX();
                int y = (int)cpu.getY();
                int w = (int)cpu.getWidth();
                int h = (int)cpu.getHeight();
                if(cpu.getDirection() == -1){
                    //g2d.drawImage(image,100,100,100+w,100+h,w,0,0,h,this);// drawImage(image,画像を置く左上と右下座標引数4つ,描画する左上と右下相対座標引数4つ,this) 左右反転
                    g2d.drawImage(image,x,y,w,h,this);// drawImage(image,縮小した時の左上と右下座標引数4つ,this) 通常
                }else if (cpu.getDirection() == 1){
                    g2d.drawImage(image,x+w,y,-w,h,this);
                }
            }catch(IOException e){
                System.out.println("Character image file is not found.");
                e.printStackTrace();
            }
        }
        try{
            file = new File(figures.get(0));
            BufferedImage image = ImageIO.read(file);
            int x = (int)player.getX();
            int y = (int)player.getY();
            int w = (int)player.getWidth();
            int h = (int)player.getHeight();
            if(player.getDirection() == -1){
                g2d.drawImage(image,x,y,w,h,this);// drawImage(image,縮小した時の左上と右下座標引数4つ,this) 通常
            }else if (player.getDirection() == 1){
                g2d.drawImage(image,x+w,y,-w,h,this);
            }
        }catch(IOException e){
            System.out.println("Player image file is not found.");
            e.printStackTrace();
        }
    }

    public void resultPanel(Graphics g){                    //result画面
        g.drawImage(backgroundImage, 0, 0, frame_width, frame_height,this);
        resultscoreLabel.setText(player.getPoint() + "");
    }

    public void setflag(int flag){
        this.flag = flag;
        initScene();
    }


    public JButton getStartButton(){
        return startButton;
    }


    public JButton getReplayButton(){
        return replayButton;
    }
}

class View extends JFrame{          //show window
    UOUOPanel panel;
    private int frame_width;
    private int frame_height;
    public View(Model model){
        this.setTitle("UOUO! Dream Fish!!");
        frame_width = model.getFrameWidth();
        frame_height = model.getFrameHeight();
        this.setSize(frame_width, frame_height);
        panel = new UOUOPanel(model,frame_width,frame_height);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.add(panel);
        this.setVisible(true);
    }

    public UOUOPanel getPanel(){
        return panel;
    }
}