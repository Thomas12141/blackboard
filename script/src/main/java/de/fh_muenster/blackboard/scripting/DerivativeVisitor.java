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
	public Function<double [], Double> visit(FunctionDoubleValue n) {
		return new FunctionDoubleValue(0.0);
	}

	public Function<double[], Double> visit(AbstractFunctionTwoVariable n) {
		Function<double[], Double> lsDerivative = visit(n.getLeft());
		Function<double[], Double> rsDerivative = visit(n.getRight());
		Function<double[], Double> ls = n.getLeft();
		Function<double[], Double> rs = n.getRight();
		String op = n.toString();

		if (n instanceof FunctionTimes) {
			return new FunctionPlus(new FunctionTimes(lsDerivative, rs), new FunctionTimes(ls, rsDerivative));
		} else if (n instanceof FunctionPlus) {
			return new FunctionPlus(lsDerivative, rsDerivative);
		} else if (n instanceof FunctionMinusBinary) {
			return new FunctionMinusBinary(lsDerivative, rsDerivative);
		} else if (n instanceof FunctionDivide) {
			return new FunctionDivide(new FunctionMinusBinary(new FunctionTimes(lsDerivative, rs), new FunctionTimes(ls, rsDerivative)), new FunctionPow(rs, new FunctionDoubleValue(2.0)));
		} else if (n instanceof FunctionPow) {
			Function<double[], Double> f = n.getLeft();
			Function<double[], Double> g = n.getRight();

			Function<double[], Double> fDerivative = visit(n.getLeft());
			Function<double[], Double> gDerivative = visit(n.getRight());
			return new FunctionTimes(new FunctionPow(f,g),new FunctionPlus(new FunctionDivide(new FunctionTimes(g, fDerivative), f), new FunctionTimes(gDerivative, new FunctionLog(f))));
		} else {
			throw new IllegalArgumentException("unkown operation: " + op);
		}
	}

	public Function<double[], Double> visit(AbstractFunctionOneVariable n) {
		Function<double[], Double> functionVariableDerivative = visit(n.getChild());

		if (n instanceof FunctionMinusUnary) {
			return new FunctionMinusUnary(functionVariableDerivative);

		} else if (n instanceof FunctionPlusUnary) {
			return new FunctionPlusUnary(functionVariableDerivative);

		} else if (n instanceof FunctionCos) {
			return new FunctionTimes(new FunctionMinusUnary(new FunctionSin(n.getChild())), functionVariableDerivative);

		} else if (n instanceof FunctionSin) {
			return new FunctionTimes(new FunctionCos(n.getChild()), functionVariableDerivative);

		} else if (n instanceof FunctionAcos) {
			return new FunctionTimes(new FunctionMinusUnary(new FunctionPow(new FunctionMinusBinary(new FunctionDoubleValue(1.0), new FunctionPow(visit(n.getChild()),
					new FunctionDoubleValue(2.0))), new FunctionMinusUnary(new FunctionDoubleValue(0.5)))), functionVariableDerivative);

		} else if (n instanceof FunctionAsin) {
			return new FunctionTimes(new FunctionDivide(new FunctionDoubleValue(1.0), new FunctionPow(new FunctionMinusBinary(new FunctionDoubleValue(1.0),
					new FunctionPow(n.getChild(), new FunctionDoubleValue(2.0))), new FunctionDoubleValue(0.5))), functionVariableDerivative);

		} else if (n instanceof FunctionLog) {
			return new FunctionTimes(new FunctionDivide(new FunctionDoubleValue(1.0), n.getChild()), functionVariableDerivative);

		} else if (n instanceof FunctionLogB) {
			return new FunctionTimes(new FunctionDivide(new FunctionDoubleValue(1.0), new FunctionTimes(visit(n.getChild()),
					new FunctionLog(new FunctionDoubleValue(2.0)))), functionVariableDerivative);

		} else if (n instanceof FunctionExp) {
			return new FunctionTimes(new FunctionExp(n.getChild()), functionVariableDerivative);
		} else {
			throw new IllegalArgumentException("unknown operation: " + n.toString());
		}
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
