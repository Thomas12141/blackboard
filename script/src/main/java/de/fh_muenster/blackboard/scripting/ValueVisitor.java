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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

/**
 *	A value visitor for ast.
 */
public class ValueVisitor extends AbstractAstVisitor<Double> {

	/**
	 * (non-Javadoc)
	 *
	 * @see de.fh_muenster.blackboard.scripting.AstVisitor#visit(de.fh_muenster.blackboard.scripting.LongValue)
	 */
	@Override
	public Double visit(LongValue n) {
		return (n.data().doubleValue());
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
			case MINUS:
				ret = ls - rs;
				break;
			case TIMES:
				ret = ls * rs;
				break;
			case DIVIDE:
				if(rs == 0){
					throw new IllegalArgumentException("division by zero");
				}
				ret = ls / rs;
				break;
			case POWER, POWERCARET:
				ret = Math.pow(ls, rs);
				if(Double.isNaN(ret)){
					throw new IllegalArgumentException("complex number");
				}
				break;
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
		double rs = n.right().accept(this);

		return rs;
	}

	@Override
	public Double visit(UnaryOperationNode n) {
		double ret = 0;
		double childValue = n.child().accept(this);

		switch (n.data()){
			case SIN:
				ret = Math.sin(childValue);
				break;
			case ASIN:
				ret = Math.asin(childValue);
				break;
			case COS:
				ret = Math.cos(childValue);
				break;
			case EXP:
				ret = Math.exp(childValue);
				break;
			case MINUS:
				ret = -childValue;
				break;
			case LN:
				ret = Math.log(childValue);
				break;
			case ACOS:
				ret = Math.acos(childValue);
				break;
			case LB:
				ret = Math.log(childValue)/Math.log(2);
				break;
			default:
				throw new IllegalArgumentException("unknown operation: " + n.data());
		}

		return ret;
	}

	@Override
	public Double visit(SemiNode n) {
		double rs = n.right().accept(this);

		return rs;
	}

	@Override
	public Double visit(FunctionNode n) {
		double childValue;
		double ls;
		double rs;
		if(n.data().equals("lb")){
			if(n.childs().get(0)instanceof VariableNode){
				throw new IllegalArgumentException("Lb kann nur ein Argument bekommen.");
			}
			childValue = n.childs().get(0).accept(this);
			return Math.log(childValue)/Math.log(2);
		}
		if(n.data().equals("ln")){
			if(n.childs().get(0)instanceof VariableNode){
				throw new IllegalArgumentException("Ln kann nur ein Argument bekommen.");
			}
			childValue = n.childs().get(0).accept(this);
			return Math.log(childValue);
		}

		if(n.data().equals("pow")){
			AstNode<?> variables = (AstNode<?>) n.childs().get(0);
			if(variables.childs().size()!=2){
				throw new IllegalArgumentException("pow braucht zwei Argumente  #args.");
			}
			ls = variables.childs().get(0).accept(this);
			rs = variables.childs().get(1).accept(this);
			return Math.pow(ls, rs);
		}
		if(n.data().equals("sin")){
			if(n.childs().size()!=1){
				throw new IllegalArgumentException("sin braucht ein Argumente.");
			}
			childValue = n.childs().get(0).accept(this);
			return Math.sin(childValue);
		}
		if(n.data().equals("cos")){
			if(n.childs().size()!=1){
				throw new IllegalArgumentException("cos braucht ein Argumente.");
			}
			childValue = n.childs().get(0).accept(this);
			return Math.cos(childValue);
		}
		if(n.data().equals("acos")){
			if(n.childs().size()!=1){
				throw new IllegalArgumentException("acos braucht ein Argumente.");
			}
			childValue = n.childs().get(0).accept(this);
			return Math.acos(childValue);
		}
		if(n.data().equals("asin")){
			if(n.childs().size()!=1){
				throw new IllegalArgumentException("asin braucht ein Argumente.");
			}
			childValue = n.childs().get(0).accept(this);
			return Math.asin(childValue);
		}
		if(n.data().equals("exp")){
			if(n.childs().size()!=1){
				throw new IllegalArgumentException("exp braucht ein Argumente.");
			}
			childValue = n.childs().get(0).accept(this);
			return Math.exp(childValue);
		}
		if(n.parent() instanceof FunctionAssignNode){
			return ((FunctionAssignNode) n.parent()).right().accept(this);
		}
		AST<?> function = searchFunctionDeclaration(n);
		if (!(AstNode.childsEquals(n, (AstNode) function))) {
			throw new IllegalArgumentException("wrong arguments for script function #args");
		}
		return function.accept(this);
	}
	private FunctionNode searchFunctionDeclaration(FunctionNode toFind){
		AST<?> parentAssignNode = toFind;
		while (!(parentAssignNode.parent() instanceof SemiNode)){
			parentAssignNode = parentAssignNode.parent();
			if(parentAssignNode==null){
				throw new IllegalArgumentException("function reference is null");
			}
		}
		AST<?> iterator = parentAssignNode.parent();
		while (iterator != null){
			if (iterator instanceof SemiNode) {
				iterator = ((SemiNode) iterator).left();
				iterator = iterator.childs().get(0);
				if(iterator instanceof FunctionNode){
					if(iterator.data().equals(toFind.data())&&iterator.parent() instanceof FunctionAssignNode){
						return  ((FunctionNode) iterator);
					}
					iterator = iterator.parent();
				}
				iterator = iterator.parent();
			}
			iterator = iterator.parent();
			if(iterator instanceof SemiNode){
				iterator = iterator.parent();
			}
		}
		throw new IllegalArgumentException("unknown function");
	}
	@Override
	public Double visit(FunctionAssignNode functionAssignNode) {
		double rs = functionAssignNode.expr().accept(this);
		return rs;
	}

