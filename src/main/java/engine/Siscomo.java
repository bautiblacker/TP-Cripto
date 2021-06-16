package engine;

import models.BMPImage;
import models.Carrier;
import models.Config;
import models.MathFunction;
import utils.BMPUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Siscomo {
    public void encrypt(BMPImage bmpImage, Config config) throws Exception {
        int k = config.getK();
        String hideInDirectory = config.getHideInDirectory();

        List<Carrier> carriers = BMPUtils.getCarriers(hideInDirectory, k);
        List<MathFunction> polynomialImage = BMPUtils.getFunctions(bmpImage.getSecretImage(), k);

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

        //save image
    }

    private int getAvailableFx(int value, Set<Integer> takenFx, int k) {
        for(int i = 0; i < k; i++) {
            if(!takenFx.contains(value)) return value;
            value++;
        }

        return value;
    }
}
