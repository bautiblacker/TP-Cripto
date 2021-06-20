package models;

import interfaces.MathExpressions;
import utils.GaloisField;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MathFunction implements MathExpressions<MultExpression> {
    List<MultExpression> function;
    /*
    F(x) = s1 * x + s2 * x^2 + .... + sk*x^k-1
     */

    public MathFunction(){
        function = new ArrayList<>();
    }

    public MathFunction(MathFunction original){
        Collections.copy(function, original.function);
    }

    public void addExpression(MultExpression multExpression){
        function.add(multExpression);
    }

    /***
     * Sumar y modulo
     * @return deberia ser int o byte (not sure)
     */
    public byte eval(){
        byte response = 0;
        for(MultExpression multExpression: function){
            response = GaloisField.moduleReducer(Byte.toUnsignedInt(GaloisField.add(response, multExpression.eval())), 355);
        }
        //TODO: Should cast to byte?? What happens if it returns 285 for instance?
        return GaloisField.moduleReducer(Byte.toUnsignedInt(response),355);
    }

    public void fill(Byte x) {
        for(int i = 0; i < function.size(); i++) {
            for(int j = 0; j < i; j++) {
                function.get(i).addExpression(x);
            }
        }
    }

    public void reset(){
        for(MultExpression multExpression : function){
            Byte aux = multExpression.subFunction.get(0); // remember first item
            multExpression.subFunction.clear(); // clear complete list
            multExpression.subFunction.add(aux); // add first item
        }
    }
}