	@Override
	public Double visit(VariableNode variableNode) {
		return null;
	}


	private Label hayInNeedleStack(Label hay, FunctionNode functionNode){
		AST<?> iterator = functionNode;
		iterator = (AstNode<?>)iterator.parent();
		while (iterator != null){
			if (iterator instanceof SemiNode) {
				iterator = ((SemiNode) iterator).left();
				iterator = ((AssignNode) iterator).left();
				if(iterator instanceof Label){

                    if(iterator.data().equals(hay.data())){
						return  ((Label) iterator);
					}
					iterator = iterator.parent();
				}
				iterator = iterator.parent();
			}
			if(iterator instanceof SemiNode){
				iterator = iterator.parent();
			}
		}
		throw new IllegalArgumentException("function reference is null");

	}
	/**
	 * (non-Javadoc)
	 *
	 * @see de.fh_muenster.blackboard.scripting.AstVisitor#visit(de.fh_muenster.blackboard.scripting.Label)
	 */
	@Override
	public Double visit(Label n) {
		Blackboard blackboard = Blackboard.getInstance();

		if(n.parent() instanceof AssignNode){
			return blackboard.answer(Double.class, ((AssignNode) n.parent()).expr());
		}
		return blackboard.answer(Double.class, needleInHaystack(n));
	}


	private AssignNode needleInHaystack(Label needle) {


		/*	iteration till the assign node of the needle.
		 */
		AST<?> parentAssignNode = needle;

		while (!(parentAssignNode.parent() instanceof SemiNode)){
			parentAssignNode = parentAssignNode.parent();
			if(parentAssignNode==null){
				throw new IllegalArgumentException("function reference is null");
			}
		}

		AST<?> iterator = needle.parent();

		while (iterator != null){
			if (iterator instanceof SemiNode) {
				iterator = ((SemiNode) iterator).left();

				if((iterator) instanceof AssignNode){
					iterator = ((AssignNode) iterator).left();

					if(iterator.data().equals(needle.data())){
						iterator = iterator.parent();

						return  ((AssignNode) iterator);
					}
					iterator = iterator.parent();
				}
				iterator = iterator.parent();
			}
			iterator = iterator.parent();
			if(iterator instanceof SemiNode && ((SemiNode) iterator).left().equals(parentAssignNode)){
				iterator = iterator.parent();
			}
		}
		throw new IllegalArgumentException("Es gibt diesen Label im Baum nicht.");
	}
}
