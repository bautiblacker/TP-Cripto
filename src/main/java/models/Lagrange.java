package models;

import javafx.util.Pair;
import utils.GaloisField;

import java.util.List;
import java.util.Map;

public class Lagrange {

    public static byte lagrangeInterpolation(List<Pair<Byte, Byte>> valuePairs, Byte xToEvaluate, Map<Byte,Byte> lookUpTable) {
        byte result = 0; // Initialize result
        int n = valuePairs.size();
        for (int i=0; i<n; i++) {
            // Compute individual terms of above formula
            byte term = valuePairs.get(i).getValue();

            for (int j=0;j<n;j++) {
                if (j!=i){

                    //term = (byte) (term*(xToEvaluate - valuePairs.get(j).getKey())/(valuePairs.get(i).getKey() - valuePairs.get(j).getKey()));
                    byte aux1 = GaloisField.add(valuePairs.get(i).getKey(),valuePairs.get(j).getKey());
                    byte aux2 = GaloisField.add(xToEvaluate,valuePairs.get(j).getKey());
                    byte prod = GaloisField.moduleReducer(GaloisField.product(Byte.toUnsignedInt(term),Byte.toUnsignedInt(aux2), 355), 355);
                    term = GaloisField.divideWithMap(prod,aux1,lookUpTable);
                    //term = GaloisField.divide(prod,aux1);
                }
            }
            // Add current term to result
            result = GaloisField.moduleReducer(Byte.toUnsignedInt(GaloisField.add(result,term)),355);
            //result += term;
        }

        return result;
    }



}
