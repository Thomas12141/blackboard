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
public class FunctionNode extends AstNode<String>{
    private ArrayList<String> variableList;

    private Function<double [], Double> functionCall;


    FunctionNode(String function, AST<?> variables) {
        super(null, function);

        if (variables instanceof VariableNode) {
            variableList = new ArrayList<>();
            AST<?> iterator = variables;
            while (iterator != null) {
                if (iterator instanceof Label) {
                    variableList.add((String) iterator.data());
                    iterator = null;
                    break;
                }
                if (iterator.childs().isEmpty()) break;
                if (iterator.childs().get(0) instanceof Label) {
                    variableList.add((String) iterator.childs().get(0).data());
                }
                iterator = iterator.childs().get(1);
            }
        }
        this.childs().add(variables);
        variables.setParent(this);
    }



    public void setFunctionCall(Function<double [], Double> functionCall) {
        this.functionCall = functionCall;
    }

    public Function<double[], Double> getFunctionCall() {
        return functionCall;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public ArrayList<String> getVariables() {
        return variableList;
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
        return String.format("\"%s\"{%s}", data(), this.childs().get(0).toString());
    }
}
