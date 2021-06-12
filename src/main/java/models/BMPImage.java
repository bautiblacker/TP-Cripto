package models;

import lombok.Data;
import net.sf.image4j.codec.bmp.BMPDecoder;
import utils.FileUtils;

import java.awt.image.DataBufferByte;
import java.io.File;

@Data
public class BMPImage {
    private byte[] secretImage;
    private int k;

    public BMPImage(String path, int k) throws Exception {
        this.k = k;
        File imageFile = FileUtils.parseFile(path);
        try {
           secretImage = ((DataBufferByte) BMPDecoder.read(imageFile).getData().getDataBuffer()).getData();
        } catch (Exception e) {
            System.out.println("Error while reading file" + imageFile.getName());
            throw  new Exception(e.getMessage());
        }
    }
}
