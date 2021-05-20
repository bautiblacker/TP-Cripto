package models;

import com.idrsolutions.image.JDeli;
import lombok.Data;
import utils.Binary;
import utils.FileUtils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Data
public class BMPImage {
    private byte[] header;
    private byte[] body;
    private List<List<String>> binaryImage = new ArrayList<>();

    public BMPImage(String path) throws FileNotFoundException {
        File imageFile = FileUtils.parseFile(path);
        try {
            BufferedImage image = JDeli.read(imageFile);

            int L = image.getData().getHeight();
            int index = 0;
            for(int i = 0;i < L; i++){
                List<String> aux = new ArrayList<>();
                for(int j = 0;j < L; j++) {
                    aux.add(Binary.getBinary(image.getData().getDataBuffer().getElem(index++)));
                    //System.out.println(image.getData().getDataBuffer().getElem(i) + " - " + Binary.getBinary(image.getData().getDataBuffer().getElem(i)));
                }
                binaryImage.add(aux);
            }
        } catch (Exception e) {
            System.out.println("Error while reading file" + imageFile.getName());
        }
    }

}
