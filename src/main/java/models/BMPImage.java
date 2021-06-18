package models;

import lombok.Data;
import net.sf.image4j.codec.bmp.BMPDecoder;
import net.sf.image4j.codec.bmp.InfoHeader;
import net.sf.image4j.io.LittleEndianInputStream;
import utils.BMPUtils;
import utils.FileUtils;

import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

@Data
public class BMPImage {
    private byte[] header;
    private byte[] secretImage;
    private int k, height, width;

    public BMPImage(String path, int k) throws Exception {
        this.k = k;
        File imageFile = FileUtils.parseFile(path);
        InputStream imageFileInputStream = new FileInputStream(imageFile);
        try {
           secretImage = ((DataBufferByte) BMPDecoder.read(imageFile).getData().getDataBuffer()).getData();
           header = BMPUtils.getHeaderFromBMPFile(path);
           height = BMPDecoder.read(imageFile).getHeight();
           width = BMPDecoder.read(imageFile).getWidth();
        } catch (Exception e) {
            System.out.println("Error while reading file" + imageFile.getName());
            throw  new Exception(e.getMessage());
        }
    }

    public byte[] getHeader() {
        return header;
    }

    public byte[] getSecretImage() {
        return secretImage;
    }

    public int getK() {
        return k;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}
