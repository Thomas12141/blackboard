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
import java.util.Stack;

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

		if (null != p) {
			p.childs().add(this);
		}
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
		if(obj==null||this.hashCode()!=obj.hashCode()||getClass() != obj.getClass()){
			return false;
		}

		if(obj==this){
			return true;
		}

		boolean ret = false;
		AstNode<?> that = (AstNode<?>) obj;
		ret = this.data.equals(that.data);

		if(!ret){
			return false;
		}

		ret &= childsEquals((AstNode) this, that);

		if(!ret){
			return false;
		}

		ret &= Objects.equals(parent, that.parent);

		return ret;
	}
	private boolean childsEquals(AstNode a, AstNode b){
		AstNode iterator1 = a;
		AstNode iterator2 = b;
		Stack<AstNode> stack1 = new Stack<AstNode>();
		Stack<AstNode> stack2 = new Stack<AstNode>();
		if(a.childs.size()!=b.childs.size()){
			return false;
		}
		for (Object toPush:
			 iterator1.childs) {
			stack1.push((AstNode) toPush);
		}
		for (Object toPush:
				iterator2.childs) {
			stack1.push((AstNode) toPush);
		}
		while (!stack1.empty() && !stack2.empty()) {
			iterator1 = stack1.pop();
			iterator2 = stack2.pop();
			if(a.childs.size()!=b.childs.size()){
				return false;
			}
			for (Object toPush : iterator1.childs) {
				stack1.push((AstNode) toPush);
			}
			for (Object toPush : iterator2.childs) {
				stack1.push((AstNode) toPush);
			}
			if(!iterator1.data().equals(iterator2.data())) {
				return false;
			}
		}
		if (!stack1.empty()||!stack2.empty()) {
			return false;
		}else {
			return true;
		}
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