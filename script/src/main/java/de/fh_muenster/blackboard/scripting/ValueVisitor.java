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
			case POWER, POWERCARET, POWERFUNCTION:
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
		Blackboard blackboard = Blackboard.getInstance();

		return blackboard.answer(Double.class, n.expr());
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
				throw new IllegalArgumentException("unkown operation: " + n.data());
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
		if(n.childs().get(0)==null){

		}
		ArrayList<Double> myDoubles = new ArrayList<Double>();

        ValueVisitor vis = new ValueVisitor();

        for(int i = 0; i < n.getVariables().size(); i++) {
            Label myLabel = hayInNeedleStack(n.getVariables().get(i), n);
            Double myDouble = myLabel.accept(vis);
            myDoubles.add(myDouble);
        }

		double[] myDArray = new double[myDoubles.size()];
		for ( int i = 0; i < myDoubles.size(); i++) {
			myDArray[i] = myDoubles.get(i);
		}

		return n.apply(myDArray);
	}

	/*private FunctionNode functionDeclarationSearch(FunctionNode functionNode){
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
			iterator = iterator.parent();
			if(iterator instanceof SemiNode){
				iterator = iterator.parent();
			}
		}
		throw new IllegalArgumentException("Es gibt diesen Label im Baum nicht.");
	}*/

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
		throw new IllegalArgumentException("Es gibt diesen Label im Baum nicht.");

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
