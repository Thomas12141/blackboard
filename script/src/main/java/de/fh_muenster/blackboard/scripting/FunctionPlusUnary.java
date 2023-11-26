package de.fh_muenster.blackboard.scripting;

import java.util.function.Function;

public class FunctionPlusUnary extends AbstractFunction{

    private FunctionNode function;

    private Function<double [], Double> child;

    public FunctionPlusUnary(Function<double[], Double> child) {
        ((AbstractFunction)child).parent = this;
        childs.add(child);
        this.child = childs.get(0);
    }

    public FunctionNode getFunction() {
        return function;
    }

    public void setFunction(FunctionNode function) {
        this.function = function;
    }

    @Override
    public Double apply(double[] doubles) {
        return child.apply(doubles);
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
        return ("(+" + child.toString() + ")");
    }
}
