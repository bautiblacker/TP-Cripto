package models;

import com.idrsolutions.image.JDeli;
import lombok.Data;
import utils.Binary;
import utils.FileUtils;

import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Data
public class BMPImage {
    private byte[] header;
    private DataBuffer secretImage;
    private byte[] body;
    private int width, height, k;
    //private List<List<Byte>> binaryImage = new ArrayList<>();
    private List<List<List<Byte>>> carriers = new ArrayList<>();

    public BMPImage(String path, int k) throws FileNotFoundException {
        this.k = k;
        File imageFile = FileUtils.parseFile(path);
        try {
            BufferedImage image = JDeli.read(imageFile);
            secretImage = image.getData().getDataBuffer();
        } catch (Exception e) {
            System.out.println("Error while reading file" + imageFile.getName());
        }
        /*File imageFile = FileUtils.parseFile(path);
        try {
            BufferedImage image = JDeli.read(imageFile);
            height = image.getData().getHeight();
            width = image.getData().getWidth();
            int size = image.getData().getDataBuffer().getSize();
            if(height%2 != 0 || size%k != 0) throw new IOException("Invalid image or k.");
            int index = 0;
            for(int i = 0;i < height; i++){
                List<Byte> aux = new ArrayList<>();
                for(int j = 0;j < width; j++) {
                    aux.add(new Integer(image.getData().getDataBuffer().getElem(index++)).byteValue());
                }
                binaryImage.add(aux);
            }
            Collections.reverse(binaryImage); //OVERKILL, SHOULD CHANGE THIS
        } catch (Exception e) {
            System.out.println("Error while reading file" + imageFile.getName());
        }*/
    }

    public void getCarriers(String folderPath) throws Exception {
        final File folder = new File(folderPath);
        List<String> files = new ArrayList<>();
        for (final File fileEntry : Objects.requireNonNull(folder.listFiles())) {
            files.add(fileEntry.getName());
        }
        if(files.size() != k)
            throw new Exception("Se necesitan poner " +k + " imagenes en " + folderPath);
        files.forEach(file -> {
            try {
                reverseCarrier(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });

    }

    private void reverseCarrier(String file) throws FileNotFoundException {
        File imageFile = FileUtils.parseFile(file);
        try {
            BufferedImage image = JDeli.read(imageFile);
            height = image.getData().getHeight();
            List<List<Byte>> binaryImage = new ArrayList<>();
            width = image.getData().getWidth();
            int size = image.getData().getDataBuffer().getSize();
            if(height%2 != 0 || size%k != 0) throw new IOException("Invalid image or k.");
            int index = 0;
            for(int i = 0;i < height; i++){
                List<Byte> aux = new ArrayList<>();
                for(int j = 0;j < width; j++) {
                    aux.add(new Integer(image.getData().getDataBuffer().getElem(index++)).byteValue());
                }
                binaryImage.add(aux);
            }
            Collections.reverse(binaryImage); //OVERKILL, SHOULD CHANGE THIS
            carriers.add(binaryImage);
        } catch (Exception e) {
            System.out.println("Error while reading file" + imageFile.getName());
        }
    }

    /*public List<List<Byte>> getSquaredMatrix() {
        List<List<Byte>> squaredMatrix = new ArrayList<>();
        for(int i = 0;i < height; i += 2){
            for(int j = 0;j < width; j += 2) {
                List<Byte> aux = new ArrayList<>();
                aux.add(binaryImage.get(i).get(j));
                aux.add(binaryImage.get(i).get(j+1));
                aux.add(binaryImage.get(i+1).get(j));
                aux.add(binaryImage.get(i+1).get(j+1));
                squaredMatrix.add(aux);
            }

        }
        return squaredMatrix;
    }*/

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
