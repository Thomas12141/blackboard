package de.fh_muenster.blackboard.scripting;

import java.util.function.Function;

public class FunctionMinusUnary extends AbstractFunction {

    private FunctionNode function;

    private Function<double [], Double> child;

    public FunctionMinusUnary(Function<double[], Double> child) {
        this.child = child;
        childs.add(child);
    }

    public FunctionNode getFunction() {
        return function;
    }

    public void setFunction(FunctionNode function) {
        this.function = function;
    }

    @Override
    public Double apply(double[] doubles) {
        return -child.apply(doubles);
    }

    @Override
    public <V> Function<V, Double> compose(Function<? super V, ? extends double[]> before) {
        return super.compose(before);
    }

    @Override
    public <V> Function<double[], V> andThen(Function<? super Double, ? extends V> after) {
        return super.andThen(after);
    }

    public String toString() {
        return ("(-" + child.toString() + ")");
    }

    public Function<double[], Double> getChild() {
        return child;
    }

    public void setChild(Function<double[], Double> child) {
        this.child = child;
    }
}
