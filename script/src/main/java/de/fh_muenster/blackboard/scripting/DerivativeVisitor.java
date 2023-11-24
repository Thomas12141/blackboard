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
public class DerivativeVisitor implements SecondLayerASTVisitor<Function<double [], Double>> {
	FunctionVisitor functionVisitor = new FunctionVisitor();
	/**
	 * (non-Javadoc)
	 *
	 * @see AstVisitor#visit(LongValue)
	 */
	public Function<double [], Double> visit(LongValue n) {
		return (a) ->{
			return 0.0;
		};
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see AstVisitor#visit(DoubleValue)
	 */
	public Function<double [], Double> visit(DoubleValue n) {
		return (a) ->{
			return 0.0;
		};
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see AstVisitor#visit(OperationNode)
	 */
	public Function<double[], Double> visit(OperationNode n) {
		Function<double[], Double> ls = n.left().accept(this);
		Function<double[], Double> rs = n.right().accept(this);
		Operation op = n.data();

		switch (op) {
			case PLUS:
				return (a) -> {return ls.apply(a) + rs.apply(a);};
			case MINUS:
				return (a) -> {return ls.apply(a) - rs.apply(a);};
			case TIMES:
				return (a) -> {return ls.apply(a)*n.right().accept(functionVisitor).apply(a) + rs.apply(a) *n.left().accept(functionVisitor).apply(a);};
			case DIVIDE:

				return (a) -> {
					if(n.right().accept(this).apply(a) == 0){
						throw new IllegalArgumentException("division by zero");
					}
					return (  ls.apply(a)*n.right().accept(functionVisitor).apply(a)-rs.apply(a) *n.left().accept(functionVisitor).apply(a) ) / Math.pow(n.right().accept(functionVisitor).apply(a), 2);};
			case POWER, POWERCARET:
				return (a) -> {
					if(n.left().accept(functionVisitor).apply(a)<0){
						throw new IllegalArgumentException("complex number");
					}
					return  rs.apply(a)*Math.pow(ls.apply(a), rs.apply(a)-1);
				};
		}
		throw new IllegalArgumentException("unkown operation: " + op);
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see AstVisitor#visit(AssignNode)
	 */
	public Function<double[], Double> visit(AssignNode n) {
		return n.right().accept(this);
	}

	public Function<double[], Double> visit(UnaryOperationNode n) {

		switch (n.data()){
			case MINUS:
				return (a)->{return -n.childs().get(0).accept(this).apply(a);};
		}
		throw new IllegalArgumentException("unknown operation: " + n.data());
	}

	public Function<double[], Double> visit(SemiNode n) {
		return n.right().accept(this);
	}

	public Function<double[], Double> visit(FunctionNode n) {
		if(n.data().equals("lb")){
			return new FunctionDivide(new FunctionDoubleValue(1.0), new FunctionTimes(n.childs().get(0).accept(this), new FunctionLog(new FunctionDoubleValue(2.0))));

		}
		if(n.data().equals("ln")){
			return new FunctionDivide(new FunctionDoubleValue(1.0), n.childs().get(0).accept(this));
		}

		if(n.data().equals("pow")){
			return new FunctionTimes(n.childs().get(1).accept(this), new FunctionPow(n.childs().get(0).accept(this),
					new FunctionMinusBinary(n.childs().get(1).accept(this), new FunctionDoubleValue(1.0))));
		}
		if(n.data().equals("sin")){
			return new FunctionCos(n.childs().get(0).accept(this));
		}
		if(n.data().equals("cos")){
			return new FunctionMinusUnary(new FunctionSin(n.childs().get(0).accept(this)));
		}
		if(n.data().equals("acos")) { // −(1 − x^2)^(−1/2)
			return new FunctionMinusUnary(new FunctionPow(new FunctionMinusBinary(new FunctionDoubleValue(1.0), new FunctionPow(n.childs().get(0).accept(this),
					new FunctionDoubleValue(2.0))), new FunctionMinusUnary(new FunctionDoubleValue(0.5))));
		}
		if(n.data().equals("asin")){ // 1/(1-(x^2))
			return new FunctionDivide(new FunctionDoubleValue(1.0), new FunctionMinusBinary(new FunctionDoubleValue(1.0),
					new FunctionPow(n.childs().get(0).accept(this), new FunctionDoubleValue(2.0))));
		}
		if(n.data().equals("exp")){
			return new FunctionExp(n.childs().get(0).accept(this));
		}
		n.setFunctionCall((FunctionPlus) n.accept(new DerivativeVisitor()));
		if(n.parent() instanceof FunctionAssignNode){
			return ((FunctionAssignNode) n.parent()).right().accept(this);
		}
		AST<?> function = searchFunctionDeclaration(n);
		return function.accept(this);
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

	public Function<double[], Double> visit(FunctionAssignNode functionAssignNode) {
		return null;
	}

	public Function<double[], Double> visit(VariableNode variableNode) {
		return null;
	}

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
	public Function<double[], Double> visit(Label n) {
		return (a)->{
			return a[0];
		};
	}


	private AssignNode needleInHaystack(Label needle) {


		/*	iteration till the assign node of the needle.
		 */
		AST<?> parentAssignNode = needle;

		while (!(parentAssignNode.parent() instanceof SemiNode)){
			parentAssignNode = parentAssignNode.parent();
			if(parentAssignNode==null){
				throw new IllegalArgumentException("function reference is null");
			}
		}

		AST<?> iterator = needle.parent();

		while (iterator != null){
			if (iterator instanceof SemiNode) {
				iterator = ((SemiNode) iterator).left();

				if((iterator) instanceof AssignNode){
					iterator = ((AssignNode) iterator).left();

					if(iterator.data().equals(needle.data())){
						iterator = iterator.parent();

						return  ((AssignNode) iterator);
					}
					iterator = iterator.parent();
				}
				iterator = iterator.parent();
			}
			iterator = iterator.parent();
			if(iterator instanceof SemiNode && ((SemiNode) iterator).left().equals(parentAssignNode)){
				iterator = iterator.parent();
			}
		}
		throw new IllegalArgumentException("Es gibt diesen Label im Baum nicht.");
	}
}
