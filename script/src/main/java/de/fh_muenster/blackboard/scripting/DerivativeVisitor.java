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

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.function.Function;

/**
 *	A value visitor for ast.
 */
public class DerivativeVisitor{
	FunctionVisitor functionVisitor = new FunctionVisitor();


	private Function<double [], Double> visit(AST<?> toVisit){
		if(toVisit instanceof LongValue)
			return visit((LongValue) toVisit);
		if(toVisit instanceof DoubleValue)
			return visit((DoubleValue) toVisit);
		if(toVisit instanceof OperationNode)
			return visit((OperationNode) toVisit);
		if(toVisit instanceof AssignNode)
			return visit((AssignNode) toVisit);
		if(toVisit instanceof UnaryOperationNode)
			return visit((UnaryOperationNode) toVisit);
		if(toVisit instanceof SemiNode)
			return visit((SemiNode) toVisit);
		if(toVisit instanceof FunctionNode)
			return visit((FunctionNode) toVisit);
		if(toVisit instanceof FunctionAssignNode)
			return visit((FunctionAssignNode) toVisit);
		if(toVisit instanceof VariableNode)
			return visit((VariableNode) toVisit);
		if(toVisit instanceof Label)
			return visit((Label) toVisit);
		throw new IllegalArgumentException("Unknown node in DerivativeVisitor.");
	}
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
		Function<double[], Double> lsDerivative = visit(n.left());
		Function<double[], Double> rsDerivative = visit(n.right());
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
		return visit(n.right());
	}

	public Function<double[], Double> visit(UnaryOperationNode n) {

		switch (n.data()){
			case MINUS:
				return new FunctionMinusUnary(visit(n.childs().get(0)));
			case PLUS:

				return new FunctionPlusUnary(visit(n.childs().get(0)));
		}
		throw new IllegalArgumentException("unknown operation: " + n.data());
	}

	public Function<double[], Double> visit(SemiNode n) {
		return null;
	}


	public Function<double[], Double> visit(FunctionNode n) {
		Function<double[], Double> functionVariableDerivative = visit(n.childs().get(0));
		if(n.data().equals("lb")){
			return new FunctionTimes(new FunctionDivide(new FunctionDoubleValue(1.0), new FunctionTimes(visit(n.childs().get(0)), new FunctionLog(new FunctionDoubleValue(2.0)))), functionVariableDerivative);

		}
		if(n.data().equals("ln")){
			return new FunctionTimes(new FunctionDivide(new FunctionDoubleValue(1.0), n.childs().get(0).accept(functionVisitor)), functionVariableDerivative);
		}

		if(n.data().equals("pow")){
			return new FunctionTimes(new FunctionTimes(visit(n.childs().get(1)), new FunctionPow(visit(n.childs().get(0)),
					new FunctionMinusBinary(visit(n.childs().get(1)), new FunctionDoubleValue(1.0)))), functionVariableDerivative);
		}
		if(n.data().equals("sin")){
			return new FunctionTimes(new FunctionCos(visit(n.childs().get(0))), functionVariableDerivative);
		}
		if(n.data().equals("cos")){
			return new FunctionTimes(new FunctionMinusUnary(new FunctionSin(visit(n.childs().get(0)))), functionVariableDerivative);
		}
		if(n.data().equals("acos")) { // −(1 − x^2)^(−1/2)
			return new FunctionTimes(new FunctionMinusUnary(new FunctionPow(new FunctionMinusBinary(new FunctionDoubleValue(1.0), new FunctionPow(visit(n.childs().get(0)),
					new FunctionDoubleValue(2.0))), new FunctionMinusUnary(new FunctionDoubleValue(0.5)))), functionVariableDerivative);
		}
		if(n.data().equals("asin")){ // 1/(1-(x^2))
			return new FunctionTimes(new FunctionDivide(new FunctionDoubleValue(1.0), new FunctionMinusBinary(new FunctionDoubleValue(1.0),
					new FunctionPow(visit(n.childs().get(0)), new FunctionDoubleValue(2.0)))), functionVariableDerivative);
		}
		if(n.data().equals("exp")){
			return new FunctionTimes(new FunctionExp(visit(n.childs().get(0))), functionVariableDerivative);
		}
		n.setFunctionCall(visit(n));
		AST<?> function = FunctionMap.functions.get(n.data());
		return new FunctionTimes(visit(n), functionVariableDerivative);
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
			visit(trees.get(i));
		}
		return visit(trees.get(trees.size()-1));
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
