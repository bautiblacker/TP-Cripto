package models;

import interfaces.MathExpressions;
import utils.GaloisField;

import java.util.ArrayList;
import java.util.List;

public class MultExpression implements MathExpressions<Integer> {
    List<Integer> subFunction;
    MultExpression(){
        subFunction = new ArrayList<>();
    }
    public void addExpression(Integer exp){
        subFunction.add(exp);
    }
    public int eval() {
        int resp = subFunction.get(0);
        for(int i = 1;i<subFunction.size();i++){
            //TODO: Should i apply module here??? Or just a normal multiplication using galois field
            //TODO: I think i should, i might have some overflow issues if not
            resp += GaloisField.moduleReducer(GaloisField.product(resp,subFunction.get(i)),355);
        }
        return resp;
    }
}
