package models;

import com.idrsolutions.image.JDeli;
import lombok.Data;
import utils.FileUtils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;

@Data
public class BMPImage {
    private byte[] header;
    private byte[] body;

    public BMPImage(String path) throws FileNotFoundException {
        File imageFile = FileUtils.parseFile(path);
        try {
            BufferedImage image = JDeli.read(imageFile);

        } catch (Exception e) {
            System.out.println("Error while reading file" + imageFile.getName());
        }
    }

}
