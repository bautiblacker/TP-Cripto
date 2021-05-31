package utils;

import java.util.List;

public class Binary {
    public static String getBinary(int number) {
        StringBuilder result = new StringBuilder();
        for (int i = 15; i >= 0; i--) {
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

}
