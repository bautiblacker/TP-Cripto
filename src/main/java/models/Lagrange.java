package models;

import javafx.util.Pair;

import java.util.List;

public class Lagrange {

    public static byte lagrangeInterpolation(List<Pair<Byte, Byte>> valuePairs, Byte xToEvaluate) {
        byte result = 0; // Initialize result
        int n = valuePairs.size();
        for (int i=0; i<n; i++) {
            // Compute individual terms of above formula
            byte term = valuePairs.get(i).getValue();

            for (int j=0;j<n;j++) {
                if (j!=i){
                    if (valuePairs.get(i).getKey().equals(valuePairs.get(j).getKey())) {
                        System.out.println("Tuvi");
                    }
                    term = (byte) (term*(xToEvaluate - valuePairs.get(j).getKey())/(valuePairs.get(i).getKey() - valuePairs.get(j).getKey()));

                }
            }

            // Add current term to result
            result += term;
        }

        return result;
    }


}
