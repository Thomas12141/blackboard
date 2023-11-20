package de.fh_muenster.blackboard.scripting;

import java.util.function.Function;

public class FunctionLabel implements Function<double [], Double> {

    private int position;
    public FunctionLabel(int position){
        this.position = position;
    }
    @Override
    public Double apply(double[] doubles) {
        return doubles[position];
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
