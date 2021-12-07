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


class Panel1 extends JPanel{
    Image backgroundImage = Toolkit.getDefaultToolkit().createImage("./5000tyoen.jpeg");
    public Panel1() {
        repaint();
    }
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawImage(backgroundImage,0,0,1000,1000,this);
        Graphics2D g2d = (Graphics2D) g;
        try{
            BufferedImage image = ImageIO.read(new File("./uni_cabbage.png"));
            int w = image.getWidth();
            int h = image.getHeight();
            //g2d.drawImage(image,100,100,100+w,100+h,w,0,0,h,this);// drawImage(image,画像を置く左上と右下座標引数4つ,描画する左上と右下相対座標引数4つ,this) 左右反転
            g2d.drawImage(image,500,100,w,h,this);// drawImage(image,縮小した時の左上と右下座標引数4つ,this) 通常
            g2d.drawImage(image,200,200,w+10,h+10,this);// drawImage(image,縮小した時の左上と右下座標引数4つ,this) 通常
            g2d.drawImage(image,600,600,w-200,h-200,this);// drawImage(image,縮小した時の左上と右下座標引数4つ,this) 通常
            //左右反転2
            AffineTransform at = AffineTransform.getScaleInstance(-1d,1d);
            at.translate(-w,0);
            AffineTransformOp atOp = new AffineTransformOp(at,null);
            g2d.drawImage(atOp.filter(image,null), 10, 10, w-100, h-100, this);
            g2d.drawImage(atOp.filter(image,null), 10, 10, w-300, h-300, this);
            g2d.drawImage(atOp.filter(image,null), 10, 600, w-200, h-200, this);
        }catch(IOException e){
            e.printStackTrace();
        }

    }
}

class uouoFrame extends JFrame{
    public uouoFrame(){
        this.setTitle("Panel1");
        this.setSize(1000,1000);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Panel1 panel1 = new Panel1();
        this.add(panel1);
        this.setVisible(true);
    }
    public static void main(String[] args){
        new uouoFrame();
    }
}