import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.*;
import java.awt.*;
import javax.swing.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.util.*;


class UOUOPanel extends JPanel{
    int flag;  //0 = startPanel    1 = playPanel    2 = resultPanel
    BufferedImage backgroundImage;
    protected Model model;
    protected ArrayList<Cpu> Cpu;
    protected ArrayList<String> figures;
    protected int frame_height;
    protected int frame_width;
    protected JButton startButton;
    protected JButton replayButton;
    public UOUOPanel(Model model,int frame_height,int frame_width){
        this.model = model;
        this.frame_height = frame_height;
        this.frame_width = frame_width;
        try{
            this.backgroundImage = ImageIO.read(new File("./background_umi.jpg"));
        }catch(IOException e){
            System.out.println("background image file is not found.");
            e.printStackTrace();
        }
        flag = 0;
        this.setLayout(null);
        repaint();
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

    public void startPanel(Graphics g){     //スタート画面
        g.drawImage(backgroundImage, 0, 0, frame_width, frame_height,this);
        Image buttonicon;
        try{
            buttonicon = ImageIO.read(new File("./start_bottun.png"));
            buttonicon = buttonicon.getScaledInstance(400,300,java.awt.Image.SCALE_SMOOTH);
            startButton = new JButton(new ImageIcon(buttonicon));
            startButton.setContentAreaFilled(false);   //ボタン透過
            startButton.setMargin(new Insets(1,1,1,1));            //ボタンと画像の間の余白
            startButton.setBorderPainted(false);       //境界線消し
            startButton.setFocusPainted(false);        //謎
            startButton.setBounds(350,400,280,140);    //ボタン位置指定 真ん中
            this.add(startButton);                     //ボタン表示
        }catch(IOException e){
            System.out.println("button image file is not found.");
            e.printStackTrace();
        }
    }

    public void playPanel(Graphics g){
        g.drawImage(backgroundImage,0,0,frame_width,frame_height,this);
        Graphics2D g2d = (Graphics2D) g;
        File file;
        Cpu = model.getUOUOs();
        figures = model.getFigures();
        for(Cpu cpu : Cpu){
            try{
                file = new File(figures.get(cpu.getFig()));
                BufferedImage image = ImageIO.read(file);
                int x = (int)cpu.getX();
                int y = (int)cpu.getY();
                int w = (int)cpu.getWidth();
                int h = (int)cpu.getHeight();
                if(cpu.getDirection() == 0){
                    //g2d.drawImage(image,100,100,100+w,100+h,w,0,0,h,this);// drawImage(image,画像を置く左上と右下座標引数4つ,描画する左上と右下相対座標引数4つ,this) 左右反転
                    g2d.drawImage(image,x,y,w,h,this);// drawImage(image,縮小した時の左上と右下座標引数4つ,this) 通常
                }else if (cpu.getDirection() == 1){
                    //左右反転
                    AffineTransform at = AffineTransform.getScaleInstance(-1d,1d);
                    at.translate(-w,0);
                    AffineTransformOp atOp = new AffineTransformOp(at,null);
                    g2d.drawImage(atOp.filter(image,null), x, y, w, h,this);
                }
            }catch(IOException e){
                System.out.println("Character image file is not found.");
                e.printStackTrace();
            }
        }
        Player player = model.getPlayer();
        try{
            file = new File(figures.get(0));
            BufferedImage image = ImageIO.read(file);
            int x = (int)player.getX();
            int y = (int)player.getY();
            int w = (int)player.getWidth();
            int h = (int)player.getHeight();
            if(player.getDirection() == 0){
                g2d.drawImage(image,x,y,w,h,this);// drawImage(image,縮小した時の左上と右下座標引数4つ,this) 通常
            }else if (player.getDirection() == 1){
                //左右反転
                AffineTransform at = AffineTransform.getScaleInstance(-1d,1d);
                at.translate(-w,0);
                AffineTransformOp atOp = new AffineTransformOp(at,null);
                g2d.drawImage(atOp.filter(image,null), x, y, w, h,this);
            }
        }catch(IOException e){
            System.out.println("Player image file is not found.");
            e.printStackTrace();
        }
    }

    public void resultPanel(Graphics g){                    //result画面
        g.drawImage(backgroundImage, 0, 0, frame_width, frame_height,this);
        Image buttonicon;
        try{
            buttonicon = ImageIO.read(new File("./replay_button.png"));
            buttonicon = buttonicon.getScaledInstance(400,300,java.awt.Image.SCALE_SMOOTH);
            replayButton = new JButton(new ImageIcon(buttonicon));
            replayButton.setContentAreaFilled(false);   //ボタン透過
            replayButton.setMargin(new Insets(1,1,1,1));            //ボタンと画像の間の余白
            replayButton.setBorderPainted(false);       //境界線消し
            replayButton.setFocusPainted(false);        //謎
            replayButton.setBounds(350,500,280,140);    //ボタン位置指定 真ん中
            this.add(replayButton);                     //ボタン表示
        }catch(IOException e){
            System.out.println("button image file is not found.");
            e.printStackTrace();
        }
    }

    public void setflag(int flag){
        this.flag = flag;
    }


    public JButton getStartButton(){
        return startButton;
    }


    public JButton getReplayButton(){
        return replayButton;
    }
}

class UOUOFrame extends JFrame{          //show window
    UOUOPanel panel;
    private int frame_height = 1000;
    private int frame_width = 1000;
    public UOUOFrame(Model model){
        this.setTitle("UOUO! Dream Fish!!");
        this.setSize(frame_height, frame_width);
        panel = new UOUOPanel(model,frame_height,frame_width);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.add(panel);
        this.setVisible(true);
    }

    public UOUOPanel getPanel(){
        return panel;
    }

    public int getFrameHeight(){
        return frame_height;
    }

    public int getFrameWidth(){
        return frame_width;
    }
}