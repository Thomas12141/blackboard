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

import java.util.List;

/**
 * Abstract syntax tree interface.
 */
public interface AST<T> {
	<V> V accept(AstVisitor<V> visitor);

	<V> V accept(SecondLayerASTVisitor<V> visitor);
	/**
	 * The data value of the node instance.
	 * 
	 * @return data
	 */
	T data();

	/**
	 * The parent of this node.
	 * 
	 * @return parent or null if root
	 */
	AST<?> parent();

	/**
	 * The childs of this node.
	 * 
	 * @return childs as list
	 */
	List<AST<?>> childs();

	/**
	 * Set the parent of this node, which will become one of his child nodes.
	 * 
	 * @param parent
	 */
	void setParent(AST<?> parent);

	/**
	 * Indicate if this node is root.
	 * 
	 * @return root flag
	 */
	default boolean isRoot() {
		return null == parent();
	}

	/**
	 * Indicate if this is a leaf node.
	 * 
	 * @return leaf flag
	 */
	default boolean isLeaf() {
		return childs().isEmpty();
	}

	/**
	 * Factory method to create an AST.
	 * 
	 * @param v parameter to wrap as AST
	 * @return AST
	 */
	static AST<String> of(final String v) {
		return new Label(v);
	}

	/**
	 * Factory method to create an AST.
	 * 
	 * @param v parameter to wrap as AST
	 * @return AST
	 */
	static AST<Double> of(final double v) {
		return new DoubleValue(v);
	}

	/**
	 * Factory method to create an AST.
	 * 
	 * @param v parameter to wrap as AST
	 * @return AST
	 */
	static AST<Long> of(final long v) {
		return new LongValue(v);
	}

}
