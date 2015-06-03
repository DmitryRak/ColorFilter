import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by dmitry on 11/6/14.
 */
public class Convolution {

    private File outFile;
    private BufferedImage outputImage,inputImage;

    public Convolution(File inputFile) throws IOException {
        inputImage = ImageIO.read(inputFile);
        outputImage = new BufferedImage(inputImage.getWidth(), inputImage.getHeight(),BufferedImage.TYPE_INT_RGB);
    }

    public void writeNewImage(String filename, String format) throws IOException {
        outFile = new File(filename+"."+format);
        ImageIO.write(outputImage,format, outFile);
    }

    public void makeConversion(){
        Color[][] newColor = new Color[inputImage.getWidth()-1][inputImage.getHeight()-1];
        for (int x = 1; x <inputImage.getWidth()-1; x++) {
            for (int y = 1; y < inputImage.getHeight()-1; y++) {
                //get the color instance of the pixel
                Color currentPixel = new Color(inputImage.getRGB(x, y));
                Color rightPixel = new Color(inputImage.getRGB(x+1, y));
                Color leftPixel = new Color(inputImage.getRGB(x-1, y));
                Color upPixel = new Color(inputImage.getRGB(x, y+1));
                Color bottomPixel = new Color(inputImage.getRGB(x, y-1));
                int red = currentPixel.getRed()*5 - rightPixel.getRed() - leftPixel.getRed() - upPixel.getRed() - bottomPixel.getRed();
                int green = currentPixel.getGreen()*5 - rightPixel.getGreen() - leftPixel.getGreen() - upPixel.getGreen() - bottomPixel.getGreen();
                int blue = currentPixel.getBlue()*5 - rightPixel.getBlue() - leftPixel.getBlue() - upPixel.getBlue() - bottomPixel.getBlue();
                if(red>255){
                    red = 255;
                }
                if(green>255){
                    green = 255;
                }
                if(blue>255){
                    blue = 255;
                }
                if(red<0){
                    red = 0;
                }
                if(green<0){
                    green = 0;
                }
                if(blue<0){
                    blue = 0;
                }
                newColor[x][y] = new Color(red,green,blue);
            }
        }
        for (int x = 1; x <inputImage.getWidth()-1; x++) {
            for (int y = 1; y < inputImage.getHeight()-1; y++) {
                outputImage.setRGB(x,y,newColor[x][y].getRGB());
            }
        }
    }

    public File getOutFile() {
        return outFile;
    }
}
