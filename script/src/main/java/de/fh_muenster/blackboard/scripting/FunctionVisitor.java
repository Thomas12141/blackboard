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
import java.util.function.Function;

/**
 *	A value visitor for ast.
 */
public class FunctionVisitor extends AbstractAstVisitor<Function<double [], Double>> {

	/**
	 * (non-Javadoc)
	 *
	 * @see AstVisitor#visit(LongValue)
	 */
	@Override
	public Function<double [], Double> visit(LongValue n) {
		return (a) ->{
			return Double.valueOf(n.data());
		};
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see AstVisitor#visit(DoubleValue)
	 */
	@Override
	public Function<double [], Double> visit(DoubleValue n) {
		return (a) ->{
			return n.data();
		};
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
		throw new IllegalArgumentException("unknown operation: " + n.data());
	}

	@Override
	public Function<double[], Double> visit(SemiNode n) {
		return n.right().accept(this);
	}

	@Override
	public Function<double[], Double> visit(FunctionNode n) {
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
		if(n.equals(n.parent().childs().get(1))){
			return n.getFunctionCall();
		}else {
			return n.parent().childs().get(1).accept(this);
		}
	}


	private FunctionNode searchFunctionDeclaration(FunctionNode toFind){
		AST<?> parentAssignNode = toFind;
		while (!(parentAssignNode.parent() instanceof SemiNode)){
			parentAssignNode = parentAssignNode.parent();
			if(parentAssignNode==null){
				throw new IllegalArgumentException("function reference is null");
			}
		}
		AST<?> iterator = parentAssignNode.parent();
		while (iterator != null){
			if (iterator instanceof SemiNode) {
				iterator = ((SemiNode) iterator).left();
				iterator = iterator.childs().get(0);
				if(iterator instanceof FunctionNode){
					if(iterator.data().equals(toFind.data())&&iterator.parent() instanceof FunctionAssignNode){
						return  ((FunctionNode) iterator);
					}
					iterator = iterator.parent();
				}
				iterator = iterator.parent();
			}
			iterator = iterator.parent();
			if(iterator instanceof SemiNode){
				iterator = iterator.parent();
			}
		}
		throw new IllegalArgumentException("unknown function");
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
		return (a)->{
			return a[0];
		};
	}
}
