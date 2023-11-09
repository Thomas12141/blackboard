/*
 * Project: Blackboard
 *
 * @author: Thomas Fidorin and Djordy v. Rönn
 */
package de.fh_muenster.blackboard.scripting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Function;

//TODO Write the function.
public class FunctionNode extends AstNode<String> implements java.util.function.Function<double[],Double> {
    ArrayList<String> variables;
    //Operations for future implementation of andThen and compose methods. When using normal doubles the list will save the values.
    ArrayList<AST<?>> variablesOperations;
    FunctionNode(AST<?> child, String variables) {
        super(null, "function");
        this.variables = new ArrayList<String>(Arrays.asList(variables.split(",")));
        super.childs().add(child);
        child.setParent(this);
    }
    //TODO Write visitor for the function.
    @Override
    public Double apply(double[] doubles) {
        variablesOperations = new ArrayList<AST<?>>();
        for (double iterator:doubles) {
            variablesOperations.add(new DoubleValue(iterator));
        }
        if(variables.size()!=variablesOperations.size()){
            throw new IllegalArgumentException("Diese Funktion braucht andere Anzahl an Werten.");
        }

        return null;
    }
    //TODO Write the tree iterator. My thought was iterating the and changing the labels to vriable operations.
    private void treeIteration(){

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
