/*
 * Project: Blackboard
 *
 * @author: Thomas Fidorin and Djordy v. Rönn
 */
package de.fh_muenster.blackboard.scripting;

import de.fh_muenster.blackboard.Blackboard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Stack;
import java.util.function.Function;

import static java.lang.Double.valueOf;

//TODO Write the function.
public class FunctionNode extends AstNode<String> implements java.util.function.Function<double[],Double>, Cloneable {
    private AST<?> variables;
    //Operations for future implementation of andThen and compose methods. When using normal doubles the list will save the values.
    private ArrayList<AST<?>> variablesOperations;
    FunctionNode(String function, AST<?> variables) {
        super(null, function);
        this.variables = variables;
        this.childs().add(variables);
        variables.setParent(this);
    }
    //TODO Write visitor for the function.
    @Override
    public Double apply(double[] doubles) {
        return this.accept(new FunctionVisitor()).apply(doubles);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    //TODO Write the tree iterator. My thought was iterating the and changing the labels to vriable operations.
    private void treeIteration(FunctionNode noodleNode, double[] doubles, ArrayList<String> variables){
        AstNode<?> iterator = (AstNode<?>) noodleNode.parent().childs().get(1);
        Stack<AstNode<?>> stag = new Stack<AstNode<?>>();
        stag.push(iterator);

        while(!(stag.isEmpty())) {
            iterator = stag.pop();
            for (AST<?> toPush : iterator.childs()) {
                stag.push((AstNode<?>) toPush);
            }
            if (iterator instanceof Label) {
                int index = variables.lastIndexOf(iterator.toString());
                if (index != -1) {
                    DoubleValue dv = new DoubleValue(doubles[index]);
                    AstNode<?> parent = (AstNode<?>) iterator.parent();
                    for (int i = 0; i < parent.childs().size(); i++) {
                        if (parent.childs().get(i).equals(iterator)) {
                            parent.childs().set(i, dv);
                            dv.setParent(parent);
                        }
                    }
                }
            }
        }
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
    public <V> V accept(AstVisitor<V> visitor) {
        return visitor.visit(this);
    }

    @Override
    public boolean isRoot() {
        return super.isRoot();
    }

    @Override
    public boolean isLeaf() {
        return super.isLeaf();
    }



    @Override
    public String toString() {
        return String.format("\"%s\"{%s}", data(), variables);
    }
}
