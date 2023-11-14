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
import java.util.Stack;

/**
 * Basic AST implementation.
 */
public class MasterNode<String> implements AST<String> {
	private static MasterNode<java.lang.String> master = null;
	private java.lang.String data;
	public static ArrayList<AST<?>> childs;

	private AST<?> parent;

	private MasterNode() {
		data = "Master";
		childs = new ArrayList<>();
	}

	public static AST<?> getInstance(){
		master = new MasterNode<>();
		childs.clear();
		return master;
	}

	@Override
	public boolean isRoot() {
		return AST.super.isRoot();
	}

	@Override
	public boolean isLeaf() {
		return AST.super.isLeaf();
	}

	@Override
	public <V> V accept(AstVisitor<V> visitor) {
		return visitor.visit(this);
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see AST#data()
	 */
	@Override
	public final String data() {
		return (String) data;
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see AST#parent()
	 */
	@Override
	public final AST<?> parent() {
		return parent;
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see AST#childs()
	 */
	@Override
	public List<AST<?>> childs() {
		return childs;
	}

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
	 * @see Object#hashCode()
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
		MasterNode<?> that = (MasterNode<?>) obj;
		ret = this.data.equals(that.data);

		if(!ret){
			return false;
		}

		ret &= childsEquals((MasterNode) this, that);

		if(!ret){
			return false;
		}
		return ret;
	}
	public static boolean childsEquals(MasterNode a, MasterNode b){
		MasterNode iterator1 = a;
		MasterNode iterator2 = b;
		Stack<MasterNode> stack1 = new Stack<MasterNode>();
		Stack<MasterNode> stack2 = new Stack<MasterNode>();
		if(a.childs.size()!=b.childs.size()){
			return false;
		}
		for (Object toPush:
			 iterator1.childs) {
			stack1.push((MasterNode) toPush);
		}
		for (Object toPush:
				iterator2.childs) {
			stack1.push((MasterNode) toPush);
		}
		while (!stack1.empty() && !stack2.empty()) {
			iterator1 = stack1.pop();
			iterator2 = stack2.pop();
			if(a.childs.size()!=b.childs.size()){
				return false;
			}
			for (Object toPush : iterator1.childs) {
				stack1.push((MasterNode) toPush);
			}
			for (Object toPush : iterator2.childs) {
				stack1.push((MasterNode) toPush);
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
	 * @see Object#toString()
	 */
	@Override
	public java.lang.String toString() {
		StringBuffer result = new StringBuffer();
		for (int i = 0; i < childs.size()-1; i++) {
			result.append(childs.get(i).toString());
			result.append(";");
		}
		if(childs.size()>0){
			result.append(childs.get(childs.size()-1).toString());
		}
		return result.toString();
	}
}