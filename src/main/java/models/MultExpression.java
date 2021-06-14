package models;

import interfaces.MathExpressions;
import utils.GaloisField;

import java.util.ArrayList;
import java.util.List;

public class MultExpression implements MathExpressions<Integer> {
    //TODO: Should be byte??
    List<Integer> subFunction;
    public MultExpression(){
        subFunction = new ArrayList<>();
    }
    public void addExpression(Integer exp){
        subFunction.add(exp);
    }
    public int eval() {
        int resp = subFunction.get(0);
        for(int i = 1;i < subFunction.size();i++){
            resp = Byte.toUnsignedInt(GaloisField.moduleReducer(GaloisField.product(resp,subFunction.get(i)),355));
        }
        return resp;
    }
}
