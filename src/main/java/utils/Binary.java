package utils;

import java.util.List;

public class Binary {
    public static String getBinary(int number) {
        StringBuilder result = new StringBuilder();
        for (int i = 7; i >= 0; i--) {
            int mask = 1 << i;
            result.append((number & mask) != 0 ? "1" : "0");
        }
        return result.toString();
    }
}
