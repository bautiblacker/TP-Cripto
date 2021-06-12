import models.BMPImage;
import models.Carrier;
import models.Config;
import utils.BMPUtils;

import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        Config config = new Config(args);
        BMPImage bmpImage = new BMPImage(config.getSecretImage().getPath(), config.getK());
        Carrier imageAsCarrier = bmpImage.getSquaredMatrix();
        List<Carrier> carriers = BMPUtils.getCarriers(config.isD() ? config.getHideInDirectory() : config.getRecoverFormDirectory(), config.getK());
    }
}
