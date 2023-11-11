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
    private ArrayList<Label> variables;
    //Operations for future implementation of andThen and compose methods. When using normal doubles the list will save the values.
    private ArrayList<AST<?>> variablesOperations;
    FunctionNode(String function, AST<?> variables) {
        super(null, function);
    }
    //TODO Write visitor for the function.
    @Override
    public Double apply(double[] doubles) {
        this.variablesOperations = new ArrayList<AST<?>>();

        for (double iterator:doubles) {
            this.variablesOperations.add(new DoubleValue(iterator));
        }
        if(variables.size() != this.variablesOperations.size()){
            throw new IllegalArgumentException("Diese Funktion braucht andere Anzahl an Werten.");
        }

        treeIteration(this, doubles);
        return this.childs().get(0).accept(new ValueVisitor());


    }
    //TODO Write the tree iterator. My thought was iterating the and changing the labels to vriable operations.
    private void treeIteration(FunctionNode noodleNode, double[] doubles){

        AstNode<?> iterator = (AstNode<?>) noodleNode.childs().get(0);
        Stack<AstNode<?>> stag = new Stack<AstNode<?>>();
        stag.push(iterator);

        while(!(stag.isEmpty())) {
            iterator = stag.pop();
            for (AST<?> toPush : iterator.childs()) {
                stag.push((AstNode<?>) toPush);
            }
            if (iterator instanceof Label) {
                int index = noodleNode.variables.lastIndexOf(iterator.toString());
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

    public ArrayList<Label> getVariables() {
        return new ArrayList<Label>(variables);
    }

    @Override
    public String toString() {

        String myStr = variables.get(0).data();
        for (int i = 1; i < variables.size(); i++) {
            myStr += ","+ variables.get(i);
        }
        try {
            if(this.childs().get(0)!=null){
                return String.format("\"%s\"{%s}", this.data()+"("+ myStr+")", this.childs().get(0));
            }
            else {
                return String.format("\"%s\"{%s}", this.data()+"("+ myStr+")");
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("unknown function or too few #args");
        }
    }
}
