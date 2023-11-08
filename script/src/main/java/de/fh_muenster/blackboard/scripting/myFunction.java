/*
 * Project: Blackboard
 *
 * @author: Thomas Fidorin and Djordy v. Rönn
 */
package de.fh_muenster.blackboard.scripting;

import java.util.function.Function;

/**
 * Various auxiliary classes - bundled in one class.
 */
public class myFunction extends AstNode implements java.util.function.Function<double[],Double> {

    myFunction(AST p, Object d) {
        super(p, d);
    }

    @Override
    public Double apply(double[] doubles) {
        return null;
    }

    @Override
    public <V> Function<V, Double> compose(Function<? super V, ? extends double[]> before) {
        return Function.super.compose(before);
    }

    @Override
    public <V> Function<double[], V> andThen(Function<? super Double, ? extends V> after) {
        return Function.super.andThen(after);
    }

    @Override
    public Object accept(AstVisitor visitor) {
        return null;
    }

    @Override
    public boolean isRoot() {
        return super.isRoot();
    }

    @Override
    public boolean isLeaf() {
        return super.isLeaf();
    }
}
