package de.fh_muenster.blackboard.scripting;

import java.util.function.Function;

public class FunctionLabel extends AbstractFunction {

    private int position;

    private String label;
    public FunctionLabel(int position, String label){
        this.position = position;
        this.label = label;
    }
    @Override
    public Double apply(double[] doubles) {
        return doubles[position];
    }

    @Override
    public <V> Function<V, Double> compose(Function<? super V, ? extends double[]> before) {
        return super.compose(before);
    }

    @Override
    public <V> Function<double[], V> andThen(Function<? super Double, ? extends V> after) {
        return super.andThen(after);
    }

    @Override
    public String toString() {
        return label;
    }
}
