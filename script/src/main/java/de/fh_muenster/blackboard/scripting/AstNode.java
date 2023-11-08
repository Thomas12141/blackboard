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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Basic AST implementation.
 */
abstract class AstNode<T> implements AST<T> {
	private final T data;
	private AST<?> parent;
	private final List<AST<?>> childs;

	AstNode(final T d) {
		this(null, d);
	}

	AstNode(final AST<?> p, final T d) {
		childs = new ArrayList<>();
		parent = p;
		data = Objects.requireNonNull(d, "data is null");
		if (null != p)
			p.childs().add(this);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see de.fh_muenster.blackboard.scripting.AST#setParent(de.fh_muenster.blackboard.scripting.AST)
	 */
	@Override
	public void setParent(AST<?> parent) {
		if (null != parent) {
			this.parent = parent;
			if (!parent.childs().contains(this)) {
				this.parent.childs().add(this);
			}
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see de.fh_muenster.blackboard.scripting.AST#data()
	 */
	@Override
	public final T data() {
		return data;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see de.fh_muenster.blackboard.scripting.AST#parent()
	 */
	@Override
	public final AST<?> parent() {
		return parent;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see de.fh_muenster.blackboard.scripting.AST#childs()
	 */
	@Override
	public List<AST<?>> childs() {
		return childs;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public final int hashCode() {
		return data.hashCode() + 31 * childs.hashCode();
	}

	@Override
	public final boolean equals(final Object obj) {
		if(this.hashCode()!=obj.hashCode()){
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		boolean ret = false;
        AstNode<?> that = (AstNode<?>) obj;
        ret = this.data.equals(that.data);
        if(!ret){
            return false;
        }
        ret &= this.childs.equals(that.childs);
        if(!ret){
            return false;
        }
        ret &= Objects.equals(parent, that.parent);
        return ret;
	}



		/**
         * (non-Javadoc)
         *
         * @see java.lang.Object#toString()
         */
	@Override
	public String toString() {
		return String.format("%s", data);
	}
}
