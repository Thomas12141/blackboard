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

import de.fh_muenster.blackboard.Blackboard;
import de.fh_muenster.blackboard.KnowledgeSource;

/**
 * A script parser as KnowledgeSource to create an AST.
 */
public abstract class Parser implements KnowledgeSource<String, AST<?>> {

	/**
	 * The constructor for the parser.
	 */
	public Parser() {
		Blackboard blackboard = Blackboard.getInstance();
		blackboard.register(this);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public final int hashCode() {
		return getClass().hashCode();
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public final boolean equals(Object o) {
		if (null == o) {
			return false;
		}
		return getClass().equals(o.getClass());
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public final String toString() {
		return getClass().getSimpleName();
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see @see
	 *      de.fh_muenster.blackboard.KnowledgeSource#canHandle(de.fh_muenster.blackboard.Blackboard,
	 *      java.lang.Object)
	 */
	@Override
	public final boolean canHandle(Blackboard bb, Object task) {
		return task instanceof String;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see @see
	 *      de.fh_muenster.blackboard.KnowledgeSource#solve(de.fh_muenster.blackboard.Blackboard,
	 *      java.lang.Object)
	 */
	@Override
	public final AST<?> solve(Blackboard bb, Object task) {
		if (task instanceof String stm) {
			return parse(stm);
		}
		throw new IllegalArgumentException("wrong task " + task);
	}

	/**
	 * Parse the given statement(s) into an AST.
	 * 
	 * @param stm statement to parse
	 * @return AST
	 */
	public abstract AST<?> parse(final String stm);

	/**
	 * Factory method to create a value AST.
	 * 
	 * @param v value
	 * @return AST
	 */
	protected AST<Long> node(long v) {
		return AST.of(v);
	}

	/**
	 * Factory method to create a value AST.
	 * 
	 * @param v value
	 * @return AST
	 */
	protected AST<Double> node(double v) {
		return AST.of(v);
	}

	/**
	 * Factory method to create a value AST.
	 * 
	 * @param s value
	 * @return AST
	 */
	protected AST<String> node(String s) {
		return AST.of(s);
	}

	/**
	 * Factory method to create an assignment AST.
	 * 
	 * @param left   the left side of the assignment
	 * @param expr the right side of the assignment
	 * @return AST
	 */
	protected AST<?> node(AST<?> left, AST<?> expr) {
		if(left instanceof FunctionNode){
			return new FunctionAssignNode(left, expr);
		}else {
			return new AssignNode((AST<String>) left, expr);
		}
	}
	/*
	*
	 * Factory method to create an function AST.
	 *
	 * @param function the right side of the assignment
	 * @return AST
	 */
	protected AST<?> node(String function, AST<?> variables, int grade) {
		return new DerivativeNode(function, variables, grade);
	}
	/**
	 * Factory method to create an function AST.
	 *
	 * @param function the right side of the assignment
	 * @return AST
	 */
	protected AST<?> node(String function, AST<?> variables) {
		return new FunctionNode(function, variables);
	}

	/**
	 * Factory method to create an arithmetic operation AST.
	 * 
	 * @param s value
	 * @return AST
	 */
	protected AST<?> node(AST<?> ls, String s, AST<?> rs) {
		Operation op = Operation.of(s);

		switch (op) {
			case KOMMA:
				return new VariableNode(ls, rs);
			case PLUS:
				return new PlusNode(ls, rs);
			case MINUS:
				return new MinusNode(ls, rs);
			case TIMES:
				return new TimesNode(ls, rs);
			case DIVIDE:
				return new DivideNode(ls, rs);
			case POWER:
				return new PowerNode(ls, rs);
			case POWERCARET:
				return new PowerCaretNode(ls, rs);
			case SEMI:
				return new SemiNode(ls, rs);
		default:
			throw new IllegalArgumentException("unknown operation: " + s);
		}
	}


	/**
	 * Factory method to create an arithmetic operation AST.
	 *
	 * @param s value
	 * @return AST
	 */
	protected AST<?> node(AST<?> child, String s) {
		UnaryOperation op = UnaryOperation.of(s);

		switch (op) {
			case MINUS:
				return new MinusUnaryNode(child);
			default:
				throw new IllegalArgumentException("unknown operation: " + s);
		}
	}
}
