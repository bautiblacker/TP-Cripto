package models;

import interfaces.MathExpressions;
import utils.GaloisField;

import java.util.ArrayList;
import java.util.List;

public class MathFunction implements MathExpressions<MultExpression> {
    List<MultExpression> function;
    /*
    F(x) = s1 * x + s2 * x^2 + .... + sk*x^k-1
     */

    public MathFunction(){
        function = new ArrayList<>();
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
            response = GaloisField.add(response, multExpression.eval());
        }
        //TODO: Should cast to byte?? What happens if it returns 285 for instance?
        return GaloisField.moduleReducer(response,355);
    }

    public void fill(Byte x) {
        for(int i = 0; i < function.size(); i++) {
            for(int j = 0; j < i; j++) {
                function.get(i).addExpression(x);
            }
        }
    }
}
