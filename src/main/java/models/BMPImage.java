package models;

import com.idrsolutions.image.JDeli;
import lombok.Data;
import utils.Binary;
import utils.FileUtils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Data
public class BMPImage {
    private byte[] header;
    private byte[] body;
    private int width, height;
    private List<List<String>> binaryImage = new ArrayList<>();

    public BMPImage(String path, int k) throws FileNotFoundException {
        File imageFile = FileUtils.parseFile(path);
        try {
            BufferedImage image = JDeli.read(imageFile);
            height = image.getData().getHeight();
            width = image.getData().getWidth();
            int size = image.getData().getDataBuffer().getSize();
            if(height%2 != 0 || size%k != 0) throw new IOException("Invalid image or k.");
            int index = 0;
            for(int i = 0;i < height; i++){
                List<String> aux = new ArrayList<>();
                for(int j = 0;j < width; j++) {
                    aux.add(Binary.getBinary(image.getData().getDataBuffer().getElem(index++)));
                    //System.out.println(image.getData().getDataBuffer().getElem(i) + " - " + Binary.getBinary(image.getData().getDataBuffer().getElem(i)));
                }
                binaryImage.add(aux);
            }
        } catch (Exception e) {
            System.out.println("Error while reading file" + imageFile.getName());
        }
    }

    public List<List<String>> getSquaredMatrix() {
        List<List<String>> squaredMatrix = new ArrayList<>();
        for(int i = 0;i < height; i += 2){
            for(int j = 0;j < width; j += 2) {
                List<String> aux = new ArrayList<>();
                aux.add(binaryImage.get(i).get(j));
                aux.add(binaryImage.get(i).get(j+1));
                aux.add(binaryImage.get(i+1).get(j));
                aux.add(binaryImage.get(i+1).get(j+1));
                squaredMatrix.add(aux);
            }

        }
        return squaredMatrix;
    }

    /*public static void main(String[] args){
        List<List<String>> example = new ArrayList<>();
        for(int i = 0;i<8;i++){
            List<String> aux = new ArrayList<>();
            for(int j = 0;j<6;j++){
                aux.add(String.valueOf(i*6 + j + 1));
            }
            example.add(aux);
        }

        printImage(example);
        System.out.println();
        System.out.println();
        System.out.println();
        printImage(getSquaredMatrix());
    }

    private static void printImage(List<List<String>> image) {
        for(int i = 0;i< image.size();i++){
            for(int j = 0;j< image.get(i).size();j++){
                System.out.print(image.get(i).get(j) + "\t");
            }
            System.out.println("\n");
        }
    }*/
}
