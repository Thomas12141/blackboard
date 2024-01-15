/*
 * Project: analysis
 *
 * Copyright (c) 2023,  Prof. Dr. Nikolaus Wulff
 * University of Applied Sciences, Muenster, Germany
 * Lab for computer sciences (Lab4Inf).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package de.fh.muenster.blackboard.analysis;

import static org.junit.jupiter.api.Assertions.*;
import static java.lang.Math.*;
import java.util.function.Function;

import de.fh_muenster.blackboard.Blackboard;
import de.fh_muenster.blackboard.scripting.DerivativeVisitor;
import de.fh_muenster.blackboard.scripting.FunctionMap;
import de.fh_muenster.blackboard.scripting.Parser;
import de.fh_muenster.blackboard.scripting.VariablesMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

/**
 * Abstract base test of the Differentiator.
 * @author nwulff
 * @since  08.12.2023
 *
 */
abstract class AbstractDifferentiatorTesting {

	Blackboard blackboard;
	Parser parser;
	DerivativeVisitor visitor;
	double delta = 5.E-8;
	Differentiator differentiator;
	Integrator integrator;
	@BeforeEach
	void setUp() {
		differentiator = createDifferentiator();
		integrator = createIntegrator();
	}

	protected abstract Integrator createIntegrator();
	/**
	 * Factory method to create native or java Differentiator.
	 * @return
	 */
	protected abstract Differentiator createDifferentiator();
	/**
	 * Test method for {@link de.fh.muenster.blackboard.analysis.Differentiator#differential(java.util.function.Function)}.
	 */
	@Test
	void testDifferential() {
		double[] arg = new double[1];
		Function<double[],Double> f = (x)-> pow(x[0],3);
		Function<double[],Double> expected = (x)-> 3*pow(x[0],2);
		Function<double[],Double> returned = differentiator.differential(f);
		double expect,retur;
        for(int j=0;j<20;j++) {
        	arg[0] = 2*(Math.random()-0.5);
        	expect = expected.apply(arg);
        	retur  = returned.apply(arg);
        	assertEquals(expect,retur,delta);
        }
	}
	/**
	 * Test method for {@link de.fh.muenster.blackboard.analysis.Differentiator#differentiate(java.util.function.Function, double[])}.
	 */
	@Test
	void testDifferentiate() {
		double[] arg = new double[1];
		Function<double[],Double> f = (x)-> sin(x[0]);
		Function<double[],Double> df = (x)-> cos(x[0]);
		double expected,returned;
        for(int j=0;j<20;j++) {
        	arg[0] = 2*(Math.random()-0.5);
        	expected = df.apply(arg);
        	returned = differentiator.differentiate(f,arg,delta);
        	assertEquals(expected,returned,delta);
        }
	}

	@Test
	public void testSineDerivation() throws Exception {
		VariablesMap.variables.clear();
		FunctionMap.functions.clear();
		parser = new JavaccParser();
		visitor = new DerivativeVisitor();
		blackboard = Blackboard.getInstance();
		String task = String.format("sin'(%.8f)", 2.0);
		double returned = blackboard.answer(Double.class ,task);
		Function<double[], Double> f = (x) -> (x[0]);
		double expected = differentiator.differentiate(f, new double[]{2.0}, delta);
		assertEquals(expected,returned, delta);
	}

	@Test
	void testDifferentiate6() {
		double[] arg = new double[1];
		arg[0] = 2;
		Function<double[],Double> f = (x)-> pow(x[0], 2);
		double beforeDifferentiate = f.apply(arg);
		double afterDifferentiate = differentiator.differentiate(f,arg,delta);
		double[] argI = new double[]{0, afterDifferentiate};
		f = (x)-> 2*x[0];
		double afterIntegrate = integrator.integrate(f, argI, delta);
		assertEquals(beforeDifferentiate,afterIntegrate,delta);
	}
	@Test
	void testDifferentiate2() {
		double[] arg = new double[1];
		Function<double[],Double> f = (x)-> Math.random();
		Function<double[],Double> df = (x)-> cos(x[0]);
		double expected,returned;
		returned = differentiator.differentiate(f,arg,delta);
		fail();
	}

	@Test
	void testDifferentiate3() {
		double[] arg = new double[1];
		Function<double[],Double> f = (x)-> sin(x[0]);
		Function<double[],Double> df = (x)-> cos(x[0]);
		double expected,returned;
		arg[0] = 2*(Math.random()-0.5);
		expected = df.apply(arg);
		returned = differentiator.differentiate(f,arg,delta);
		assertEquals(expected,returned,delta);

	}

}
