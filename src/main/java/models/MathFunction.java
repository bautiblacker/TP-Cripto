package models;

import interfaces.MathExpressions;
import utils.GaloisField;

import java.util.ArrayList;
import java.util.List;

public class MathFunction implements MathExpressions<MultExpression> {
    List<MultExpression> function;

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
    public int eval(){
        int response = 0;
        for(MultExpression multExpression: function){
            response += multExpression.eval();
        }
        //TODO: Should cast to byte?? What happens if it returns 285 for instance?
        return GaloisField.moduleReducer(response,355);
    }
}
