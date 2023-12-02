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

	static String variable;
	public Function<double [], Double> visit(AbstractFunction toVisit){
		if(toVisit instanceof FunctionDoubleValue)
			return visit((FunctionDoubleValue) toVisit);

		if(toVisit instanceof AbstractFunctionOneVariable)
			return visit((AbstractFunctionOneVariable) toVisit);

		if(toVisit instanceof AbstractFunctionTwoVariable)
			return visit((AbstractFunctionTwoVariable) toVisit);

		if(toVisit instanceof FunctionLabel)
			return visit((FunctionLabel) toVisit);

		throw new IllegalArgumentException("Unknown node in DerivativeVisitor.");
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

				Function<double[], Double> f = n.childs().get(0).accept(functionVisitor);
				Function<double[], Double> g = n.childs().get(1).accept(functionVisitor);

				Function<double[], Double> fDerivative = visit(n.childs().get(0));
				Function<double[], Double> gDerivative = visit(n.childs().get(1));
				return new FunctionTimes(new FunctionPow(f,g),new FunctionPlus(new FunctionDivide(new FunctionTimes(g, fDerivative), f), new FunctionTimes(gDerivative, new FunctionLog(f))));
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

	public Function<double[], Double> visit(AbstractFunctionOneVariable n) {
		Function<double[], Double> functionVariableDerivative = visit(n.getChild());
		if (n instanceof FunctionMinusUnary) {
			return new FunctionMinusUnary(visit(n.getChild()));
		} else if (n instanceof FunctionPlusUnary) {
			return new FunctionPlusUnary(visit(n.getChild()));
		} else if (n instanceof FunctionCos) {
			return new FunctionTimes(new FunctionMinusUnary(new FunctionSin(n.getChild())), functionVariableDerivative);
		} else if (n instanceof FunctionSin) {

		}

		throw new IllegalArgumentException("unknown operation: " + n.data());
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
			Function<double[], Double> f = n.childs().get(0).childs().get(0).accept(functionVisitor);
			Function<double[], Double> g = n.childs().get(0).childs().get(1).accept(functionVisitor);

			Function<double[], Double> fDerivative = visit(n.childs().get(0).childs().get(0));
			Function<double[], Double> gDerivative = visit(n.childs().get(0).childs().get(1));

			return new FunctionTimes(new FunctionPow(f,g),new FunctionPlus(new FunctionDivide(new FunctionTimes(g, fDerivative), f), new FunctionTimes(gDerivative, new FunctionLog(f))));
		}
		if(n.data().equals("sin")){
			return new FunctionTimes(new FunctionCos(n.childs().get(0).accept(functionVisitor)), functionVariableDerivative);
		}
		if(n.data().equals("cos")){
			return new FunctionTimes(new FunctionMinusUnary(new FunctionSin(n.childs().get(0).accept(functionVisitor))), functionVariableDerivative);
		}
		if(n.data().equals("acos")) { // −(1 − x^2)^(−1/2)
			return new FunctionTimes(new FunctionMinusUnary(new FunctionPow(new FunctionMinusBinary(new FunctionDoubleValue(1.0), new FunctionPow(visit(n.childs().get(0)),
					new FunctionDoubleValue(2.0))), new FunctionMinusUnary(new FunctionDoubleValue(0.5)))), functionVariableDerivative);
		}
		if(n.data().equals("asin")){ // 1/(1-(x^2))
			return new FunctionTimes(new FunctionDivide(new FunctionDoubleValue(1.0), new FunctionPow(new FunctionMinusBinary(new FunctionDoubleValue(1.0),
					new FunctionPow(n.childs().get(0).accept(functionVisitor), new FunctionDoubleValue(2.0))), new FunctionDoubleValue(0.5))),
					visit(n.childs().get(0)));
		}
		if(n.data().equals("exp")){
			return new FunctionTimes(new FunctionExp(n.childs().get(0).accept(functionVisitor)), functionVariableDerivative);
		}
		AST<?> function = FunctionMap.functions.get(n.data());
		if(function.parent() instanceof FunctionAssignNode){
			function = ((FunctionAssignNode) function.parent()).expr();
		}
		n.setFunctionCall(function.accept(functionVisitor));
		return new FunctionTimes(visit(function), functionVariableDerivative);
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see AstVisitor#visit(Label)
	 */
	public Function<double[], Double> visit(FunctionLabel n) {
		if(n.toString().equals(variable)){
			return new FunctionDoubleValue(1.0);
		}
		return new FunctionDoubleValue(0.0);
	}

}
