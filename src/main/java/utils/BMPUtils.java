package utils;

import models.BMPImage;
import models.Carrier;
import models.MathFunction;
import models.MultExpression;
import net.sf.image4j.codec.bmp.BMPDecoder;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BMPUtils {

    public static List<Carrier> getCarriers(String folderPath, int k) throws Exception {
        final File folder = new File(folderPath);
        List<String> files = new ArrayList<>();
        List<Carrier> carriers = new ArrayList<>();
        for (final File fileEntry : Objects.requireNonNull(folder.listFiles())) {
            files.add(fileEntry.getName());
        }
        if(files.size() != k)
            throw new Exception("Se necesitan poner " + k + " imagenes en " + folderPath);
        files.forEach(file -> {
            try {
                reverseCarrier(carriers, folderPath + file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });
        return carriers;
    }

    private static void reverseCarrier(List<Carrier> carriers, String file) throws FileNotFoundException {
        File imageFile = FileUtils.parseFile(file);
        try {
            BufferedImage imageInfo = BMPDecoder.read(imageFile);
            byte[] image = ((DataBufferByte) imageInfo.getData().getDataBuffer()).getData();
            int height = imageInfo.getHeight();
            int width = imageInfo.getWidth();

            carriers.add(getSquaredMatrix(image, height, width));
        } catch (Exception e) {
            System.out.println("Error while reading file" + imageFile.getName());
        }
    }

    private static Carrier getSquaredMatrix(byte[] image, int height, int width) throws IOException {
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

        return new Carrier(squaredMatrix);
    }

    /*private static void reverseGetSquaredMatrix(Carrier myCarrier, int height, int width){
        /*
        uint32_t x = 0;
        for(uint32_t i = 0 ; i < n / 2 ; i++){
            for(uint32_t j = 0 ; j < k  / 2; j++){
                //printf("%d %d %d %d \n",ret[x][0],ret[x][1],ret[x][2],ret[x][3]);
                uint32_t a = ret[x][0];
                uint32_t b = ret[x][1];
                uint32_t c = ret[x][2];
                uint32_t d = ret[x][3];
                uint32_t I = 2i;
                uint32_t J = 2j;
                //printf("{%d -> %d | %d -> %d} : %d %d %d %d \n",i,I,j,J,a,b,c,d);
                mat[I][J] = a;
                mat[I][J+1] = b;
                mat[I+1][J] = c;
                mat[I+1][J+1] = d;
                x++;
            }
        }

        int index = 0;
        List<List<Byte>> myList = new ArrayList<>();
        //si width = 300, height = 300, k= 4 => n 300*300/k = 22500
        for(int i = 0; i < n/2 ; i += 2) {
            for(int j = 0;j < k/2 ; j++){
                Byte x = myCarrier.getImageBlockBytes().get(index).get(0);
                Byte w = myCarrier.getImageBlockBytes().get(index).get(1);
                Byte v = myCarrier.getImageBlockBytes().get(index).get(2);
                Byte u = myCarrier.getImageBlockBytes().get(index).get(3);

                myList.get(i*2).set(2*j,x);
                myList.get(i*2).set(2*j + 1,w);
                myList.get(i*2 + 1).set(2*j,v);
                myList.get(i*2 + 1).set(2*j + 1,u);

                index++;
            }


        }
    }*/

    public static List<MathFunction>  getFunctions(List<Byte> secretImage, int k){
        List<MathFunction> tmp = new ArrayList<>();
        for(int i = 0; i < secretImage.size(); ){
            MathFunction aux = new MathFunction();
            for(int j = 0; j < k ;k++) {
                MultExpression multExpression = new MultExpression();
                multExpression.addExpression(Byte.toUnsignedInt(secretImage.get(i++)));
            }

            tmp.add(aux);
        }
        return tmp;
    }

    public void saveImage(Carrier carrier, int height, int width) {
        byte[] carrierByteArray =  BMPUtils.reverseCarrier(carrier, height, width);
        // 1. reverse carrier
        // 2. save image :)
    }

    public static List<Byte> convertSecretToMatrix(byte[] image, int height, int width, int k){
        List<List<Byte>> matrix = new ArrayList<>();

        //QUIERO EVITAR ESTO....... COMO??
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

    public static byte[] reverseCarrier(Carrier myCarrier, int heigth, int width){
        //TODO: CHANGE BYTE[][] TO LIST<LIST<BYTE>>
        byte[][] blockArray = new byte[heigth][width];
        int index = 0;
        for(int i = 0; i < heigth/2; i++){
            //blockList.set(2*i, new ArrayList<>());
            //blockList.set(2*i + 1, new ArrayList<>());
            for(int j = 0; j < width/2; j++){
                Byte x = myCarrier.getImageBlockBytes().get(index).get(0);
                Byte w = myCarrier.getImageBlockBytes().get(index).get(1);
                Byte v = myCarrier.getImageBlockBytes().get(index).get(2);
                Byte u = myCarrier.getImageBlockBytes().get(index).get(3);

                blockArray[2*i][2*j] = x;
                blockArray[2*i][2*j+1] = w;
                blockArray[2*i+1][2*j] = v;
                blockArray[2*i+1][2*j+1] = u;

                index ++;
            }
        }
        List<List<Byte>> blockList = new ArrayList<>();

        for(int i = 0; i < heigth; i++){
            List<Byte> tmp = new ArrayList<>();
            for(int j = 0; j < width; j++){
                tmp.add(blockArray[i][j]);
            }
            blockList.add(tmp);
        }

        Collections.reverse(blockList);

        index = 0;
        byte[] image = new byte[heigth * width];
        for(List<Byte> byteList : blockList){
            for(Byte b : byteList){
                image[index++] = b;
            }
        }

        return image;
    }

    public static void main(String[] args) throws IOException {
        byte[] carrierImage = new byte[36];
        for(int i = 0;i < 36;i++){
            carrierImage[i] = (byte) i;
        }
        int heigth = 6, width = 6, k = 4;
        Carrier carrier = BMPUtils.getSquaredMatrix(carrierImage,heigth, width);

        byte[] convertedCarrierImage = BMPUtils.reverseCarrier(carrier,heigth,width);
        System.out.println("HOLA");
    }
}
