package de.fh_muenster.blackboard.scripting;

import java.util.ArrayList;
import java.util.function.Function;

public abstract class AbstractFunction implements Function<double[], Double>, Cloneable {
    ArrayList<Function<double[], Double>> childs =new ArrayList<>();
    AbstractFunction parent;

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

    public AbstractFunction getParent() {
        return parent;
    }

    @Override
    public AbstractFunction clone() {
        try {
            AbstractFunction clone = (AbstractFunction) super.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
