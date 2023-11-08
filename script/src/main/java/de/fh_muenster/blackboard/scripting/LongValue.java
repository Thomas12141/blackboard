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
 * Long as AST label.
 */
public final class LongValue extends Value<Long> {

	/**
	 * Constructor for a long value as an AST label.
	 *
	 * @param v long value
	 */
	LongValue(Long v) {
		this(null, v);
	}

	/**
	 * Constructor for a long value as an AST label.
	 *
	 * @param v long value
	 */
	LongValue(AST<?> p, Long v) {
		super(p, v);
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
