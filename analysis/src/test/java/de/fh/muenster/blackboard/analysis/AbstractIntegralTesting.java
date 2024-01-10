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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static java.lang.Math.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Abstract base test of the Differentiator.
 * @author nwulff
 * @since  08.12.2023
 *
 */
abstract class AbstractIntegralTesting {
	double delta = 0.1;
	Integrator integrator;
	@BeforeEach
	void setUp() {
		integrator = createIntegrator();
	}
	/**
	 * Factory method to create native or java Differentiator.
	 * @return
	 */
	protected abstract Integrator createIntegrator();
	@Test
	void testIntegral() {
		double[] arg = new double[2];
		arg[0] = 5;
		arg[1] = 6;
		Function<double[],Double> f = (x)-> pow(x[0],2);
		Function<double[],Double> expected = (x)-> (pow(x[1],3)-pow(x[0],3))/3;
		Function<double[],Double> returned = integrator.integral(f);
		assertEquals(expected.apply(new double[]{5, 6}),returned.apply(arg),delta);
		for(int j=0;j<20;j++) {
			arg[0] = 2*(Math.random()+2);
			arg[1] = 2*(Math.random()+2);
			double expect = expected.apply(arg);
			double retur  = returned.apply(arg);
			assertEquals(expect,retur,delta);
		}
	}
	@Test
	void testDifferentiate() {
		double[] arg = new double[2];
		arg[0] = 0;
		arg[1] = PI/2;
		Function<double[],Double> integf = (x)-> sin(x[1]-sin(x[0]));
		Function<double[],Double> f = (x)-> cos(x[0]);
		double expected,returned;
        for(int j=0;j<20;j++) {
			arg[0] = (Math.random()-0.5);
			arg[1] = (Math.random()-0.5);
        	expected = integf.apply(arg);
        	returned = integrator.integrate(f,arg,delta);
        	assertEquals(expected,returned,delta);
        }
	}


}
