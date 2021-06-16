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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

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
                reverseCarrier(carriers, folderPath + file, k);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });
        return carriers;
    }

    private static void reverseCarrier(List<Carrier> carriers, String file, int k) throws FileNotFoundException {
        File imageFile = FileUtils.parseFile(file);
        try {
            BufferedImage imageInfo = BMPDecoder.read(imageFile);
            byte[] image = ((DataBufferByte) imageInfo.getData().getDataBuffer()).getData();
            int height = imageInfo.getHeight();
            int width = imageInfo.getWidth();
            List<List<Byte>> binaryImage = new ArrayList<>();
            int size = image.length;
            if(height%2 != 0 || size%k != 0) throw new IOException("Invalid image or k.");
            int index = 0;
            for(int i = 0; i < height; i++){
                List<Byte> aux = new ArrayList<>();
                for(int j = 0;j < width; j++) {
                    aux.add(image[index++]);
                }
                binaryImage.add(aux);
            }
            Collections.reverse(binaryImage); //OVERKILL, SHOULD CHANGE THIS
            carriers.add(getSquaredMatrix(binaryImage, height, width));
        } catch (Exception e) {
            System.out.println("Error while reading file" + imageFile.getName());
        }
    }

    private static Carrier getSquaredMatrix(List<List<Byte>> binaryImage, int height, int width) {
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

    public static List<MathFunction> getFunctions(byte[] secretImage, int k){
        List<MathFunction> tmp = new ArrayList<>();
        for(int i = 0; i < secretImage.length; ){
            MathFunction aux = new MathFunction();
            for(int j = 0; j < k ;k++) {
                MultExpression multExpression = new MultExpression();
                multExpression.addExpression(Byte.toUnsignedInt(secretImage[i++]));
            }

            tmp.add(aux);
        }
        return tmp;
    }

    public void saveImage(Carrier carrier) {
        // 1. reverse carrier
        // 2. save image :)
    }
}
