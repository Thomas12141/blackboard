package de.fh_muenster.blackboard.scripting;

import java.util.ArrayList;
import java.util.function.Function;

public abstract class AbstractFunction implements Function<double[], Double> {
    ArrayList<Function<double[], Double>> childs =new ArrayList<>();

    public ArrayList<Function<double[], Double>> getChilds() {
        return childs;
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
