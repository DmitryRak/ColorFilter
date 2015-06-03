import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by dmitry on 11/6/14.
 */
public class ExpandContrast {

    private File outFile;
    private BufferedImage outputImage,inputImage;
    private ArrayList<Color> colors;

    public ExpandContrast(File inputFile, ArrayList<Color> colorRGB) throws IOException {
        inputImage = ImageIO.read(inputFile);
        outputImage = new BufferedImage(inputImage.getWidth(), inputImage.getHeight(),BufferedImage.TYPE_INT_RGB);
        colors = colorRGB;
    }

    public void writeNewImage(String filename, String format) throws IOException {
        outFile = new File(filename+"."+format);
        ImageIO.write(outputImage,format, outFile);
    }

    public void makeConversion(){
        //loop for each pixel of the image
        int redmax = 0;
        int redmin = 255;
        int greemax = 0;
        int greenmin = 255;
        int bluemax = 0;
        int bluemin = 255;
        for (int x = 0; x <inputImage.getWidth(); x++) {
            for (int y = 0; y < inputImage.getHeight(); y++) {


                //get the color instance of the pixel
                Color c = new Color(inputImage.getRGB(x, y));
                int red = c.getRed();
                int green = c.getGreen();
                int blue = c.getBlue();
                if(red>redmax){
                    redmax = red;
                }
                if(red<redmin){
                    redmin = red;
                }
                if(green>greemax){
                    greemax = green;
                }
                if(green<greenmin){
                    greenmin = green;
                }
                if(blue>bluemax){
                    bluemax = blue;
                }
                if(blue<bluemin){
                    bluemin = blue;
                }

            }
        }
        for (int x = 0; x <inputImage.getWidth(); x++) {
            for (int y = 0; y < inputImage.getHeight(); y++) {

                //magic is here
                Color c = new Color(inputImage.getRGB(x, y));
                int currentRed = c.getRed();
                int newRed = 255 * (currentRed - redmin)/(redmax-redmin);
                int currentGreen = c.getGreen();
                int newGreen = 255 * (currentGreen - greenmin)/(greemax-greenmin);
                int currentBlue = c.getBlue();
                int newBlue = 255 * (currentBlue - bluemin)/(bluemax-bluemin);

                outputImage.setRGB(x, y, new Color(newRed,newGreen,newBlue).getRGB());
            }
        }
    }

    public File getOutFile() {
        return outFile;
    }
}
