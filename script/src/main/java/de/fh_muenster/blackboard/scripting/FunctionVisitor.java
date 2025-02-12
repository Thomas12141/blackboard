/*
 * Project: script
 *
 * Copyright (c) 2023, Prof. Dr. Nikolaus Wulff University of Applied Sciences,
 * Muenster, Germany Lab for computer sciences (Lab4Inf).
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 */
package de.fh_muenster.blackboard.scripting;

import java.util.ArrayList;
import java.util.Stack;
import java.util.function.Function;

/**
 *	A value visitor for ast.
 */
public class FunctionVisitor extends AbstractAstVisitor<Function<double [], Double>> {



	public FunctionVisitor(){
		super();
		FunctionNode functionNode = new FunctionNode("ln", new Label("x"));
		FunctionMap.functions.put("ln", functionNode);
		functionNode = new FunctionNode("sin", new Label("x"));
		FunctionMap.functions.put("sin", functionNode);
		functionNode = new FunctionNode("lb", new Label("x"));
		FunctionMap.functions.put("lb", functionNode);
		functionNode = new FunctionNode("pow", new VariableNode(new Label("x"), new Label("y")));
		FunctionMap.functions.put("pow", functionNode);
		functionNode = new FunctionNode("cos", new Label("x"));
		FunctionMap.functions.put("cos", functionNode);
		functionNode = new FunctionNode("acos", new Label("x"));
		FunctionMap.functions.put("acos", functionNode);
		functionNode = new FunctionNode("asin", new Label("x"));
		FunctionMap.functions.put("asin", functionNode);
		functionNode = new FunctionNode("exp", new Label("x"));
		FunctionMap.functions.put("exp", functionNode);
	}
	/**
	 * (non-Javadoc)
	 *
	 * @see AstVisitor#visit(LongValue)
	 */
	@Override
	public Function<double [], Double> visit(LongValue n) {
		return new FunctionDoubleValue(n.data().doubleValue());
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see AstVisitor#visit(DoubleValue)
	 */
	@Override
	public Function<double [], Double> visit(DoubleValue n) {
		return new FunctionDoubleValue(n.data());
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see AstVisitor#visit(OperationNode)
	 */
	@Override
	public Function<double[], Double> visit(OperationNode n) {
		double ret = 0;
		Function<double[], Double> ls = n.left().accept(this);
		Function<double[], Double> rs = n.right().accept(this);
		Operation op = n.data();

		switch (op) {
			case PLUS:
				return new FunctionPlus(ls, rs);
			case MINUS:
				return new FunctionMinusBinary(ls, rs);
			case TIMES:
				return new FunctionTimes(ls, rs);
			case DIVIDE:
				return new FunctionDivide(ls, rs);
			case POWER, POWERCARET:
				return new FunctionPow(ls, rs);
		}
		throw new IllegalArgumentException("unkown operation: " + op);
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see AstVisitor#visit(AssignNode)
	 */
	@Override
	public Function<double[], Double> visit(AssignNode n) {
		VariablesMap.variables.put(n.id().data(), n.expr().accept(new ValueVisitor()));
		return n.right().accept(this);
	}

	@Override
	public Function<double[], Double> visit(UnaryOperationNode n) {

		switch (n.data()){
			case MINUS:
				return new FunctionMinusUnary(n.childs().get(0).accept(this));
		}
		switch (n.data()){
			case PLUS:
				return new FunctionPlusUnary(n.childs().get(0).accept(this));
		}
		throw new IllegalArgumentException("unknown operation: " + n.data());
	}

	@Override
	public Function<double[], Double> visit(SemiNode n) {
		return n.right().accept(this);
	}

	@Override
	public Function<double[], Double> visit(FunctionNode n) {
		int grade = iteratorDerivative(n);
		if(n.data().equals("lb")){
			return new FunctionLogB(n.childs().get(0).accept(this));
		}
		if(n.data().equals("ln")){
			return new FunctionLog(n.childs().get(0).accept(this));
		}
		if(n.data().equals("pow")){
			return new FunctionPow(n.childs().get(0).childs().get(0).accept(this), n.childs().get(0).childs().get(1).accept(this));
		}
		if(n.data().equals("sin")){
			return new FunctionSin(n.childs().get(0).accept(this));
		}
		if(n.data().equals("cos")){
			return new FunctionCos(n.childs().get(0).accept(this));
		}
		if(n.data().equals("acos")){
			return new FunctionAcos(n.childs().get(0).accept(this));
		}
		if(n.data().equals("asin")){
			return new FunctionAsin(n.childs().get(0).accept(this));
		}
		if(n.data().equals("exp")){
			return new FunctionExp(n.childs().get(0).accept(this));
		}
		if(grade>0){
			if(FunctionMap.functions.containsKey(n.data())){
				DerivativeVisitor.variable = FunctionMap.functions.get(n.data()).childs().get(0).toString();
				return FunctionMap.functions.get(n.data()).getFunctionCall();
			}else if(n.childs().get(0) instanceof Label|| isNotElementaryFunction(n)){
				Function<double[], Double> iterator = FunctionMap.functions.get(n.data().substring(0, n.data().indexOf('\''))).getFunctionCall();
				DerivativeVisitor.variable = FunctionMap.functions.get(n.data().substring(0, n.data().indexOf('\''))).childs().get(0).toString();
				DerivativeVisitor derivativeVisitor = new DerivativeVisitor();
				String functionName = n.data();
				for (int i = 0; i < grade; i++) {
					iterator = derivativeVisitor.visit((AbstractFunction)iterator);
					iterator = FunctionShortener.toShort(iterator);
					functionName += "'";
					ArrayList<String> variables = new ArrayList<String>();
					variables.add(DerivativeVisitor.variable);
					FunctionNode functionNode = new FunctionNode(functionName, new Label(DerivativeVisitor.variable));
					functionNode.setFunctionCall(iterator);
					functionNode.setVariableList(variables);
					FunctionMap.functions.put(functionNode.data(), functionNode);
				}
				return iterator;
			}else{
				return new FunctionDoubleValue(0.0);
			}
		}else if(n.childs().size()==1&& n.childs().get(0) instanceof FunctionNode){
			Function<double[], Double> function = FunctionMap.functions.get(n.data()).getFunctionCall();
			String label = (String) n.childs().get(0).childs().get(0).data();
			AbstractFunction abstractFunction = replace((AbstractFunction) FunctionMap.functions.get(n.childs().get(0).data()).getFunctionCall(), (AbstractFunction) function, label);
			return abstractFunction;
		}else if(n.equals(n.parent().childs().get(1))){
			if(n.getFunctionCall()==null){
				FunctionNode functionNode = FunctionMap.functions.get(n.data());
				AbstractFunction toSolve = ((AbstractFunction)FunctionMap.functions.get(n.data()).getFunctionCall()).clone();
				int position = 0;
				for (AST<?> toReplace: n.childs()) {
					AbstractFunction newFunctionSubtree = (AbstractFunction) toReplace.accept(this);
					if(newFunctionSubtree!=null){
						variableReplace(n.childs().get(0).data().toString(), functionNode.getVariables().get(0), toSolve);
						toSolve = replace(newFunctionSubtree, toSolve, functionNode.getVariables().get(position));
						position++;
					}
				}
				return toSolve;
			}
			return n.getFunctionCall();
		}else if(FunctionMap.functions.containsKey(n.data())){
			FunctionNode functionNode = FunctionMap.functions.get(n.data());
			AbstractFunction toSolve = ((AbstractFunction)functionNode.getFunctionCall()).clone();
			variableReplace(n.childs().get(0).data().toString(), functionNode.childs().get(0).toString(), toSolve);
			return FunctionMap.functions.get(n.data()).getFunctionCall();
		}else {
			return n.parent().childs().get(1).accept(this);
		}
	}

	private boolean isNotElementaryFunction(FunctionNode n) {
		String function = n.data().replaceAll("'", "");
		if(function.equals("sin")||function.equals("cos")||function.equals("ln")||function.equals("lb")||function.equals("acos")
				||function.equals("asin")||function.equals("pow")||function.equals("exp"))
			return false;
		return true;
	}


	@Override
	public Function<double[], Double> visit(FunctionAssignNode functionAssignNode) {
		FunctionMap.functions.put(functionAssignNode.id().data(), (FunctionNode) functionAssignNode.id());
		return functionAssignNode.expr().accept(this);
	}

	@Override
	public Function<double[], Double> visit(VariableNode variableNode) {
		return null;
	}

	@Override
	public Function<double[], Double> visit(MasterNode masterNode) {
		ArrayList<AST<?>> trees = (ArrayList) masterNode.childs();
		for (int i = 0; i < trees.size()-1; i++) {
			trees.get(i).accept(this);
		}
		return trees.get(trees.size()-1).accept(this);
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see AstVisitor#visit(Label)
	 */
	@Override
	public Function<double[], Double> visit(Label n) {
		AST<?> iterator =n.parent();
		while (iterator != null&&!(iterator instanceof FunctionNode)&&!(iterator instanceof FunctionAssignNode)){
			iterator = iterator.parent();
		}
		if (iterator ==null|| (!(iterator instanceof FunctionAssignNode) && ((FunctionNode) iterator).getVariables()==null)){
			return new FunctionLabel(0, n.data());
		}

		if (iterator instanceof FunctionAssignNode){
			iterator = ((FunctionAssignNode) iterator).left();
		}
		return new FunctionLabel(((FunctionNode)iterator).getVariables().indexOf(n.data()), n.data());
	}

	public static int iteratorDerivative(FunctionNode n) {
		StringVisitor stringVisitor = new StringVisitor();
		String i = n.accept(stringVisitor);
		int end = i.indexOf("(");
		int counter = 0;

		for (int j = 0; j < end; j++) {
			if ("'".equals(i.substring(j, j + 1))) {
				counter++;
			}
		}

		return counter;
	}


	private AbstractFunction replace(AbstractFunction toReplace, AbstractFunction inWhichTree, String whichLabel){
		AbstractFunction iterator = new FunctionPlusUnary(inWhichTree);
		Stack<Function<double[], Double>> stack = new Stack<>();
		stack.push(iterator);
		do{
			for (Function<double[], Double> toPush: iterator.childs) {
				stack.push(toPush);
			}
			if(iterator.toString().equals(whichLabel)){
				AbstractFunction newFunction = toReplace.clone();
				if(iterator.parent.childs.size()==2){
					if(iterator.parent.childs.get(0) == iterator){
						((AbstractFunctionTwoVariable)iterator.parent).setLeft(newFunction);
						((AbstractFunctionTwoVariable) iterator.parent).getLeft().setParent(newFunction);
						iterator.parent.childs.set(0, newFunction);
					}else{
						((AbstractFunctionTwoVariable)iterator.parent).setRight(newFunction);
						((AbstractFunctionTwoVariable)iterator.parent).getRight().setParent(iterator.parent);
						iterator.parent.childs.set(1, newFunction);
					}
				}else {
					((AbstractFunctionOneVariable)iterator.parent).setChild(newFunction);
					((AbstractFunctionOneVariable)iterator.parent).child.setParent(iterator.parent);
					iterator.parent.childs.set(0, newFunction);
				}
			}
			iterator = (AbstractFunction) stack.pop();
		} while (!stack.empty());
		while (iterator.parent != null){
			iterator = iterator.parent;
		}

		((AbstractFunction)iterator.childs.get(0)).parent = null;
		return (AbstractFunction) iterator.childs.get(0);
	}

	private static void variableReplace(String newVariable, String oldVariable, AbstractFunction toSolve){
		if(newVariable.equals("*")){
			return;
		}
		if(toSolve instanceof AbstractFunctionTwoVariable){
			variableReplace(newVariable, oldVariable, ((AbstractFunctionTwoVariable) toSolve).left);
			variableReplace(newVariable, oldVariable, ((AbstractFunctionTwoVariable) toSolve).right);
		}else if(toSolve instanceof AbstractFunctionOneVariable){
			variableReplace(newVariable, oldVariable, ((AbstractFunctionOneVariable) toSolve).child);
		}else if(toSolve instanceof FunctionLabel&&((FunctionLabel) toSolve).getLabel().equals(oldVariable)){
			((FunctionLabel) toSolve).setLabel(newVariable);
		}
	}
}
