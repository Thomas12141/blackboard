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
import java.util.function.Function;

/**
 *	A value visitor for ast.
 */
public class FunctionVisitor extends AbstractAstVisitor<Function<double [], Double>> {

	/**
	 * (non-Javadoc)
	 *
	 * @see AstVisitor#visit(LongValue)
	 */
	@Override
	public Function<double [], Double> visit(LongValue n) {
		return (a) ->{
			return Double.valueOf(n.data());
		};
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see AstVisitor#visit(DoubleValue)
	 */
	@Override
	public Function<double [], Double> visit(DoubleValue n) {
		return (a) ->{
			return n.data();
		};
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see AstVisitor#visit(OperationNode)
	 */
	@Override
	public Function<double[], Double> visit(OperationNode n) {
		double ret = 0;
		Function<double[], Double> ls = n.left().accept(this);
		Function<double[], Double> rs = n.right().accept(this);
		Operation op = n.data();

		switch (op) {
			case PLUS:
				return (a) -> {return ls.apply(a) + rs.apply(a);};
			case MINUS:
				return (a) -> {return ls.apply(a) - rs.apply(a);};
			case TIMES:
				return (a) -> {return ls.apply(a) * rs.apply(a);};
			case DIVIDE:

				return (a) -> {
					if(a[0] == 0){
						throw new IllegalArgumentException("division by zero");
					}
					return a[0] / a[1];};
			case POWER, POWERCARET:
				return (a) -> {
					if(Double.isNaN(Math.pow(a[0], a[1]))){
						throw new IllegalArgumentException("complex number");
					}
					return  Math.pow(a[0], a[1]);
				};
		}
		throw new IllegalArgumentException("unkown operation: " + op);
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see AstVisitor#visit(AssignNode)
	 */
	@Override
	public Function<double[], Double> visit(AssignNode n) {
		return n.right().accept(this);
	}

	@Override
	public Function<double[], Double> visit(UnaryOperationNode n) {

		switch (n.data()){
			case MINUS:
				return (a)->{return -n.childs().get(0).accept(this).apply(a);};
		}
		throw new IllegalArgumentException("unknown operation: " + n.data());
	}

	@Override
	public Function<double[], Double> visit(SemiNode n) {
		return n.right().accept(this);
	}

	@Override
	public Function<double[], Double> visit(FunctionNode n) {
		double childValue;
		double ls;
		double rs;
		if(n.data().equals("lb")){
			return (a)->{
				if (a.length!=1){
					throw new IllegalArgumentException("Lb kann nur ein Argument bekommen.");
				}
				if(a[0]<0){
					throw new IllegalArgumentException("Lb kann nur Werten größer gleich 0 bekommen.");
				}
				return Math.log(a[0])/Math.log(2);
			};
		}
		if(n.data().equals("ln")){
			return (a)->{

				if (a.length!=1){
					throw new IllegalArgumentException("Ln kann nur ein Argument bekommen.");
				}
				if(a[0]<0){
					throw new IllegalArgumentException("Ln kann nur Werten größer gleich 0 bekommen.");
				}
				return Math.log(a[0]);
			};
		}

		if(n.data().equals("pow")){
			return (a)->{
				if(a.length!=2){
					throw new IllegalArgumentException("pow braucht zwei Argumente  #args.");
				}
				if(Double.isNaN(Math.pow(a[0], a[1]))){
					throw new IllegalArgumentException("complex number");
				}
				return  Math.pow(a[0], a[1]);
			};
		}
		if(n.data().equals("sin")){
			return (a)->{
				if(a.length!=1){
					throw new IllegalArgumentException("sin braucht ein Argument.");
				}
				return  Math.sin(a[0]);
			};
		}
		if(n.data().equals("cos")){
			return (a)->{
				if(a.length!=1){
					throw new IllegalArgumentException("cos braucht ein Argument.");
				}
				return  Math.cos(a[0]);
			};
		}
		if(n.data().equals("acos")){
			return (a)->{
				if(a.length!=1){
					throw new IllegalArgumentException("acos braucht ein Argument.");
				}
				return  Math.acos(a[0]);
			};
		}
		if(n.data().equals("asin")){
			return (a)->{
				if(a.length!=1){
					throw new IllegalArgumentException("asin braucht ein Argument.");
				}
				return  Math.asin(a[0]);
			};
		}
		if(n.data().equals("exp")){
			return (a)->{
				if(a.length!=1){
					throw new IllegalArgumentException("exp braucht ein Argument.");
				}
				if(a[0]<1){
					throw new IllegalArgumentException("complex number");
				}
				return  Math.exp(a[0]);
			};
		}
		n.setFunctionCall((FunctionCall) n.accept(new FunctionVisitor()));
		if(n.parent() instanceof FunctionAssignNode){
			return ((FunctionAssignNode) n.parent()).right().accept(this);
		}
		AST<?> function = searchFunctionDeclaration(n);
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
	public Function<double[], Double> visit(FunctionAssignNode functionAssignNode) {
		return functionAssignNode.expr().accept(this);
	}

	@Override
	public Function<double[], Double> visit(VariableNode variableNode) {
		return null;
	}

	@Override
	public Function<double[], Double> visit(MasterNode masterNode) {
		ArrayList<AST<?>> trees = (ArrayList) masterNode.childs();
		for (int i = 0; i < trees.size()-1; i++) {
			trees.get(i).accept(this);
		}
		return trees.get(trees.size()-1).accept(this);
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see AstVisitor#visit(Label)
	 */
	@Override
	public Function<double[], Double> visit(Label n) {
		return (a)->{

			Blackboard blackboard = Blackboard.getInstance();

			if(n.parent() instanceof AssignNode){
				return blackboard.answer(Double.class, ((AssignNode) n.parent()).expr());
			}
			return blackboard.answer(Double.class, needleInHaystack(n));
		};
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
