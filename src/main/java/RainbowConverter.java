import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by dmitry on 11/6/14.
 */
public class RainbowConverter {

    private File outFile;
    private BufferedImage outputImage,inputImage;
    private ArrayList<Color> colors;

    public RainbowConverter(File inputFile, ArrayList<Color> colorRGB) throws IOException {
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
        for (int x = 0; x <inputImage.getWidth(); x++) {
            for (int y = 0; y < inputImage.getHeight(); y++) {
                //get the color instance of the pixel
                Color c = new Color(inputImage.getRGB(x, y));
                double[] lab1 = new double[3];
                //Convert RGB values of the color to CIELab and put into lab1 array
                CIELab.rgb2lab(c.getRed(), c.getGreen(), c.getBlue(), lab1);
                double minDelta = 9999999999999d;
                int minColorIndex = 0;
                //for each color of the rainbow
                for(int i =0;i<colors.size();i++){
                    double[] lab2 = new double[3];
                    //convert RGB values of the color of the rainbow to CIELab and put into lab2 array
                    CIELab.rgb2lab(colors.get(i).getRed(), colors.get(i).getGreen(), colors.get(i).getBlue(), lab2);
                    //calculate deltaE for lab1 and lab2 arrays
                    double deltaE = Math.abs(CIELab.calculateDeltaE(lab1, lab2));
                    if(deltaE<minDelta){
                        minDelta = deltaE;
                        minColorIndex = i;
                    }
                }
                outputImage.setRGB(x, y, colors.get(minColorIndex).getRGB());
            }
        }
    }

    public File getOutFile() {
        return outFile;
    }
}
