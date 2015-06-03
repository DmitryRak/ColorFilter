import com.opencsv.CSVReader;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

/**
 * Created by dmitry on 11/6/14.
 */
public class ConverterGUI implements ActionListener{
    private BufferedImage inputImage, outputImage;
    private JButton openImageButton;
    private JButton viewButton, chooseFileButton;
    private JPanel containerPanel;
    private JLabel inputFile, outputFile;
    private JButton conversionButton;
    private JComboBox rowIndex, columnIndex;
    private WeightedMedian converter;
    private static final int  MAXIMUM_MATRIX_SIZE = 21;
    private int actualMaskSize;
    private ArrayList<Color> rainbowColors;
    public ConverterGUI() {

        JFrame frame = new JFrame("ImageConverter");
        rainbowColors = new ArrayList<Color>();

        Color red = new Color(255,0,0);
        Color orange = new Color(255,128,0);
        Color yellow = new Color(255,255,0);
        Color green = new Color(0,200,0);
        Color lightblue = new Color(0,255,255);
        Color blue = new Color(0,0,255);
        Color purple = new Color(128,0,255);
        Color white = new Color(255,255,255);
        Color black = new Color(0,0,0);
        rainbowColors.add(red);
        rainbowColors.add(orange);
        rainbowColors.add(yellow);
        rainbowColors.add(green);
        rainbowColors.add(lightblue);
        rainbowColors.add(blue);
        rainbowColors.add(purple);
        rainbowColors.add(white);
        rainbowColors.add(black);

        openImageButton.addActionListener(this);
        conversionButton.addActionListener(this);
        viewButton.addActionListener(this);
        chooseFileButton.addActionListener(this);
        rowIndex.addActionListener(this);
        columnIndex.addActionListener(this);
        frame.setContentPane(containerPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setDefaultLookAndFeelDecorated(true);
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        //if Open Image button is clicked
        if(actionEvent.getSource().equals(openImageButton)){
            //FileNameExtensionFilter filter = new FileNameExtensionFilter("BMP Files", "bmp");
            JFileChooser fileOpenDialog = new JFileChooser();
            fileOpenDialog.setFileFilter(new FileNameExtensionFilter("Image files", "jpg", "png", "gif", "bmp"));
           // fileOpenDialog.setFileFilter(filter);
            int ret = fileOpenDialog.showDialog(null, "Открыть файл");
            if (ret == JFileChooser.APPROVE_OPTION) {
                File file = fileOpenDialog.getSelectedFile();
                try {
                    converter = new WeightedMedian(file);
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null, "Конвертер был создан с ошибками", "Ошибка: " + e.getClass(), JOptionPane.INFORMATION_MESSAGE);
                }
                try {
                    inputImage = ImageIO.read(file);
                    System.out.println("File is read as image");
                    if(inputImage.getWidth() > 500 | inputImage.getHeight() > 500){
                        inputFile.setText("Размеры входящего файла слишком велики для показа в панели");
                    }else{
                        inputFile.setText("");
                        inputFile.setIcon(new ImageIcon(file.getAbsolutePath()));
                        inputFile.repaint();
                        System.out.println("File is read as image");
                    }
                } catch (IOException e) {

                    e.printStackTrace();
                }


            }
        //if Conversion button is clicked
        }else if(actionEvent.getSource().equals(chooseFileButton)){
            JFileChooser fileOpenDialog = new JFileChooser();
            fileOpenDialog.setFileFilter(new FileNameExtensionFilter("CSV files", "csv"));
            int ret = fileOpenDialog.showDialog(null, "Открыть файл");
            if (ret == JFileChooser.APPROVE_OPTION) {
                File file = fileOpenDialog.getSelectedFile();
                CSVReader reader = null;
                try {
                    reader = new CSVReader(new FileReader(file));

                    List<String[]> myEntries = reader.readAll();
                    rowIndex.removeAllItems();
                    columnIndex.removeAllItems();
                    actualMaskSize = myEntries.size();
                    if(actualMaskSize >= MAXIMUM_MATRIX_SIZE){
                        actualMaskSize = MAXIMUM_MATRIX_SIZE;
                    }
                    int[][] matrix = new int[actualMaskSize][actualMaskSize];
                    for(int i = 0; i < actualMaskSize; i++){
                        rowIndex.addItem(i+1);
                        for(int j = 0; j < actualMaskSize; j++){
                            matrix[i][j] = Integer.parseInt(myEntries.get(i)[j]);
                            if(i==0)
                                columnIndex.addItem(j+1);
                        }
                    }
                    converter.setupWeightMask(matrix, actualMaskSize, actualMaskSize);
                    rowIndex.setSelectedIndex(actualMaskSize / 2);
                    columnIndex.setSelectedIndex(actualMaskSize/2);
                    converter.setupCenterOfTheMask(actualMaskSize/2, actualMaskSize/2);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else if(actionEvent.getSource().equals(rowIndex) || actionEvent.getSource().equals(columnIndex)){
            converter.setupCenterOfTheMask(rowIndex.getSelectedIndex(), rowIndex.getSelectedIndex());
        } else if(actionEvent.getSource().equals(conversionButton)){
            try {
                converter.makeConversion();
                SimpleDateFormat sdf = new SimpleDateFormat("mm_ss");
                converter.writeNewImage(sdf.format(new Date())+"rainbow","PNG");
                try {
                    outputImage = ImageIO.read(converter.getOutFile());
                    if(outputImage.getWidth() > 500 | outputImage.getHeight() > 500){
                        outputFile.setText("Размеры исходного файла слишком велики для показа в панели");

                    }else{
                        outputFile.setText("");
                        outputFile.setIcon(new ImageIcon(converter.getOutFile().getAbsolutePath()));
                        outputFile.repaint();
                        System.out.println("new iage was set");
                    }
                } catch (IOException e) {

                    e.printStackTrace();
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Ошибка создания конечного файла", "InfoBox: ", JOptionPane.INFORMATION_MESSAGE);
            } catch (NullPointerException e){
                JOptionPane.showMessageDialog(null, "Выберите файл для конвертации", "Ошибка: "+e.getClass(), JOptionPane.INFORMATION_MESSAGE);
            }
            //View output image
        }else if(actionEvent.getSource().equals(viewButton)){
            if(Desktop.isDesktopSupported()){
                Desktop dt = Desktop.getDesktop();
                try {
                    dt.open(converter.getOutFile());
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null, "Ошибка откытия конечного файла", "Ошибка: "+e.getClass(), JOptionPane.INFORMATION_MESSAGE);
                }catch (NullPointerException e){
                    JOptionPane.showMessageDialog(null, "Выберите файл для конвертации", "Ошибка: "+e.getClass(), JOptionPane.INFORMATION_MESSAGE);
                }
            }else{
                JOptionPane.showMessageDialog(null, "Desktop не поддерживается", "Ошибка: ", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
}
