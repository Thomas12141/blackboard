package de.fh_muenster.blackboard.scripting;

import java.util.function.Function;

public class FunctionPlus extends AbstractFunction {

    private FunctionNode function;

    private Function<double [], Double> left;

    private Function<double [], Double> right;

    public FunctionPlus(Function<double[], Double> left, Function<double[], Double> right) {
        ((AbstractFunction)left).parent = this;
        ((AbstractFunction)right).parent = this;
        childs.add(left);
        childs.add(right);
        this.left = childs.get(0);
        this.right = childs.get(1);
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
        return super.compose(before);
    }

    @Override
    public <V> Function<double[], V> andThen(Function<? super Double, ? extends V> after) {
        return super.andThen(after);
    }

    public Function<double[], Double> getLeft() {
        return left;
    }

    public Function<double[], Double> getRight() {
        return right;
    }

    public String toString() {
        return (left.toString() + "+" + right.toString());
    }
}
