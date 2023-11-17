package de.fh_muenster.blackboard.scripting;

import java.util.function.Function;

public class FunctionPlus implements Function<double [], Double> {

    private FunctionNode function;

    private Function<double [], Double> left;

    private Function<double [], Double> right;

    public FunctionPlus(Function<double[], Double> left, Function<double[], Double> right) {
        this.left = left;
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
        return left.apply(doubles) + right.apply(doubles);
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
