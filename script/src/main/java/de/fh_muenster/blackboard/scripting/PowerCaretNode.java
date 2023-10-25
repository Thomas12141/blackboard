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
 * POWERCaret operation node
 */
public class PowerCaretNode extends OperationNode {
	PowerCaretNode(AST<?> left, AST<?> right) {
		super(left, Operation.POWERCARET, right);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see AST#accept(AstVisitor)
	 */
	@Override
	public <V> V accept(AstVisitor<V> visitor) {
		return visitor.visit(this);
	}
}