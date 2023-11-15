package de.fh_muenster.blackboard.scripting;

import java.util.function.Function;

public class FunctionCall implements Function<double [], Double> {

    private FunctionNode function;

    public FunctionCall(Function<double[], Double> exp) {
        super();
    }

    public FunctionNode getFunction() {
        return function;
    }

    public void setFunction(FunctionNode function) {
        this.function = function;
    }

    @Override
    public Double apply(double[] doubles) {
        return null;
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
