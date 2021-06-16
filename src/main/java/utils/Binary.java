package utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Binary {

    public static Map<Integer, Integer> getMappingByte(){
        Map<Integer, Integer> map = new HashMap<>();
        map.put(32768,15);
        map.put(16384,14);
        map.put(8192,13);
        map.put(4096,12);
        map.put(2048,11);
        map.put(1024,10);
        map.put(512,9);
        map.put(256,8);
        map.put(128,7);
        map.put(64,6);
        map.put(32,5);
        map.put(16,4);
        map.put(8,3);
        map.put(4,2);
        map.put(2,1);
        map.put(1,0);
        return map;
    }

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

    public static Byte toInt(boolean[] bitArray) {
        byte value = 0;
        for(int i = 0; i < 8; i++) {
            if(bitArray[i]) {
                value |= (byte)(1 << i);
            }
        }
        return value;
    }

}
