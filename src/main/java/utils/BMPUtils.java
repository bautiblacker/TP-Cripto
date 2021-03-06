package utils;

import models.BMPImage;
import models.Carrier;
import models.MathFunction;
import models.MultExpression;
import net.sf.image4j.codec.bmp.BMPDecoder;

import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.util.*;

public class BMPUtils {
    public static final int HEADER_LENGTH = 1078;

    public static List<Carrier> getCarriers(String folderPath, int k) throws Exception {
        final File folder = new File(folderPath);
        List<String> files = new ArrayList<>();
        List<Carrier> carriers = new ArrayList<>();
        for (final File fileEntry : Objects.requireNonNull(folder.listFiles())) {
            files.add(fileEntry.getName());
        }
        if(files.size() != k)
            throw new Exception("Se necesitan poner " + k + " imagenes en " + folderPath);
        for(int i = 0; i < files.size(); i++) {
            try {
                reverseCarrier(carriers, folderPath + files.get(i));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return carriers;
    }

    private static void reverseCarrier(List<Carrier> carriers, String file) throws FileNotFoundException {
        File imageFile = FileUtils.parseFile(file);
        try {
            BufferedImage imageInfo = BMPDecoder.read(imageFile);
            byte[] imageHeader = getHeaderFromBMPFile(file);
            int height = imageInfo.getHeight();
            int width = imageInfo.getWidth();
            byte[] array = Files.readAllBytes(imageFile.toPath());
            byte[] image = new byte[height * width];
            int index = 0;
            for(int i = 1078; i < array.length; i++) {
                image[index++] = array[i];
            }

            carriers.add(getSquaredMatrix(image, height, width, file, imageHeader));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error while reading file" + imageFile.getName());
        }
    }

    public static byte[] getHeaderFromBMPFile(String fileName) throws FileNotFoundException, IOException {
        byte[] buffer = new byte[1078];
        InputStream is = new FileInputStream(fileName);
        if (is.read(buffer) != buffer.length) {
            System.out.println("Invalid header on BMP file:" + fileName + ". Exiting.");
            System.exit(1);
        }
        is.close();
        return buffer;
    }

    private static Carrier getSquaredMatrix(byte[] image, int height, int width, String path, byte[] imageHeader) throws IOException {
        List<List<Byte>> binaryImage = new ArrayList<>();
        int size = image.length;
        if(height%2 != 0) throw new IOException("Invalid image or k.");
        int index = 0;
        for(int i = 0; i < height; i++){
            List<Byte> aux = new ArrayList<>();
            for(int j = 0;j < width; j++) {
                aux.add(image[index++]);
            }
            binaryImage.add(aux);
        }
        Collections.reverse(binaryImage);

        List<List<Byte>> squaredMatrix = new ArrayList<>();
        for(int i = 0; i < height; i += 2){
            for(int j = 0; j < width; j += 2) {
                List<Byte> aux = new ArrayList<>();
                aux.add(binaryImage.get(i).get(j));
                aux.add(binaryImage.get(i).get(j+1));
                aux.add(binaryImage.get(i+1).get(j));
                aux.add(binaryImage.get(i+1).get(j+1));
                squaredMatrix.add(aux);
            }
        }

        return new Carrier(squaredMatrix, path, imageHeader, width, height);
    }

    public static List<MathFunction> getFunctions(List<Byte> secretImage, int k){
        List<MathFunction> tmp = new ArrayList<>();
        for(int i = 0; i < secretImage.size(); ){
            MathFunction aux = new MathFunction();
            for(int j = 0; j < k; j++) {
                MultExpression multExpression = new MultExpression();
                multExpression.addExpression(secretImage.get(i++));
                aux.addExpression(multExpression);
            }
            tmp.add(aux);
        }
        return tmp;
    }

    /*
     * Documentar codigo (que sea legible y entendible que hace cada funcion)
     * NTH: Hacer mas performante el decrypt
     * Look up table para el divide
     * Ejecutable para correr en pampero
     * Hacer README
     * Imagenes de distintos tama??os
     */

    public static void saveAll(List<Carrier> carriers, byte[] header) throws FileNotFoundException, IOException {
        for(Carrier carrier : carriers) {
            byte[] carrierByteArray =  BMPUtils.reverseCarrier(carrier, header);
            // System.out.println("Hiding part of the image at " + carrier.getFilePath());
            File outputFile = new File(carrier.getFilePath());
            try (FileOutputStream outputStream = new FileOutputStream(outputFile)) {
                outputStream.write(carrierByteArray);
            }
        }
    }

    public static void saveBMPIMage(BMPImage image, String filePath) {
        File outputFile = new File(filePath);
        try (FileOutputStream outputStream = new FileOutputStream(outputFile)) {
            outputStream.write(image.getHeader());
            outputStream.write(image.getSecretImage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Byte> convertSecretToMatrix(byte[] image, int height, int width, int k){
        List<List<Byte>> matrix = new ArrayList<>();

        int idx = 0;
        for(int i = 0;i < height;i++){
            List<Byte> tmp = new ArrayList<>();
            for(int j = 0;j < width;j++){
                tmp.add(image[idx++]);
            }
            matrix.add(tmp);
        }

        Collections.reverse(matrix);

        List<Byte> fullArray = new ArrayList<>();
        for(List<Byte> tmp : matrix){
            fullArray.addAll(tmp);
        }

        return fullArray;
    }

    public static byte[] reverseCarrier(Carrier myCarrier, byte[] header){
        List<List<Byte>> blockList = new ArrayList<>();
        int heigth = myCarrier.getHeight();
        int width = myCarrier.getWidth();
        int index = 0;
        for(int i = 0; i < heigth/2; i++) {
            blockList.add(2 * i, new ArrayList<>());
            blockList.add(2*i + 1, new ArrayList<>());
            for (int j = 0; j < width / 2; j++) {
                Byte x = myCarrier.getImageBlockBytes().get(index).get(0);
                Byte w = myCarrier.getImageBlockBytes().get(index).get(1);
                Byte v = myCarrier.getImageBlockBytes().get(index).get(2);
                Byte u = myCarrier.getImageBlockBytes().get(index).get(3);

                blockList.get(2 * i).add(2 * j, x);
                blockList.get(2 * i).add(2 * j + 1, w);
                blockList.get(2 * i + 1).add(2 * j, v);
                blockList.get(2 * i + 1).add(2 * j + 1, u);

                index++;
            }
        }

        Collections.reverse(blockList);

        index = 0;
        byte[] image = new byte[HEADER_LENGTH + heigth * width];
        for (byte b: header) {
            image[index++] = b;
        }
        for(List<Byte> byteList : blockList){
            for(Byte b : byteList){
                image[index++] = b;
            }
        }

        return image;
    }
}
