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
	double delta = 5.E-8;
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
		arg[0] = 2*(Math.random()-0.5);
		arg[1] = 2*(Math.random()-0.5);
		Function<double[],Double> f = (x)-> pow(x[0],2);
		Function<double[],Double> expected = (x)-> (pow(x[0],3)-pow(x[1],3))/3;
		Function<double[],Double> returned = integrator.integral(f);
		double expect,retur;
        for(int j=0;j<20;j++) {
        	arg[0] = 2*(Math.random()-0.5);
        	expect = expected.apply(arg);
        	retur  = returned.apply(arg);
        	assertEquals(expect,retur,delta);
        }
	}
	@Test
	void testDifferentiate() {
		double[] arg = new double[1];
		Function<double[],Double> f = (x)-> sin(x[0]);
		Function<double[],Double> df = (x)-> cos(x[0]);
		double expected,returned;
        for(int j=0;j<20;j++) {
        	arg[0] = 2*(Math.random()-0.5);
        	expected = df.apply(arg);
        	//returned = differentiator.differentiate(f,arg,delta);
			returned = 0;
        	assertEquals(expected,returned,delta);
        }
	}
}
