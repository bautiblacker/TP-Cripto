import models.BMPImage;
import models.Carrier;
import models.Config;
import utils.BMPUtils;

import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        Config config = new Config(args);
        BMPImage bmpImage = new BMPImage(config.getSecretImage().getPath(), config.getK());
        //Carrier imageAsCarrier = BMPUtils.getSquaredMatrix(bmpImage.getSecretImage(), config.getK());
        List<Carrier> carriers = BMPUtils.getCarriers(config.isD() ? config.getHideInDirectory() : config.getRecoverFormDirectory(), config.getK());
    }
}
