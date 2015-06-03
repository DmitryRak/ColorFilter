import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.imageio.ImageIO;


public class WeightedMedian {
    private File outFile;
    private BufferedImage outputImage,inputImage;
    public int imageWidth, imageHeight, xLeftPadding, xRightPadding, yTopPadding, yBottomPadding;
    int MASK_SIZE; //hardcode
    int maskArray[][];

    public WeightedMedian(File inputFile) throws IOException {
        inputImage = ImageIO.read(inputFile);
        outputImage = new BufferedImage(inputImage.getWidth(), inputImage.getHeight(),BufferedImage.TYPE_INT_RGB);
    }

    public void writeNewImage(String filename, String format) throws IOException {
        outFile = new File(filename+"."+format);
        ImageIO.write(outputImage,format, outFile);
    }
    /**
     *
     * @param mask
     * @param maskWidth
     * @param maskHeight
     */
    public void setupWeightMask(int[][] mask, int maskWidth,int maskHeight){
        MASK_SIZE = maskWidth; //hardcode for quadro matrixes
        maskArray = mask;


        System.out.println("Paddings: " + xLeftPadding + ","+ xRightPadding + ","+  yTopPadding + ","+  yBottomPadding);
    }
    public void setupCenterOfTheMask(int rowOfCentralElement, int columnOfCentralElement){
        xLeftPadding = columnOfCentralElement;
        xRightPadding = (MASK_SIZE-1) - columnOfCentralElement;
        yTopPadding = rowOfCentralElement;
        yBottomPadding = (MASK_SIZE-1) - rowOfCentralElement;
    }
    public void makeConversion(){
        imageWidth = inputImage.getWidth();
        imageHeight = inputImage.getHeight();
        int xCoordinate = 0;
        int yCoordinate = 0;
        int xLastPixel = imageWidth - xRightPadding;
        int yLastPixel = imageHeight - yBottomPadding;
        System.out.println("Start coordinates: x: " +xLeftPadding+" y: "+yTopPadding);
        System.out.println("End coordinates: x: " +xLastPixel+" y: "+yLastPixel);
        for(int x = xLeftPadding; x < xLastPixel; x++){
            for(int y = yTopPadding; y < yLastPixel; y++){
                List<Integer> redColorArray = new ArrayList<Integer>();
                List<Integer> greenColorArray = new ArrayList<Integer>();
                List<Integer> blueColorArray = new ArrayList<Integer>();
                for(int maskWidth = 0; maskWidth < MASK_SIZE; maskWidth++){
                    for(int maskheight = 0; maskheight < MASK_SIZE; maskheight++){
                        for(int i = 0; i < maskArray[maskWidth][maskheight]; i++){
                            try{
                                xCoordinate = x-xLeftPadding+maskWidth;
                                yCoordinate = y-yTopPadding+maskheight;
                                redColorArray.add(new Color(inputImage.getRGB(xCoordinate, yCoordinate)).getRed());
                                greenColorArray.add(new Color(inputImage.getRGB(xCoordinate, yCoordinate)).getGreen());
                                blueColorArray.add(new Color(inputImage.getRGB(xCoordinate, yCoordinate)).getBlue());
                            }catch(ArrayIndexOutOfBoundsException ex){
                                ex.printStackTrace();
                                System.out.println("coordinates: x: " +xCoordinate+" y: "+yCoordinate);
                                System.exit(1);
                            }
                        }
                    }
                }
                Collections.sort(redColorArray);
                Collections.sort(greenColorArray);
                Collections.sort(blueColorArray);
                int redMideianColor = redColorArray.get((redColorArray.size()+1)/2);
                int greenMideianColor = greenColorArray.get((greenColorArray.size()+1)/2);
                int blueMideianColor = blueColorArray.get((blueColorArray.size()+1)/2);
                outputImage.setRGB(x, y, new Color(redMideianColor, greenMideianColor, blueMideianColor).getRGB());
            }
        }
        for(int y = 0; y < imageHeight; y++){
            for(int x = 0; x < xLeftPadding; x++){
                outputImage.setRGB(x,y,inputImage.getRGB(x,y));
            }
            for(int x = imageWidth-xRightPadding; x < imageWidth; x++){
                outputImage.setRGB(x,y,inputImage.getRGB(x,y));
            }

        }
        for(int x = xLeftPadding; x < imageWidth-xRightPadding; x++){
            for(int y = 0; y < yTopPadding; y++){
                outputImage.setRGB(x,y,inputImage.getRGB(x,y));
            }
            for(int y = imageHeight-yBottomPadding; y < imageHeight; y++){
                outputImage.setRGB(x,y,inputImage.getRGB(x,y));
            }
        }
    }

    public File getOutFile() {
        return outFile;
    }

}