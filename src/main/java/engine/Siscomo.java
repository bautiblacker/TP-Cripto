package engine;

import models.BMPImage;
import models.Carrier;
import models.Config;
import models.MathFunction;
import utils.BMPUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Siscomo {
    public static void encrypt(Config config) throws Exception {
        BMPImage bmpImage = new BMPImage(config.getSecretImage().getPath(), config.getK());
        int k = config.getK();
        String hideInDirectory = config.getHideInDirectory();

        List<Carrier> carriers = BMPUtils.getCarriers(hideInDirectory, k);
        List<Byte> secretImage = BMPUtils.convertSecretToMatrix(bmpImage.getSecretImage(), bmpImage.getHeight(),
                bmpImage.getWidth(),config.getK());
        List<MathFunction> polynomialImage = BMPUtils.getFunctions(secretImage, k);

        int index = 0;
        Set<Integer> takenFx = new HashSet<>();
        for(MathFunction mathFunction : polynomialImage) {
            for(Carrier carrier : carriers) {
                Byte x = carrier.getImageBlockBytes().get(index).get(0);
                mathFunction.fill(x);
                int fx = getAvailableFx(mathFunction.eval(), takenFx, k);
                takenFx.add(fx);
                Byte byteFx = (byte) fx;
                carrier.setXAtIndex(index, byteFx);
            }
            takenFx.clear();
            index++;
        }

        try {
            BMPUtils.saveAll(carriers);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static int getAvailableFx(int value, Set<Integer> takenFx, int k) {
        for(int i = 0; i < k; i++) {
            if(!takenFx.contains(value)) return value;
            value++;
        }

        return value;
    }

    public static void decrypt(Config config) throws Exception {
        String recover = config.getRecoverFormDirectory();
        int k = config.getK();
        List<Carrier> carriers = BMPUtils.getCarriers(recover, k);

        int index = 0;
        for(Carrier carrier : carriers){
            for(int i = 0; i < carrier.getHeight()*carrier.getWidth()/config.getK();i++){
                index++;
                Byte x = carrier.getImageBlockBytes().get(i).get(0);
                Byte fx = Carrier.getFXFromBlock(carrier.getImageBlockBytes().get(i).get(1),
                        carrier.getImageBlockBytes().get(i).get(2),
                        carrier.getImageBlockBytes().get(i).get(3));
            }
        }
    }
}
