package engine;

import javafx.util.Pair;
import models.*;
import utils.BMPUtils;
import utils.GaloisField;

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
        Set<Byte> takenX = new HashSet<>();
        for(MathFunction mathFunction : polynomialImage) {
            for(Carrier carrier : carriers) {
                Byte x = carrier.getImageBlockBytes().get(index).get(0);
                x = getAvailableX(x, takenX, k);
                takenX.add(x);
                mathFunction.fill(x);
                int fx = mathFunction.eval();
                mathFunction.reset();
                Byte byteFx = (byte) fx;
                carrier.setXAtIndex(index, byteFx,x);
            }
            takenX.clear();
            index++;
        }

        try {
            BMPUtils.saveAll(carriers, bmpImage.getHeader());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Byte getAvailableX(Byte value, Set<Byte> takenFx, int k) {
        for(int i = 0; i < k; i++) {
            if(!takenFx.contains(value)) return value;
            value++;
        }

        return value;
    }

    public static void decrypt(Config config) throws Exception {
        String recover = config.getRecoverFormDirectory();
        int k = config.getK();
        List<Carrier> carriers = BMPUtils.getCarriers(recover, k); //podria
        byte[] secretImageHeader = carriers.get(0).getHeader();

        List<List<Pair<Byte, Byte>>> xAndFxPairsForAllBlocks = new ArrayList<>();

        for(int i = 0; i < carriers.get(0).getHeight()*carriers.get(0).getWidth()/config.getK();i++){
            List<Pair<Byte, Byte>> xAndFxPairForBlock = new ArrayList<>();
            for(Carrier carrier : carriers){
                Byte x = carrier.getImageBlockBytes().get(i).get(0);
                Byte fx = Carrier.getFXFromBlock(carrier.getImageBlockBytes().get(i).get(1),
                        carrier.getImageBlockBytes().get(i).get(2),
                        carrier.getImageBlockBytes().get(i).get(3));
                xAndFxPairForBlock.add(new Pair<>(x, fx));
            }
            xAndFxPairsForAllBlocks.add(xAndFxPairForBlock);
        }
        List<Byte[]> blockCoefficients = new ArrayList<>();
        for (List<Pair<Byte, Byte>> xAndFxPairsForBlock : xAndFxPairsForAllBlocks) {
            Byte[] currentBlockCoefficients = getCoefficients(xAndFxPairsForBlock, k); //
            blockCoefficients.add(currentBlockCoefficients);
        }

        byte[] secretImageData = new byte[blockCoefficients.size() * blockCoefficients.get(0).length];
        int index = 0;
        for (Byte[] coefficients: blockCoefficients)
            for (Byte b: coefficients)
                secretImageData[index++] = b;

        BMPImage secretImage = new BMPImage(secretImageHeader, secretImageData);
        BMPUtils.saveBMPIMage(secretImage, config.getSecretImage().getPath());
    }

    private static Byte[] getCoefficients(List<Pair<Byte, Byte>> xAndFxPairs, int k) {
        int currentOrder = k;
        Byte[] coefficients = new Byte[k];
        int index = 0;
        byte lastCoefficient = Lagrange.lagrangeInterpolation(xAndFxPairs, (byte)0);
        coefficients[index++] = lastCoefficient;
        xAndFxPairs = removeZeroElements(xAndFxPairs);
        while (--currentOrder > 0) {
            xAndFxPairs = getprimeYValues(xAndFxPairs, lastCoefficient);
            lastCoefficient = Lagrange.lagrangeInterpolation(xAndFxPairs, (byte)0);
            coefficients[index++] = lastCoefficient;
            xAndFxPairs.remove(xAndFxPairs.size()-1);
        }

        return coefficients;
    }

    private static byte getYPrime(Pair<Byte, Byte> xAndFxValues, byte s) {
        //return (byte)((xAndFxValues.getValue() - s) / xAndFxValues.getKey());
        return GaloisField.divide(GaloisField.add(xAndFxValues.getValue(),s),xAndFxValues.getKey());
    }

    private static List<Pair<Byte, Byte>> removeZeroElements(List<Pair<Byte, Byte>> list) {
        boolean foundZeroElement = false;
        List<Pair<Byte, Byte>> result = new ArrayList<>();
        for (Pair<Byte, Byte> pair: list){
            if (!pair.getKey().equals((byte)0)){
                result.add(pair);
            } else {
                foundZeroElement = true;
            }
        }
        if (!foundZeroElement)
            result.remove(result.size()-1);
        return result;
    }

    private static List<Pair<Byte, Byte>> getprimeYValues(List<Pair<Byte, Byte>> list, byte lastCoefficient) {
        List<Pair<Byte, Byte>> result = new ArrayList<>();
        for (int i = 0; i < list.size(); i++){
            result.add(new Pair<>(list.get(i).getKey(), getYPrime(list.get(i), lastCoefficient)));
        }
        return result;
    }
}
