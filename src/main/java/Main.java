import engine.Siscomo;
import models.BMPImage;
import models.Carrier;
import models.Config;
import utils.BMPUtils;

import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        Config config = new Config(args);
        if(config.isR())
            Siscomo.decrypt(config);
        else
            Siscomo.encrypt(config);

    }
}
