package utils;


import java.util.*;

public class GaloisField {
    //TODO: Cambiar static
//    static Map<Integer, Integer> map = Binary.getMappingByte();
    public static Byte add(Byte b1, Byte b2){
        // convert to ints and xor
        int one = Byte.toUnsignedInt(b1);
        int two = Byte.toUnsignedInt(b2);
        int xor = one ^ two;

        // convert back to byte
        return (byte)(0xff & xor);
    }

    public static int product(int b1, int b2){
        String byte2 = Binary.getBinary(b2,15);
        int tmp = 0;
        boolean firstTime = true;
        for(int i = 0; i < byte2.length(); i++){
            if(byte2.charAt(i) == '1') {
                int aux = ((b1) << byte2.length()-i-1);
                if(firstTime) {
                    tmp = aux;
                    firstTime = false;
                } else
                    tmp = tmp ^ aux;
            }
        }
        return tmp;
    }

    public static int product(int b1, int b2, int generator){
        String byte2 = Binary.getBinary(b2,15);
        int result = 0;
        for (int i = 0; i < byte2.length(); i++) {
            if (byte2.charAt(byte2.length()-1 - i) == '1'){
                result ^= (b1 << i);
            }
        }
        return result;
//        return moduleReducer(result, generator);
    }

//    public static Byte product(int b1, int b2, int generator){
//        return moduleReducer(product(b1,b2), generator);
//    }


    public static long getUnsignedInt(int x) {
        return x & (-1L >>> 32);
    }

//    private static List<Integer> mapToList(Integer n) {
//        Integer maxValue = Binary.getMappingByte().get(Integer.highestOneBit(n));
//        if(maxValue == null)
//            maxValue = 8;
//        String binary = Binary.getBinary(n, maxValue);
//        List<Integer> binaryAsList = new ArrayList<>();
//        for(String bit : binary.split("")) {
//            binaryAsList.add(Integer.parseInt(bit));
//        }
//
//        return binaryAsList;
//    }

    public static byte moduleReducer(int val, int generator) {
        int gradeDiff = getGradeDiff(val, generator);
        int remainder = val;
        while (gradeDiff >= 0){
            int result = generator << gradeDiff;
            remainder ^= result;
            gradeDiff = getGradeDiff(remainder, generator);
        }
        return (byte)remainder;
    }

    private static int getGradeDiff(int a, int b) {
        int aGrade = getGradeFromBinaryString(Binary.getBinary(a,15));
        int bGrade = getGradeFromBinaryString(Binary.getBinary(b,15));
        if (aGrade == -1 || bGrade == -1){
            System.out.println("Error perri");
            System.exit(1);
        }
        return aGrade - bGrade;
    }

    private static int getGradeFromBinaryString(String binaryStr) {
        for (int i = 0; i < binaryStr.length(); i++){
            if (binaryStr.charAt(i) == '1')
                return binaryStr.length() - 1 - i; //000010
        }
        return 0;
    }

//    public static byte moduleReducer(int tmp, int generator) {
//        List<Integer> px = mapToList(tmp);
//        List<Integer> mx = mapToList(generator);
//        int gradeDiff = px.size() - mx.size();
//        List<Integer> currentPx = new ArrayList<>(px);
//        List<Integer> ratio;
//
//        while(gradeDiff >= 0) {
//            List<Integer> tempPx = new ArrayList<>();
//            ratio = new ArrayList<>(mx);
//            for(int j = gradeDiff; j > 0; j--) {
//                ratio.add(0);
//            }
//
//            for(int i = 0; i < ratio.size(); i++) {
//                tempPx.add(currentPx.get(i) ^ ratio.get(i));
//            }
//
//            int index = getIndex(tempPx);
//            if(index == -1)
//                return 0; //TODO: Fijarse si esto esta bien
//            currentPx = new ArrayList<>(tempPx.subList(index, tempPx.size()));
//            gradeDiff = (currentPx.size() - mx.size());
//        }
//
//        return Binary.getBinary(currentPx);
//    }


    public static byte divide(byte b1, byte b2){
        if(b1 == 0)
            return 0;
        return moduleReducer(product(Byte.toUnsignedInt(b1),Byte.toUnsignedInt(inverse(b2))),355);
    }

    public static byte inverse(byte x){
        for(int i = 0; i < 255 ; i++){
            if(moduleReducer(product(i,Byte.toUnsignedInt(x)),355) == 1)
                return (byte) i;

        }
        return x;
    }

    public static void main(String[] args) {
        byte num1 = (byte)200, num2 = 14;
        byte tmp = divide(num1,num2);
        System.out.println(Byte.toUnsignedInt(tmp));
        System.out.println(Byte.toUnsignedInt(inverse(num2)));
    }

}
