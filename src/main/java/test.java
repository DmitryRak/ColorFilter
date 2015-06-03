import java.awt.*;

/**
 * Created by dmitry on 1/17/15.
 */
public class test {
    public static void main(String[] args) {
        /*
         Color red = new Color(255,0,0);
        Color orange = new Color(255,128,0);
        Color yellow = new Color(255,255,0);
        Color green = new Color(0,255,0);
        Color lightblue = new Color(0,128,255);
        Color blue = new Color(0,0,255);
        Color purple = new Color(128,0,255);
        Color white = new Color(255,255,255);
        Color black = new Color(0,0,0);
         */
        int r = 0;
        int g = 255;
        int b = 255;

        double[]lala = new double[3];
        CIELab.rgb2lab(r,g,b,lala);
        System.out.println(lala[0]+" "+lala[1]+" "+lala[2]);
        System.out.println(r+" "+g+" "+b);
    }
}
