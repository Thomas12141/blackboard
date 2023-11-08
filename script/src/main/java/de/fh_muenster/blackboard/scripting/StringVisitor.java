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
 * A Visitor iterating over an AST providing a String representation.
 */
public class StringVisitor extends AbstractAstVisitor<String> {

	/**
	 * (non-Javadoc)
	 * 
	 * @see de.fh_muenster.blackboard.scripting.AstVisitor#visit(de.fh_muenster.blackboard.scripting.LongValue)
	 */
	@Override
	public String visit(LongValue n) {
		return n.toString();
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see de.fh_muenster.blackboard.scripting.AstVisitor#visit(de.fh_muenster.blackboard.scripting.DoubleValue)
	 */
	@Override
	public String visit(DoubleValue n) {
		return n.toString();
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see de.fh_muenster.blackboard.scripting.AstVisitor#visit(de.fh_muenster.blackboard.scripting.Label)
	 */
	@Override
	public String visit(Label n) {
		return n.toString();
	}

	@Override
	public String visit(OperationNode n) {
		String left = n.left().accept(this);
		String right = n.right().accept(this);
		return left + n.data() + right;
	}

	@Override
	public String visit(AssignNode n) {
		String left = n.left().accept(this);
		String right = n.right().accept(this);
		return left + n.data() + right;
	}

	@Override
	public String visit(UnaryOperationNode n) {
		String child = n.child().accept(this);
		return n.data() + "(" + child + ")";
	}

	@Override
	public String visit(SemiNode n) {
		String left = n.left().accept(this);
		String right = n.right().accept(this);
		return right + n.data() + left;
	}
}
