package de.fh_muenster.blackboard.scripting;

import java.util.function.Function;

public class FunctionDoubleValue extends AbstractFunctionOneVariable {

    private FunctionNode function;



    private Double value;

    public FunctionDoubleValue(Double value) {
        this.value = value;
    }
    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }
    public FunctionNode getFunction() {
        return function;
    }

    public void setFunction(FunctionNode function) {
        this.function = function;
    }

    @Override
    public Double apply(double[] doubles) {
        return value;
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
        return (value.toString());
    }
}
