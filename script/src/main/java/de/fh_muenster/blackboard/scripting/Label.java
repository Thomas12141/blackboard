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
 * String as AST label.
 */
public class Label extends AstNode<String> {

	/**
	 * Constructor for a label node.
	 *
	 * @param s String for the label
	 */
	Label(String s) {
		this(null, s);
	}

	/**
	 * Constructor for a label node.
	 *
	 * @param s String for the label
	 */
	Label(AST<?> p, String s) {
		super(p, s);
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
