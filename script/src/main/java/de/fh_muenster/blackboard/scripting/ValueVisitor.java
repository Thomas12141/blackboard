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
	FunctionVisitor functionVisitor = new FunctionVisitor();
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
		VariablesMap.variables.put(((Label)n.left()).data(), n.left().accept(this));

		return VariablesMap.variables.get(((Label)n.left()).data());
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
			case PLUS:
				ret = childValue;
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
		if(n.data().startsWith("lb")){
			if(n.childs().get(0)instanceof VariableNode){
				throw new IllegalArgumentException("Lb kann nur ein Argument bekommen.");
			}
			childValue = n.childs().get(0).accept(this);
			AbstractFunction function = (AbstractFunction) functionVisitor.visit(n);
			return function.apply(new double[]{childValue});
		}
		if(n.data().startsWith("ln")){
			if(n.childs().get(0)instanceof VariableNode){
				throw new IllegalArgumentException("Ln kann nur ein Argument bekommen.");
			}
			childValue = n.childs().get(0).accept(this);
			AbstractFunction function = (AbstractFunction) functionVisitor.visit(n);
			return function.apply(new double[]{childValue});
		}

		if(n.data().startsWith("pow")){
			AstNode<?> variables = (AstNode<?>) n.childs().get(0);
			if(variables.childs().size()!=2){
				throw new IllegalArgumentException("pow braucht zwei Argumente  #args.");
			}
			ls = variables.childs().get(0).accept(this);
			rs = variables.childs().get(1).accept(this);
			AbstractFunction function = (AbstractFunction) functionVisitor.visit(n);
			return function.apply(new double[]{ls, rs});
		}
		if(n.data().startsWith("sin")){
			if(n.childs().size()!=1){
				throw new IllegalArgumentException("sin braucht ein Argumente.");
			}
			childValue = n.childs().get(0).accept(this);
			AbstractFunction function = (AbstractFunction) functionVisitor.visit(n);
			return function.apply(new double[]{childValue});
		}
		if(n.data().startsWith("cos")){
			if(n.childs().size()!=1){
				throw new IllegalArgumentException("cos braucht ein Argumente.");
			}
			childValue = n.childs().get(0).accept(this);
			AbstractFunction function = (AbstractFunction) functionVisitor.visit(n);
			return function.apply(new double[]{childValue});
		}
		if(n.data().startsWith("acos")){
			if(n.childs().size()!=1){
				throw new IllegalArgumentException("acos braucht ein Argumente.");
			}
			childValue = n.childs().get(0).accept(this);
			AbstractFunction function = (AbstractFunction) functionVisitor.visit(n);
			return function.apply(new double[]{childValue});
		}
		if(n.data().startsWith("asin")){
			if(n.childs().size()!=1){
				throw new IllegalArgumentException("asin braucht ein Argumente.");
			}
			childValue = n.childs().get(0).accept(this);
			AbstractFunction function = (AbstractFunction) functionVisitor.visit(n);
			return function.apply(new double[]{childValue});
		}
		if(n.data().startsWith("exp")){
			if(n.childs().size()!=1){
				throw new IllegalArgumentException("exp braucht ein Argumente.");
			}
			childValue = n.childs().get(0).accept(this);
			AbstractFunction function = (AbstractFunction) functionVisitor.visit(n);
			return function.apply(new double[]{childValue});
		}
		if(!FunctionMap.functions.containsKey(n.data().replaceAll("'", ""))){
			throw new IllegalArgumentException("unknown function");
		}
		if(FunctionVisitor.iteratorDerivative(n)>0){
			childValue = n.childs().get(0).accept(this);
			AbstractFunction function = (AbstractFunction) functionVisitor.visit(n);
			return function.apply(new double[]{childValue});
		}
		if(n.parent() instanceof FunctionAssignNode&&((FunctionAssignNode) n.parent()).left().equals(n)){
			return ((FunctionAssignNode) n.parent()).right().accept(this);
		}
		FunctionNode function = FunctionMap.functions.get(n.data());
		if(!(n.parent() instanceof FunctionAssignNode)&&!n.equals(function)){
			ArrayList<Double> variableValues = new ArrayList<Double>();
			AstNode<?> iterator = (AstNode<?>) n.childs().get(0);
			while (iterator!=null){
				if(iterator instanceof Label){
					variableValues.add(iterator.accept(this));
					break;
				}
				else if(iterator instanceof VariableNode){
					variableValues.add(iterator.childs().get(0).accept(this));
					iterator = (AstNode<?>) iterator.childs().get(1);
				}else {
					variableValues.add(iterator.accept(this));
					break;
				}
			}
			if(function.getVariables()!=null&&variableValues.size()!=function.getVariables().size()){
				throw new IllegalArgumentException("The number of the arguments given by the function call is wrong.#args");
			}
			double [] values = new double[variableValues.size()];
			for (int i = 0; i<values.length; i++){
				values[i]=variableValues.get(i);
			}
			function.setFunctionCall(function.accept(new FunctionVisitor()));
			return function.getFunctionCall().apply(values);
		}
		if (!(AstNode.childsEquals(n, (AstNode) function))) {
			throw new IllegalArgumentException("wrong arguments for script function #args");
		}
		return function.accept(this);
	}
	@Override
	public Double visit(FunctionAssignNode functionAssignNode) {
		FunctionMap.functions.put(functionAssignNode.id().data(),(FunctionNode) functionAssignNode.id());
		AST<?> iterator = functionAssignNode.left().childs().get(0);
		ArrayList<String> variables = new ArrayList<String>();
		while (iterator!= null){
			if(iterator instanceof Label){
				variables.add((String) iterator.data());
				break;
			}
			variables.add(iterator.childs().get(0).data().toString());
			iterator = iterator.childs().get(1);
		}
		((FunctionNode) functionAssignNode.id()).setVariableList(variables);
		((FunctionNode) functionAssignNode.id()).setFunctionCall(functionAssignNode.expr().accept(functionVisitor));
		Double answer;
		try{
			answer = functionAssignNode.expr().accept(this);
		}catch (IllegalArgumentException e){
			return 0.0;
		}
		return answer;
	}

	@Override
	public Double visit(VariableNode variableNode) {
		return null;
	}

	@Override
	public Double visit(MasterNode masterNode) {
		ArrayList<AST<?>> trees = (ArrayList) masterNode.childs();
		for (int i = 0; i < trees.size()-1; i++) {
			trees.get(i).accept(this);
		}
		return trees.get(trees.size()-1).accept(this);
	}


	/**
	 * (non-Javadoc)
	 *
	 * @see de.fh_muenster.blackboard.scripting.AstVisitor#visit(de.fh_muenster.blackboard.scripting.Label)
	 */
	@Override
	public Double visit(Label n) {
		if(VariablesMap.variables.containsKey(n.data())&&!(n.parent() instanceof AssignNode)){
			return VariablesMap.variables.get(n.data());
		}else if(n.parent() instanceof AssignNode){
			return ((AssignNode) n.parent()).expr().accept(this);
		}else {
			throw new IllegalArgumentException("ValueVisitor in visit Label, this label wasn't declared.");
		}
	}
}
