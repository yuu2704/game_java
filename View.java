import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.awt.Image;
import java.awt.Toolkit;
import javax.imageio.*;
import javax.swing.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.util.*;


class UOUOPanel extends JPanel{
    protected Image backgroundImage = Toolkit.getDefaultToolkit().createImage("./5000tyoen.jpeg");
    protected UOUOModel model;
    protected ArrayList<UOUOcpu> uouoCpu;
    protected ArrayList<String> uouoFigures;
    public UOUOPanel(UOUOModel model){
        this.model = model;
        repaint();
    }
    @Override
    public void paintComponent(Graphics g){     //paint method
        super.paintComponent(g);
        g.drawImage(backgroundImage,0,0,1000,1000,this);
        Graphics2D g2d = (Graphics2D) g;
        File file;
        uouoCpu = model.getUOUOs();
        uouoFigures = model.getFigures();
        for(UOUOcpu cpu : uouoCpu){
            try{
                file = new File(uouoFigures.get(cpu.getFig()));
                BufferedImage image = ImageIO.read(file);
                int x = (int)cpu.getX();
                int y = (int)cpu.getY();
                int w = (int)cpu.getWidth();
                int h = (int)cpu.getHeight();
                if(cpu.getDirection() == 1){
                    //g2d.drawImage(image,100,100,100+w,100+h,w,0,0,h,this);// drawImage(image,画像を置く左上と右下座標引数4つ,描画する左上と右下相対座標引数4つ,this) 左右反転
                    g2d.drawImage(image,x,y,w,h,this);// drawImage(image,縮小した時の左上と右下座標引数4つ,this) 通常
                }else if (cpu.getDirection() == 0){
                    //左右反転2
                    AffineTransform at = AffineTransform.getScaleInstance(-1d,1d);
                    at.translate(-w,0);
                    AffineTransformOp atOp = new AffineTransformOp(at,null);
                    g2d.drawImage(atOp.filter(image,null), x, y, w, h,this);
                }
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }
}

class UOUOFrame extends JFrame{          //show window
    UOUOModel model;
    UOUOPanel panel;
    private int frame_height = 1000;
    private int frame_width = 1000;
    public UOUOFrame(){
        this.setTitle("Panel1");
        this.setSize(frame_height, frame_width);
        model = new UOUOModel();
        panel = new UOUOPanel(model);
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