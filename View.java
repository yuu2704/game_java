import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.*;
import java.awt.*;
import javax.swing.*;
import java.util.*;
import javax.swing.border.*;
import javax.sound.sampled.*;

class UOUOPanel extends JPanel{
    int flag;  //0 = startPanel    1 = playPanel    2 = resultPanel
    protected Model model;
    protected Sounds sound;
    protected ArrayList<Cpu> Cpu;
    protected Player player;
    protected int maxHP;
    protected int maxTime;
    protected ArrayList<String> figures;
    protected int frame_width;
    protected int frame_height;
    protected BufferedImage backgroundImage;
    protected BufferedImage startbackgroundImage;

    protected JButton startButton;
    protected JButton replayButton;
    protected JLabel scorelabel;
    protected JLabel score_num_Label;
    protected JLabel resultscoreLabel;
    protected JProgressBar hpbar;
    protected JProgressBar timebar;

    public UOUOPanel(Model model,Sounds sound,int frame_width,int frame_height){
        this.model = model;
        this.sound = sound;
        this.frame_width = frame_width;
        this.frame_height = frame_height;
        this.setLayout(null);
        flag = 0;
        setLAF();
        createLabelandButton();
        initScene();
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
        //スタート画面背景
        try{
            this.startbackgroundImage = ImageIO.read(new File("./UOUO_title.png"));
        }catch(IOException e){
            System.out.println("startbackground image file is not found.");
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
        score_num_Label.setFont(new Font("Silom",Font.BOLD,80));
        score_num_Label.setHorizontalAlignment(JLabel.CENTER);
        score_num_Label.setBounds(0,frame_height-100,200,80);
        this.add(score_num_Label);
        //HPbar
        hpbar = new JProgressBar();
        hpbar.setForeground(Color.green);
        hpbar.setBackground(Color.white);
        hpbar.setStringPainted(true);
        hpbar.setBorderPainted(true);
        LineBorder border = new LineBorder(Color.black, 1, true);
        hpbar.setBorder(border);
        hpbar.setValue(100);
        hpbar.setFont(new Font("Silom",Font.BOLD,20));
        hpbar.setBounds(frame_width-300,frame_height-70,280,40);
        this.add(hpbar);
        //timebar
        timebar = new JProgressBar();
        timebar.setForeground(Color.blue);
        timebar.setBackground(Color.white);
        timebar.setBorderPainted(true);
        timebar.setBorder(border);
        timebar.setValue(100);
        timebar.setBounds(frame_width/4,0,frame_width/2,40);
        this.add(timebar);
        //"スコア"ラベル
        scorelabel = new JLabel("Score");
        scorelabel.setFont(new Font("Silom", Font.BOLD,180));
        scorelabel.setHorizontalAlignment(JLabel.CENTER);
        scorelabel.setBounds(frame_width/2-300,frame_height/2-350,600,200);    //ボタン位置指定 真ん中
        this.add(scorelabel);
        //リザルトスコア
        resultscoreLabel = new JLabel();
        resultscoreLabel.setFont(new Font("Silom",Font.BOLD,180));
        resultscoreLabel.setHorizontalAlignment(JLabel.CENTER);
        resultscoreLabel.setBounds(frame_width/2-300,frame_height/2-90,600,180);
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
            replayButton.setBounds(frame_width/2-150,frame_height/2+160,280,140);    //ボタン位置指定 真ん中
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
            
            sound.soundPlay(0);
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
            maxHP = player.getMaxHP();
            maxTime = model.getMaxTime();

            sound.soundClose();
            sound.soundPlay(5);
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

            sound.soundClose();
            if(player.getPoint() < 70){
                sound.sePlay(2);
            }else{
                sound.sePlay(3);
            }
        }
    }

    public void startPanel(Graphics g){     //スタート画面
        g.drawImage(startbackgroundImage, 0, 0, frame_width, frame_height,this);
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
        resultscoreLabel.setText((int)player.getPoint() + "");
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

class Sounds {
    protected ArrayList<String> sounds;
    Clip clip_bgm;
    Clip clip_se;
    public Sounds(){
        sounds = new ArrayList<String>();
        sounds.add("uouo_start.wav");
        sounds.add("eat.wav");
        sounds.add("ti-n.wav");
        sounds.add("levelup.wav");
        sounds.add("heavypunch.wav");
        sounds.add("uouo_bgm.wav"); 
    }

    public void soundPlay(int idx){
        AudioInputStream ais_bgm = null;
        try {
            ais_bgm = AudioSystem.getAudioInputStream(new File(sounds.get(idx)));
            AudioFormat af = ais_bgm.getFormat();
            DataLine.Info info = new DataLine.Info(Clip.class, af);
            clip_bgm = (Clip)AudioSystem.getLine(info);
            clip_bgm.open(ais_bgm);
            clip_bgm.loop(10);
            clip_bgm.flush();
            clip_bgm.setFramePosition(0);
        }catch(UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void sePlay(int idx){
        AudioInputStream ais_se = null;
        try {
            ais_se = AudioSystem.getAudioInputStream(new File(sounds.get(idx)));
            AudioFormat af = ais_se.getFormat();
            DataLine.Info info = new DataLine.Info(Clip.class, af);
            clip_se = (Clip)AudioSystem.getLine(info);
            clip_se.open(ais_se);
            FloatControl control = (FloatControl)clip_se.getControl(FloatControl.Type.MASTER_GAIN);
            control.setValue((float) Math.log10(0.05) * 20);
            clip_se.loop(0);
            clip_se.flush();
        }catch(UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }finally {
            try {
                ais_se.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void soundClose(){
        clip_bgm.stop();
        clip_bgm.close();
    }

    public void seClose(){
        clip_se.stop();
        clip_se.close();
    }
}

class View extends JFrame{          //show window
    UOUOPanel panel;
    private int frame_width;
    private int frame_height;
    public View(Model model,Sounds sound){
        this.setTitle("UOUO! Dream Fish!!");
        frame_width = model.getFrameWidth();
        frame_height = model.getFrameHeight();
        this.getContentPane().setPreferredSize(new Dimension(frame_width, frame_height));
        panel = new UOUOPanel(model,sound,frame_width,frame_height);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.add(panel);
        this.pack();
        this.setVisible(true);
    }

    public UOUOPanel getPanel(){
        return panel;
    }
}