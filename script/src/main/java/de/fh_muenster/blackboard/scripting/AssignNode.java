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

/**
 * Node for an assignment id = expr.
 */
public class AssignNode extends AbstractBinaryNode<String> {

	/**
	 * Constructor for assigne nodes.
	 *
	 * @param id   of the variable to the left
	 * @param expr expression of the right side
	 */
	AssignNode(AST<String> id, AST<?> expr) {
		super(id, "=", expr);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String.format("%s%s%s", id(), data(), expr());
	}

	/**
	 * Get the left identifier AST.
	 * 
	 * @return left child of an assign node
	 */
	@SuppressWarnings("unchecked")
	public final AST<String> id() {
		return (AST<String>) left();
	}

	/**
	 * Get the right AST with the assignment expression.
	 * 
	 * @return expression AST
	 */
	public final AST<?> expr() {
		return right();
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see de.fh_muenster.blackboard.scripting.AST#accept(de.fh_muenster.blackboard.scripting.AstVisitor)
	 */
	@Override
	public <V> V accept(AstVisitor<V> visitor) {
		return visitor.visit(this);
	}

}
