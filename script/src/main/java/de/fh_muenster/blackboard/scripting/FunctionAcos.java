package de.fh_muenster.blackboard.scripting;

import java.util.function.Function;

public class FunctionAcos implements Function<double [], Double> {

    private FunctionNode function;


    private Function<double [], Double> right;

    public FunctionAcos(Function<double[], Double> right) {
        this.right = right;
    }

    public FunctionNode getFunction() {
        return function;
    }

    public void setFunction(FunctionNode function) {
        this.function = function;
    }

    @Override
    public Double apply(double[] doubles) {
        double rightSide = right.apply(doubles);
        if (rightSide > 1 || rightSide < -1) {
            throw new IllegalArgumentException("Der Wert ist ungeeignet für ACos: " + rightSide);
        }
        return Math.acos(rightSide);
    }

    @Override
    public <V> Function<V, Double> compose(Function<? super V, ? extends double[]> before) {
        return Function.super.compose(before);
    }

    @Override
    public <V> Function<double[], V> andThen(Function<? super Double, ? extends V> after) {
        return Function.super.andThen(after);
    }
}
