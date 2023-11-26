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
            toShort(temp.child);
        }
        return temp.child;
    }

    private static boolean helper(Function<double[], Double> toShort){
        AbstractFunction parent = ((AbstractFunction) toShort).parent;
        if(toShort instanceof FunctionPlus){
            Function<double[], Double> firstChild = ((FunctionPlus)toShort).getLeft();
            Function<double[], Double> secondChild = ((FunctionPlus)toShort).getRight();
            if(firstChild instanceof FunctionDoubleValue && ((FunctionDoubleValue) firstChild).getValue() == 0.0){
                parent.childs.set(parent.childs.indexOf(toShort), secondChild);
                toShort = secondChild;
                return true;
            }
            if(secondChild instanceof FunctionDoubleValue && ((FunctionDoubleValue) secondChild).getValue() == 0.0){
                parent.childs.set(parent.childs.indexOf(toShort), firstChild);
                toShort = firstChild;
                return true;
            }
        }else if(toShort instanceof FunctionMinusBinary){
            Function<double[], Double> firstChild = ((FunctionMinusBinary)toShort).getLeft();
            Function<double[], Double> secondChild = ((FunctionMinusBinary)toShort).getRight();
            if(firstChild instanceof FunctionDoubleValue && ((FunctionDoubleValue) firstChild).getValue() == 0.0){
                AbstractFunction newFunction = new FunctionMinusUnary(secondChild);
                parent.childs.set(parent.childs.indexOf(toShort), newFunction);
                toShort = newFunction;
                return true;
            }
            if(secondChild instanceof FunctionDoubleValue && ((FunctionDoubleValue) secondChild).getValue() == 0.0){
                parent.childs.set(parent.childs.indexOf(toShort), firstChild);
                toShort = firstChild;
                return true;
            }
        }else if(toShort instanceof FunctionMinusUnary){
            Function<double[], Double> firstChild = ((FunctionMinusUnary)toShort).getChild();
            if(firstChild instanceof FunctionDoubleValue && ((FunctionDoubleValue) firstChild).getValue() == 0.0){
                parent.childs.set(parent.childs.indexOf(toShort), firstChild);
                toShort = firstChild;
                return true;
            }
        }else if(toShort instanceof FunctionTimes){
            AbstractFunction firstChild = ((FunctionTimes)toShort).getLeft();
            Function<double[], Double> secondChild = ((FunctionTimes)toShort).getRight();
            if(firstChild instanceof FunctionDoubleValue && ((FunctionDoubleValue) firstChild).getValue() == 0.0){
                AbstractFunction newFunction =new FunctionDoubleValue(0.0);
                parent.childs.set(parent.childs.indexOf(toShort), newFunction);
                toShort = newFunction;
                return true;
            }
            if(secondChild instanceof FunctionDoubleValue && ((FunctionDoubleValue) secondChild).getValue() == 0.0){
                AbstractFunction newFunction =new FunctionDoubleValue(0.0);
                parent.childs.set(parent.childs.indexOf(toShort), newFunction);
                return true;
            }
            if(firstChild instanceof FunctionDoubleValue && ((FunctionDoubleValue) firstChild).getValue() == 1.0){
                parent.childs.set(parent.childs.indexOf(toShort), secondChild);
                toShort = secondChild;
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
            Function<double[], Double> firstChild = ((FunctionDivide)toShort).getLeft();
            Function<double[], Double> secondChild = ((FunctionDivide)toShort).getRight();
            if(firstChild instanceof FunctionDoubleValue && ((FunctionDoubleValue) firstChild).getValue() == 0.0){
                AbstractFunction newFunction =new FunctionDoubleValue(0.0);
                parent.childs.set(parent.childs.indexOf(toShort), newFunction);
                return true;
            }
            if(secondChild instanceof FunctionDoubleValue && ((FunctionDoubleValue) secondChild).getValue() == 1.0){
                parent.childs.set(parent.childs.indexOf(toShort), firstChild);
                toShort = firstChild;
                return true;
            }
        }
        return false;
    }
}
