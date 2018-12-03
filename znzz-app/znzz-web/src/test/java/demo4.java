import java.applet.Applet;
import java.awt.*;

public class demo4 extends Applet {
    public static void main(String[] args) {

    }

    int x, y;

    public void start() {
        //获取画笔
        Graphics graphics = getGraphics();
        //画x轴、画Y轴
        for (x = 0; x <= 750; x+=1){
            graphics.drawString(".",x,200);
            if (x < 385) graphics.drawString(".",360,x);
        }
        graphics.drawString("Y",330,200);
        //画Y箭头
        for (x = 360; x <= 370; x += 1){
            graphics.drawString(".",x - 10, 375 - x);
            graphics.drawString(".",x,x - 355);
        }
        //画x轴箭头
        graphics.drawString("X",735,230);
        for (x = 740; x <= 750; x+=1){
            graphics.drawString(".",x,x-550);
            graphics.drawString(".",x, 950-x);
        }
        //画cox()曲线
        for (x=0; x<=720;x+=1){
            double a = Math.cos(x*Math.PI/180+Math.PI);
            y=(int)(200 + 80*a);
            graphics.drawString(".",x,y);
        }
    }

}
