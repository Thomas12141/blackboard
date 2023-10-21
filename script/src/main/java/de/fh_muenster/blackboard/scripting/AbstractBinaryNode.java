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

import java.util.Collections;
import java.util.List;

/**
 * BinaryNode with left and right child.
 */
abstract class AbstractBinaryNode<D> extends AstNode<D> {

	/**
	 * @param lhs   of the variable to the left
	 * @param rhs expression of the right side
	 */
	AbstractBinaryNode(AST<?> lhs, D data, AST<?> rhs) {
		super(null, data);
		super.childs().add(lhs);
		super.childs().add(rhs);
		lhs.setParent(this);
		rhs.setParent(this);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see de.fh_muenster.blackboard.scripting.AstNode#childs()
	 */
	@Override
	public final List<AST<?>> childs() {
		return Collections.unmodifiableList(super.childs());
	}

	/**
	 * get the left AST child
	 * 
	 * @return left AST
	 */
	public final AST<?> left() {
		return childs().get(0);
	}

	/**
	 * get the right AST child
	 * 
	 * @return right AST
	 */
	public final AST<?> right() {
		return childs().get(1);
	}
}
