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
 * 
 */
public class ValueVisitor extends AbstractAstVisitor<Double> {

	/**
	 * (non-Javadoc)
	 * 
	 * @see de.fh_muenster.blackboard.scripting.AstVisitor#visit(de.fh_muenster.blackboard.scripting.LongValue)
	 */
	@Override
	public Double visit(LongValue n) {
		return n.data().doubleValue();
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see de.fh_muenster.blackboard.scripting.AstVisitor#visit(de.fh_muenster.blackboard.scripting.DoubleValue)
	 */
	@Override
	public Double visit(DoubleValue n) {
		return n.data();
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see de.fh_muenster.blackboard.scripting.AstVisitor#visit(de.fh_muenster.blackboard.scripting.OperationNode)
	 */
	@Override
	public Double visit(OperationNode n) {
		double ret = 0;
		double ls = n.left().accept(this);
		double rs = n.right().accept(this);
		Operation op = n.data();
		switch (op) {
		case PLUS:
			ret = ls + rs;
			break;
		// TODO rest of the operations missing
		default:
			throw new IllegalArgumentException("unkown operation: " + op);
		}
		return ret;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see de.fh_muenster.blackboard.scripting.AstVisitor#visit(de.fh_muenster.blackboard.scripting.AssignNode)
	 */
	@Override
	public Double visit(AssignNode n) {
		// TODO
		throw new IllegalStateException("not implemented yet");
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see de.fh_muenster.blackboard.scripting.AstVisitor#visit(de.fh_muenster.blackboard.scripting.Label)
	 */
	@Override
	public Double visit(Label n) {
		// TODO
		throw new IllegalStateException("not implemented yet");
	}

}
