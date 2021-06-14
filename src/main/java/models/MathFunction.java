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
    public int eval(){
        int response = 0;
        for(MultExpression multExpression: function){
            response = Byte.toUnsignedInt(GaloisField.add((byte)response,(byte)multExpression.eval()));
        }
        //TODO: Should cast to byte?? What happens if it returns 285 for instance?
        return Byte.toUnsignedInt(GaloisField.moduleReducer(response,355));
    }
    public static void main(String[] args){
        MultExpression multExpression1 = new MultExpression();
        multExpression1.addExpression(2);
        multExpression1.addExpression(45);
        MultExpression multExpression2 = new MultExpression();
        multExpression2.addExpression(2);
        multExpression2.addExpression(45);
        multExpression2.addExpression(45);
        MultExpression multExpression3 = new MultExpression();
        multExpression3.addExpression(2);
        multExpression3.addExpression(45);
        multExpression3.addExpression(45);
        multExpression3.addExpression(45);
        MultExpression multExpression4 = new MultExpression();
        multExpression4.addExpression(2);
        multExpression4.addExpression(45);
        multExpression4.addExpression(45);
        multExpression4.addExpression(45);
        multExpression4.addExpression(45);
        MultExpression multExpression5 = new MultExpression();
        multExpression5.addExpression(2);
        multExpression5.addExpression(45);
        multExpression5.addExpression(45);
        multExpression5.addExpression(45);
        multExpression5.addExpression(45);
        multExpression5.addExpression(45);
        MultExpression multExpression6 = new MultExpression();
        multExpression6.addExpression(2);
        multExpression6.addExpression(45);
        multExpression6.addExpression(45);
        multExpression6.addExpression(45);
        multExpression6.addExpression(45);
        multExpression6.addExpression(45);
        multExpression6.addExpression(45);
        MathFunction mathFunction = new MathFunction();
        mathFunction.addExpression(multExpression1);
        mathFunction.addExpression(multExpression2);
        mathFunction.addExpression(multExpression3);
        mathFunction.addExpression(multExpression4);
        mathFunction.addExpression(multExpression5);
        System.out.println(mathFunction.eval());
        /*MultExpression multExpression1 = new MultExpression();
        multExpression1.addExpression(7);
        multExpression1.addExpression(7);
        multExpression1.addExpression(7);
        MathFunction mathFunction = new MathFunction();
        mathFunction.addExpression(multExpression1);
        System.out.println(mathFunction.eval());*/

    }
}
