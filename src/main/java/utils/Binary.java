package utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Binary {
    public static String getBinary(int number, int length) {
        StringBuilder result = new StringBuilder();
        for (int i = length; i >= 0; i--) {
            int mask = 1 << i;
            result.append((number & mask) != 0 ? "1" : "0");
        }
        return result.toString();
    }

    public static byte getBinary(String number) {
        return (byte)Integer.parseInt(number,2);
    }
    public static int toByte(int number) {
        int tmp = number & 0xff;
        return (tmp & 0x80) == 0 ? tmp : tmp - 256;
    }

    public static byte getBinary(List<Integer> number) {
        int result = 0;
        int size = number.size();
        for(int i = 0; i < size; i++) {
           if(number.get(i) == 1) {
               result += Math.pow(2, size - i - 1);
           }
        }

        return (byte) result;
    }

    public static boolean[] toBits(Byte val) {
        boolean[] bitArray = new boolean[8];
        for (int i = 0; i < 8; i++) {
            bitArray[i] = (val >> (8 - (i + 1)) & 0x0001) == 1;
        }

        return bitArray;
    }

    public static Byte toByte(boolean[] bitArray) {
        int n = 0, l = bitArray.length;
        for (int i = 0; i < l; ++i) {
            n = (n << 1) + (bitArray[i] ? 1 : 0);
        }
        return (byte) n;
    }

//    public static int toInt(boolean[] bitArray) {
//        int result = 0;
//        int currVal = 1;
//        for(int i = 7; i >= 0; i--) {
//            if(bitArray[i]) {
//                result += currVal;
//            }
//            currVal *= 2;
//        }
//        return result;
//    }

}
