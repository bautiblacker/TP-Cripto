import com.idrsolutions.image.JDeli;
import models.BMPImage;
import models.Config;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        Config config = new Config(args);
        BMPImage bmpImage = new BMPImage(config.getSecretImage().getPath(), config.getK());
        //List<List<Byte>> squaredMatrix = bmpImage.getSquaredMatrix();
    }
}
