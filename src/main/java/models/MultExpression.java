package models;

import interfaces.MathExpressions;
import utils.GaloisField;

import java.util.ArrayList;
import java.util.List;

public class MultExpression implements MathExpressions<Byte> {
    //TODO: Should be byte??
    List<Byte> subFunction;
    public MultExpression(){
        subFunction = new ArrayList<>();
    }

    public void addExpression(Byte exp){
        subFunction.add(exp);
    }

    public byte eval() {
        byte resp = subFunction.get(0);
        for(int i = 1; i < subFunction.size(); i++){
            resp = GaloisField.moduleReducer(GaloisField.product(Byte.toUnsignedInt(resp),Byte.toUnsignedInt(subFunction.get(i))), 355);
        }
        return resp;
    }
}
