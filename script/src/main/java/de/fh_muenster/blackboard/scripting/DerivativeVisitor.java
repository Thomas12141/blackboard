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
public class DerivativeVisitor extends AbstractAstVisitor<Function<double[], Double>> {
	FunctionVisitor functionVisitor = new FunctionVisitor();
	/**
	 * (non-Javadoc)
	 *
	 * @see AstVisitor#visit(LongValue)
	 */
	public Function<double [], Double> visit(LongValue n) {
		return new FunctionDoubleValue(0.0);
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see AstVisitor#visit(DoubleValue)
	 */
	public Function<double [], Double> visit(DoubleValue n) {
		return new FunctionDoubleValue(0.0);
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see AstVisitor#visit(OperationNode)
	 */
	public Function<double[], Double> visit(OperationNode n) {
		Function<double[], Double> lsDerivative = n.left().accept(this);
		Function<double[], Double> rsDerivative = n.right().accept(this);
		Function<double[], Double> ls = n.left().accept(functionVisitor);
		Function<double[], Double> rs = n.right().accept(functionVisitor);
		Operation op = n.data();

		switch (op) {
			case PLUS:
				return new FunctionPlus(lsDerivative, rsDerivative);
			case MINUS:
				return new FunctionMinusBinary(lsDerivative, rsDerivative);
			case TIMES:
				return new FunctionPlus(new FunctionTimes(lsDerivative, rs), new FunctionTimes(ls, rsDerivative));
			case DIVIDE:
				return new FunctionDivide(new FunctionMinusBinary(new FunctionTimes(lsDerivative, rs), new FunctionTimes(ls, rsDerivative)), new FunctionPow(rs, new FunctionDoubleValue(2.0)));
			case POWER, POWERCARET:
				return new FunctionTimes(new FunctionPow(ls, rs), new FunctionPlus(new FunctionTimes(rs, new FunctionDivide(lsDerivative, ls)), new FunctionTimes(rsDerivative, new FunctionLog(ls))));
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
				return new FunctionMinusUnary(n.childs().get(0).accept(this));
			case PLUS:

				return new FunctionPlusUnary(n.childs().get(0).accept(this));
		}
		throw new IllegalArgumentException("unknown operation: " + n.data());
	}

	@Override
	public Function<double[], Double> visit(SemiNode n) {
		return null;
	}


	public Function<double[], Double> visit(FunctionNode n) {
		Function<double[], Double> functionVariableDerivative = n.childs().get(0).accept(this);
		if(n.data().equals("lb")){
			return new FunctionTimes(new FunctionDivide(new FunctionDoubleValue(1.0), new FunctionTimes(n.childs().get(0).accept(this), new FunctionLog(new FunctionDoubleValue(2.0)))), functionVariableDerivative);

		}
		if(n.data().equals("ln")){
			return new FunctionTimes(new FunctionDivide(new FunctionDoubleValue(1.0), n.childs().get(0).accept(this)), functionVariableDerivative);
		}

		if(n.data().equals("pow")){
			return new FunctionTimes(new FunctionTimes(n.childs().get(1).accept(this), new FunctionPow(n.childs().get(0).accept(this),
					new FunctionMinusBinary(n.childs().get(1).accept(this), new FunctionDoubleValue(1.0)))), functionVariableDerivative);
		}
		if(n.data().equals("sin")){
			return new FunctionTimes(new FunctionCos(n.childs().get(0).accept(this)), functionVariableDerivative);
		}
		if(n.data().equals("cos")){
			return new FunctionTimes(new FunctionMinusUnary(new FunctionSin(n.childs().get(0).accept(this))), functionVariableDerivative);
		}
		if(n.data().equals("acos")) { // −(1 − x^2)^(−1/2)
			return new FunctionTimes(new FunctionMinusUnary(new FunctionPow(new FunctionMinusBinary(new FunctionDoubleValue(1.0), new FunctionPow(n.childs().get(0).accept(this),
					new FunctionDoubleValue(2.0))), new FunctionMinusUnary(new FunctionDoubleValue(0.5)))), functionVariableDerivative);
		}
		if(n.data().equals("asin")){ // 1/(1-(x^2))
			return new FunctionTimes(new FunctionDivide(new FunctionDoubleValue(1.0), new FunctionMinusBinary(new FunctionDoubleValue(1.0),
					new FunctionPow(n.childs().get(0).accept(this), new FunctionDoubleValue(2.0)))), functionVariableDerivative);
		}
		if(n.data().equals("exp")){
			return new FunctionTimes(new FunctionExp(n.childs().get(0).accept(this)), functionVariableDerivative);
		}
		n.setFunctionCall(n.accept(this));
		AST<?> function = FunctionMap.functions.get(n.data());
		return new FunctionTimes(function.accept(this), functionVariableDerivative);
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
		return new FunctionDoubleValue(1.0);
	}

}
