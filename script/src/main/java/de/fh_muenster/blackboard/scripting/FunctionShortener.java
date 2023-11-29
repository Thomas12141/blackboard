package de.fh_muenster.blackboard.scripting;

import java.util.Stack;
import java.util.function.Function;

public class FunctionShortener {
    public static Function<double[], Double> toShort(Function<double[], Double> toShort){
        FunctionPlusUnary temp = new FunctionPlusUnary(toShort);
        Stack<Function<double[], Double>> myStack = new Stack<>();
        myStack.push(toShort);
        Function<double[], Double> iterator;
        boolean again = false;
        while (!myStack.empty()){
            iterator = myStack.pop();
            for (Function<double[], Double> toPush:((AbstractFunction)iterator).getChilds()) {
                myStack.push(toPush);
            }
            again = again || helper(iterator);
        }
        if(again){
            temp.setChild((AbstractFunction) toShort(temp.child));
        }
        return temp.child;
    }

    private static boolean helper(Function<double[], Double> toShort){
        AbstractFunction parent = ((AbstractFunction) toShort).parent;
        if(toShort instanceof FunctionPlus){
            AbstractFunction firstChild = ((FunctionPlus)toShort).getLeft();
            AbstractFunction secondChild = ((FunctionPlus)toShort).getRight();
            if(firstChild instanceof FunctionDoubleValue && ((FunctionDoubleValue) firstChild).getValue() == 0.0){
                if(parent.childs.size() == 2) {
                    if(parent.childs.get(0) == toShort) {
                        ((AbstractFunctionTwoVariable)parent).setLeft(secondChild);
                        ((AbstractFunctionTwoVariable)parent).getLeft().setParent(parent);
                    } else {
                        ((AbstractFunctionTwoVariable)parent).setRight(secondChild);
                        ((AbstractFunctionTwoVariable)parent).getRight().setParent(parent);
                    }
                } else {
                    ((AbstractFunctionOneVariable)parent).setChild(secondChild);
                    ((AbstractFunctionOneVariable)parent).child.setParent(parent);
                }
                parent.childs.set(parent.childs.indexOf(toShort), secondChild);
                return true;
            }
            if(secondChild instanceof FunctionDoubleValue && ((FunctionDoubleValue) secondChild).getValue() == 0.0){
                if(parent.childs.size() == 2) {
                    if(parent.childs.get(0) == toShort) {
                        ((AbstractFunctionTwoVariable)parent).setLeft(firstChild);
                        ((AbstractFunctionTwoVariable)parent).getLeft().setParent(parent);
                    } else {
                        ((AbstractFunctionTwoVariable)parent).setRight(firstChild);
                        ((AbstractFunctionTwoVariable)parent).getRight().setParent(parent);
                    }
                } else {
                    ((AbstractFunctionOneVariable)parent).setChild(firstChild);
                    ((AbstractFunctionOneVariable)parent).child.setParent(parent);
                }
                parent.childs.set(parent.childs.indexOf(toShort), firstChild);
                return true;
            }
        }else if(toShort instanceof FunctionMinusBinary){
            AbstractFunction firstChild = ((FunctionMinusBinary)toShort).getLeft();
            AbstractFunction secondChild = ((FunctionMinusBinary)toShort).getRight();
            if(firstChild instanceof FunctionDoubleValue && ((FunctionDoubleValue) firstChild).getValue() == 0.0){
                FunctionMinusUnary value;
                if(parent.childs.size() == 2) {
                    if(parent.childs.get(0) == toShort) {
                        value = new FunctionMinusUnary(secondChild);
                        ((AbstractFunctionTwoVariable)parent).setLeft(value);
                        ((AbstractFunctionTwoVariable)parent).getLeft().setParent(parent);
                        parent.childs.set(parent.childs.indexOf(toShort), value);
                        return true;
                    } else {
                        ((AbstractFunctionTwoVariable)parent).setRight(secondChild);
                        ((AbstractFunctionTwoVariable)parent).getRight().setParent(parent);
                        parent.childs.set(parent.childs.indexOf(toShort), secondChild);
                        return true;
                    }
                } else {
                    value = new FunctionMinusUnary(secondChild);
                    ((AbstractFunctionOneVariable)parent).setChild(value);
                    ((AbstractFunctionOneVariable)parent).child.setParent(parent);
                    parent.childs.set(parent.childs.indexOf(toShort), value);
                    return true;
                }
            }
            if(secondChild instanceof FunctionDoubleValue && ((FunctionDoubleValue) secondChild).getValue() == 0.0){
                if(parent.childs.size() == 2) {
                    if(parent.childs.get(0) == toShort) {
                        ((AbstractFunctionTwoVariable)parent).setLeft(firstChild);
                        ((AbstractFunctionTwoVariable)parent).getLeft().setParent(parent);
                    } else {
                        ((AbstractFunctionTwoVariable)parent).setRight(firstChild);
                        ((AbstractFunctionTwoVariable)parent).getRight().setParent(parent);
                    }
                } else {
                    ((AbstractFunctionOneVariable)parent).setChild(firstChild);
                    ((AbstractFunctionOneVariable)parent).child.setParent(parent);
                }
                parent.childs.set(parent.childs.indexOf(toShort), firstChild);
                return true;
            }
        }else if(toShort instanceof FunctionMinusUnary){
            AbstractFunction firstChild = ((FunctionMinusUnary)toShort).getChild();
            if(firstChild instanceof FunctionDoubleValue && ((FunctionDoubleValue) firstChild).getValue() == 0.0){
                ((AbstractFunctionOneVariable)parent).setChild(firstChild);
                ((AbstractFunctionOneVariable)parent).child.setParent(parent);
                parent.childs.set(parent.childs.indexOf(toShort), firstChild);
                return true;
            }

        }else if(toShort instanceof FunctionTimes){
            AbstractFunction firstChild = ((FunctionTimes)toShort).getLeft();
            AbstractFunction secondChild = ((FunctionTimes)toShort).getRight();
            if(firstChild instanceof FunctionDoubleValue && ((FunctionDoubleValue) firstChild).getValue() == 0.0){
                if(parent.childs.size() == 2) {
                    if(parent.childs.get(0) == toShort) {
                        ((AbstractFunctionTwoVariable)parent).setLeft(firstChild);
                        ((AbstractFunctionTwoVariable)parent).getLeft().setParent(parent);
                    } else {
                        ((AbstractFunctionTwoVariable)parent).setRight(firstChild);
                        ((AbstractFunctionTwoVariable)parent).getRight().setParent(parent);
                    }
                } else {
                    ((AbstractFunctionOneVariable)parent).setChild(firstChild);
                    ((AbstractFunctionOneVariable)parent).child.setParent(parent);
                }
                parent.childs.set(parent.childs.indexOf(toShort), firstChild);
                return true;
            }
            if(secondChild instanceof FunctionDoubleValue && ((FunctionDoubleValue) secondChild).getValue() == 0.0){
                if(parent.childs.size() == 2) {
                    if(parent.childs.get(0) == toShort) {
                        ((AbstractFunctionTwoVariable)parent).setLeft(secondChild);
                        ((AbstractFunctionTwoVariable)parent).getLeft().setParent(parent);
                    } else {
                        ((AbstractFunctionTwoVariable)parent).setRight(secondChild);
                        ((AbstractFunctionTwoVariable)parent).getRight().setParent(parent);
                    }
                } else {
                    ((AbstractFunctionOneVariable)parent).setChild(secondChild);
                    ((AbstractFunctionOneVariable)parent).child.setParent(parent);
                }
                parent.childs.set(parent.childs.indexOf(toShort), secondChild);
                return true;
            }
            if(firstChild instanceof FunctionDoubleValue && ((FunctionDoubleValue) firstChild).getValue() == 1.0){
                if(parent.childs.size() == 2) {
                    if(parent.childs.get(0) == toShort) {
                        ((AbstractFunctionTwoVariable)parent).setLeft(secondChild);
                        ((AbstractFunctionTwoVariable)parent).getLeft().setParent(parent);
                    } else {
                        ((AbstractFunctionTwoVariable)parent).setRight(secondChild);
                        ((AbstractFunctionTwoVariable)parent).getRight().setParent(parent);
                    }
                } else {
                    ((AbstractFunctionOneVariable)parent).setChild(secondChild);
                    ((AbstractFunctionOneVariable)parent).child.setParent(parent);
                }
                parent.childs.set(parent.childs.indexOf(toShort), secondChild);
                return true;
            }
            if(secondChild instanceof FunctionDoubleValue && ((FunctionDoubleValue) secondChild).getValue() == 1.0){
                if(parent.childs.size()==2){
                    if(parent.childs.get(0)==toShort){
                        ((AbstractFunctionTwoVariable)parent).setLeft(firstChild);
                        ((AbstractFunctionTwoVariable) parent).getLeft().setParent(parent);
                    }else {
                        ((AbstractFunctionTwoVariable)parent).setRight(firstChild);
                        ((AbstractFunctionTwoVariable) parent).getRight().setParent(parent);
                    }
                }else {
                    ((AbstractFunctionOneVariable)parent).setChild(firstChild);
                    ((AbstractFunctionOneVariable) parent).child.setParent(parent);
                }
                parent.childs.set(parent.childs.indexOf(toShort), firstChild);
                return true;
            }
        }else if(toShort instanceof FunctionDivide){
            AbstractFunction firstChild = ((FunctionDivide)toShort).getLeft();
            AbstractFunction secondChild = ((FunctionDivide)toShort).getRight();
            if(firstChild instanceof FunctionDoubleValue && ((FunctionDoubleValue) firstChild).getValue() == 0.0){
                FunctionDoubleValue value = new FunctionDoubleValue(0.0);
                if(parent.childs.size() == 2) {
                    if(parent.childs.get(0) == toShort) {
                        ((AbstractFunctionTwoVariable)parent).setLeft(value);
                        ((AbstractFunctionTwoVariable)parent).getLeft().setParent(parent);
                    }
                } else {
                    ((AbstractFunctionOneVariable)parent).setChild(value);
                    ((AbstractFunctionOneVariable)parent).child.setParent(parent);
                }
                parent.childs.set(parent.childs.indexOf(toShort), value);
                return true;
            }
            if(secondChild instanceof FunctionDoubleValue && ((FunctionDoubleValue) secondChild).getValue() == 1.0){
                if(parent.childs.size() == 2) {
                    if(parent.childs.get(1) == toShort) {
                        ((AbstractFunctionTwoVariable)parent).setLeft(firstChild);
                        ((AbstractFunctionTwoVariable)parent).getLeft().setParent(parent);
                    }
                } else {
                    ((AbstractFunctionOneVariable)parent).setChild(firstChild);
                    ((AbstractFunctionOneVariable)parent).child.setParent(parent);
                }
                parent.childs.set(parent.childs.indexOf(toShort), firstChild);
                return true;
            }
        }
        return false;
    }
}
