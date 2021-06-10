package utils;


import java.util.*;

public class GaloisField {
    //TODO: Cambiar static
    static Map<Integer, Integer> map = Binary.getMappingByte();
    public static Byte add(Byte b1, Byte b2){
        // convert to ints and xor
        int one = (int)b1;
        int two = (int)b2;
        int xor = one ^ two;

        // convert back to byte
        return (byte)(0xff & xor);
    }

    public static Byte product(Byte b1, Byte b2, int generator){
        String byte2 = Binary.getBinary(b2, 15);
        int tmp = 0;
        boolean firstTime = true;
        for(int i = 0;i<byte2.length();i++){
            if(byte2.charAt(i) == '1') {
                int aux = ((int)b1) << byte2.length()-i-1;
                if(firstTime) {
                    tmp = aux;
                    firstTime = false;
                }else
                    tmp = tmp ^ aux;
            }
        }
        for(int i = 9 ;i >= 0;i--){
            if (((tmp >>> i) & 1) != 0) {
                System.out.println(i + " is Set");
            }
        }
        System.out.println("highestonebit: " + Integer.highestOneBit(tmp) + "  -  " + map.get(Integer.highestOneBit(tmp)));
        System.out.println("Number: " + tmp);
        List<Integer> tmpList = mapToList(tmp);
        List<Integer> generatorList = mapToList(generator);
        return moduleReducer(tmpList, generatorList);
    }

    public static void main(String[] args) {
        Integer num1 = 84, num2 = 13;
        //System.out.println(Integer.toBinaryString(num1.byteValue() << 5));
        //System.out.println(product(num1.byteValue(),num2.byteValue(),(byte)0)); //Print as int
        //System.out.println(Binary.getBinary(product(num1.byteValue(),num2.byteValue(),(byte)0))); // Print as binary
        byte tmp = product(num1.byteValue(),num2.byteValue(),355);
        System.out.println(tmp);

    }
    public static long getUnsignedInt(int x) {
        return x & (-1L >>> 32);
    }

    private static List<Integer> mapToList(Integer n) {
        Integer maxValue = Binary.getMappingByte().get(Integer.highestOneBit(n));
        String binary = Binary.getBinary(n, maxValue);
        List<Integer> binaryAsList = new ArrayList<>();
        for(String bit : binary.split("")) {
            binaryAsList.add(Integer.parseInt(bit));
        }

        return binaryAsList;
    }

    public static byte moduleReducer(List<Integer> px, List<Integer> mx) {
        int gradeDiff = px.size() - mx.size();
        List<Integer> currentPx = new ArrayList<>(px);
        List<Integer> ratio = new ArrayList<>();

        while(gradeDiff >= 0) {
            List<Integer> tempPx = new ArrayList<>();
            ratio = new ArrayList<>(mx);
            for(int j = gradeDiff; j > 0; j--) {
                ratio.add(0);
            }

            for(int i = 0; i < ratio.size(); i++) {
                tempPx.add(currentPx.get(i) ^ ratio.get(i));
            }

            int index = getIndex(tempPx);
            currentPx = new ArrayList<>(tempPx.subList(index, tempPx.size()));
            gradeDiff = (currentPx.size() - mx.size());
        }

        return Binary.getBinary(currentPx);
    }

    private static int getIndex(List<Integer> list) {
        for(int i = 0; i < list.size(); i++) {
            if(list.get(i) == 1) {
                return i;
            }
        }

        return -1;
    }

}
