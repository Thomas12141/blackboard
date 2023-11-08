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

/**
 * An abstract class for an abstract ast visitor.
 */
abstract class AbstractAstVisitor<T> implements AstVisitor<T> {

	/**
	 * POJO constructor.
	 */
	public AbstractAstVisitor() {
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
	 * @see de.fh_muenster.blackboard.KnowledgeSource#canHandle(de.fh_muenster.blackboard.Blackboard,
	 *      java.lang.Object)
	 */
	@Override
	public final boolean canHandle(Blackboard bb, Object task) {
		return task instanceof AST<?>;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see de.fh_muenster.blackboard.KnowledgeSource#solve(de.fh_muenster.blackboard.Blackboard,
	 *      java.lang.Object)
	 */
	@Override
	public final Object solve(Blackboard bb, Object p) {
		if (p instanceof AST<?> ast) {
			return ast.accept(this);
		}
		throw new IllegalArgumentException("wrong problem " + p);
	}
}
