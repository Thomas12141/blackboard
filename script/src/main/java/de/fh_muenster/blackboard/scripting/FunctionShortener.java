package de.fh_muenster.blackboard.scripting;

import java.util.Stack;
import java.util.function.Function;

public class FunctionShortener {
    public static void toShort(Function<double[], Double> toShort){
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
            toShort(toShort);
        }
    }

    private static boolean helper(Function<double[], Double> toShort){

        if(toShort instanceof FunctionPlus){
            Function<double[], Double> firstChild = ((FunctionPlus)toShort).getLeft();
            Function<double[], Double> secondChild = ((FunctionPlus)toShort).getRight();
            if(firstChild instanceof FunctionDoubleValue && ((FunctionDoubleValue) firstChild).getValue() == 0.0){
                toShort = secondChild;
                return true;
            }
            if(secondChild instanceof FunctionDoubleValue && ((FunctionDoubleValue) secondChild).getValue() == 0.0){
                toShort = firstChild;
                return true;
            }
        }else if(toShort instanceof FunctionMinusBinary){
            Function<double[], Double> firstChild = ((FunctionMinusBinary)toShort).getLeft();
            Function<double[], Double> secondChild = ((FunctionMinusBinary)toShort).getRight();
            if(firstChild instanceof FunctionDoubleValue && ((FunctionDoubleValue) firstChild).getValue() == 0.0){
                toShort = new FunctionMinusUnary(secondChild);
                return true;
            }
            if(secondChild instanceof FunctionDoubleValue && ((FunctionDoubleValue) secondChild).getValue() == 0.0){
                toShort = firstChild;
                return true;
            }
        }else if(toShort instanceof FunctionMinusUnary){
            Function<double[], Double> firstChild = ((FunctionMinusUnary)toShort).getChild();
            if(firstChild instanceof FunctionDoubleValue && ((FunctionDoubleValue) firstChild).getValue() == 0.0){
                toShort = firstChild;
                return true;
            }
        }else if(toShort instanceof FunctionTimes){
            Function<double[], Double> firstChild = ((FunctionTimes)toShort).getLeft();
            Function<double[], Double> secondChild = ((FunctionTimes)toShort).getRight();
            if(firstChild instanceof FunctionDoubleValue && ((FunctionDoubleValue) firstChild).getValue() == 0.0){
                toShort = new FunctionDoubleValue(0.0);
                return true;
            }
            if(secondChild instanceof FunctionDoubleValue && ((FunctionDoubleValue) secondChild).getValue() == 0.0){
                toShort = new FunctionDoubleValue(0.0);
                return true;
            }
            if(firstChild instanceof FunctionDoubleValue && ((FunctionDoubleValue) firstChild).getValue() == 1.0){
                toShort = secondChild;
                return true;
            }
            if(secondChild instanceof FunctionDoubleValue && ((FunctionDoubleValue) secondChild).getValue() == 1.0){
                toShort = firstChild;
                return true;
            }
        }else if(toShort instanceof FunctionDivide){
            Function<double[], Double> firstChild = ((FunctionDivide)toShort).getLeft();
            Function<double[], Double> secondChild = ((FunctionDivide)toShort).getRight();
            if(firstChild instanceof FunctionDoubleValue && ((FunctionDoubleValue) firstChild).getValue() == 0.0){
                toShort = new FunctionDoubleValue(0.0);
                return true;
            }
            if(secondChild instanceof FunctionDoubleValue && ((FunctionDoubleValue) secondChild).getValue() == 1.0){
                toShort = firstChild;
                return true;
            }
        }
        return false;
    }
}
