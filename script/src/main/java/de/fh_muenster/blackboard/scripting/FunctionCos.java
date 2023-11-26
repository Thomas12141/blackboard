package de.fh_muenster.blackboard.scripting;

import java.util.function.Function;

public class FunctionCos extends AbstractFunction {

    private FunctionNode function;


    private Function<double [], Double> child;

    public FunctionCos(Function<double[], Double> child) {
        ((AbstractFunction)child).parent = this;
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
        double rightSide = child.apply(doubles);
        return Math.cos(rightSide);
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
        return ("cos(" + child.toString() + ")");
    }
}
