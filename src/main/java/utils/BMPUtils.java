package utils;

import models.BMPImage;
import models.Carrier;
import models.MathFunction;
import models.MultExpression;
import net.sf.image4j.codec.bmp.BMPDecoder;
import net.sf.image4j.codec.bmp.InfoHeader;
import net.sf.image4j.io.LittleEndianInputStream;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.*;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

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
            byte[] imageHeader = getHeaderFromBMPFile(file);
            byte[] image = ((DataBufferByte) imageInfo.getData().getDataBuffer()).getData();
            int height = imageInfo.getHeight();
            int width = imageInfo.getWidth();

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
            // do something
            System.out.println("getHeaderFromBMPFile() printaloo");
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
        int counter = 0;
        for(int i = 0; i < secretImage.size(); ){
            MathFunction aux = new MathFunction();
            for(int j = 0; j < k ;j++) {
                MultExpression multExpression = new MultExpression();
                multExpression.addExpression(Byte.toUnsignedInt(secretImage.get(i++)));
                aux.addExpression(multExpression);
            }
            tmp.add(aux);
        }
        return tmp;
    }

    public static void saveAll(List<Carrier> carriers) throws FileNotFoundException, IOException {
        for(Carrier carrier : carriers) {
            byte[] carrierByteArray =  BMPUtils.reverseCarrier(carrier);
            System.out.println(carrier.getFilePath());
            File outputFile = new File(carrier.getFilePath().split("\\.")[0] + "_final.png");
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

    public static byte[] reverseCarrier(Carrier myCarrier){
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

//        Collections.reverse(blockList);

        index = 0;
        byte[] image = new byte[HEADER_LENGTH + heigth * width];
        for (byte b: myCarrier.getHeader()) {
            image[index++] = b;
        }
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
        Carrier carrier = BMPUtils.getSquaredMatrix(carrierImage,heigth, width, null, null);

        byte[] convertedCarrierImage = BMPUtils.reverseCarrier(carrier);
        System.out.println("HOLA");
    }
}
