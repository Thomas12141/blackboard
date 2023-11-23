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

import de.fh_muenster.blackboard.KnowledgeSource;

/**
 * Interface for the Visitor pattern.
 */
public interface SecondLayerASTVisitor<V>{
	/**
	 * visit a long leaf node.
	 * 
	 * @param n node to visit
	 * @return V value
	 */
	V visit(LongValue n);

	/**
	 * visit a double leaf node.
	 * 
	 * @param n node to visit
	 * @return V value
	 */
	V visit(DoubleValue n);

	/**
	 * visit a string id leaf node.
	 * 
	 * @param n node to visit
	 * @return V value
	 */
	V visit(Label n);

	/**
	 * visit a arithmetic operation node.
	 * 
	 * @param n node to visit
	 * @return V value
	 */
	V visit(OperationNode n);

	/**
	 * visit an assignment node.
	 * 
	 * @param n node to visit
	 * @return V value
	 */
	V visit(AssignNode n);

	/**
	 * visit a unary arithmetic operation node.
	 *
	 * @param n node to visit
	 * @return V value
	 */
	V visit(UnaryOperationNode n);

	/**
	 * visit a semi node.
	 *
	 * @param n node to visit
	 * @return V value
	 */
    V visit(SemiNode n);

	/**
	 * visit a semi node.
	 *
	 * @param n node to visit
	 * @return V value
	 */
	V visit(FunctionNode n);

    V visit(FunctionAssignNode functionAssignNode);

	V visit(VariableNode variableNode);

	V visit(MasterNode masterNode);
}
