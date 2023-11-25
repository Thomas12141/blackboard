package de.fh_muenster.blackboard.scripting;

import java.util.function.Function;

public class FunctionLogB extends AbstractFunction {

    private FunctionNode function;


    private Function<double [], Double> child;

    public FunctionLogB(Function<double[], Double> child) {
        childs.add(child);
        this.child = child;
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
        if (rightSide <= 0) {
            throw new IllegalArgumentException("LogB erlaubt keine Werte kleiner-gleich 0.");
        }
        return Math.log(rightSide)/Math.log(2);
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
        return ("lb(" + child.toString() + ")");
    }
}
