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


import static java.lang.Math.cos;
import static java.lang.Math.exp;
import static java.lang.Math.pow;
import static java.lang.Math.sin;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.function.Function;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

/**
 * Extended test for Math and Scripting functions - state of exam II.
 */
public class Praktikum_II_Test extends Praktikum_I_Test {
	Function<double[], Double> fct;
	/**
	 * @throws Exception
	 */
	@BeforeEach
	protected void setUp() throws Exception {
		super.setUp();
		delta = 1.E-6;
	}
	@Test
	@Timeout(1)
	//@Disabled
	public void testSimpleFunction() throws Exception {
		task = define("f(x)=3*x + 2; x=%.8f;  y=f(x)",x1);
		expected = 3*x1 + 2;
		returned = resultOf(task,7);
		assertEquals(expected,returned, delta);
	}

	@Test
	@Timeout(1)
	//@Disabled
	public void test2DFunction() throws Exception {
		task = define("f(x,y)=2*x + 3*y; x=%.8f; y=%.8f; z=f(x,y)",x1,x2);
		expected = 2*x1 + 3*x2;
		returned = resultOf(task,7);
		assertEquals(expected,returned, delta);
	}

	@Test
	@Timeout(1)
	public void testUnkownFunction() throws Exception {
		try {
			task = define("x=%.8f;  y=foo(x)",x1);
			returned = resultOf(task,7);
			fail("unkown function not recognized");
		} catch(IllegalArgumentException e) {
			// log("e: %s",e.getMessage());
			assertTrue(e.getMessage().contains("unknown function"));
		}
	}
	@Test
	@Timeout(1)
	public void testWrongArgumentsForScriptFunction() throws Exception {
		try {
			task = define("x=%.8f; f(x,y)=x*y; z=f(x)",x1);
			returned = resultOf(task,7);
			fail("wrong function argumtens not recognized");
		} catch(IllegalArgumentException e) {
			//log("e: %s",e.getMessage());
			assertTrue(e.getMessage().contains("#args"));
		}
	}
	@Test
	@Timeout(1)
	public void testWrongArgumentsForMathFunction() throws Exception {
		try {
			task = define("x=%.8f; z=pow(x)",x1);
			returned = resultOf(task,7);
			fail("wrong function argumtens not recognized");
		} catch(IllegalArgumentException e) {
			//log("e: %s",e.getMessage());
			assertTrue(e.getMessage().contains("#args"));
		}
	}

	@Test
	@Timeout(1)
	//@Disabled
	public void testExpFunction() throws Exception {
		task = define("f(x)=3*exp(x) + 2; x=%.8f;  y=f(x)",x1);
		expected = 3*exp(x1) + 2;
		returned = resultOf(task,7);
		assertEquals(expected,returned, delta);
	}
	@Test
	@Timeout(1)
	//@Disabled
	public void testSineFunction() throws Exception {
		task = define("f(x)=3*sin(x) + 2; x=%.8f;  y=f(x)",x1);
		expected = 3*sin(x1) + 2;
		returned = resultOf(task,7);
		assertEquals(expected,returned, delta);
	}
	@Test
	@Timeout(1)
	//@Disabled
	public void testCosineFunction() throws Exception {
		task = define("f(x)=3*cos(x) + 2; x=%.8f;  y=f(x)",x1);
		expected = 3*cos(x1) + 2;
		returned = resultOf(task,7);
		assertEquals(expected,returned, delta);
	}
	@Test
	@Timeout(1)
	//@Disabled
	public void testPowerFunction() throws Exception {
		task = define("f(x,y)=pow(x,y); x=%.8f; y=%.8f; z=f(x,y)",x1,x2);
		expected = pow(x1,x2);
		returned = resultOf(task,7);
		assertEquals(expected,returned, delta);
	}
	@Test
	@Timeout(1)
	//@Disabled
	public void testDirectPower() throws Exception {
		task = define("x=%.8f; y=%.8f; z=pow(x,y)",x1,x2);
		expected = pow(x1,x2);
		returned = resultOf(task,7);
		assertEquals(expected,returned, delta);
	}
	@Test
	@Timeout(1)
	public void testDirectSine() throws Exception {
		task = define("-sin(%.8f)",x1);
		expected = -sin(x1);
		returned = resultOf(task,7);
		assertEquals(expected,returned, delta);
	}

	@Test
	@Timeout(1)
	@SuppressWarnings("unchecked")
	//@Disabled
	public void testGetFunction() throws Exception {
		task = define("f(x)= x*exp(-x)");
		Object ref = blackboard.answer(Function.class, task);
		assertNotNull(ref,"function reference is null");
		assertTrue(ref instanceof Function);
		fct = (Function<double[],Double>)Function.class.cast(ref);
		for(int j = 0;j<10; j++) {
			expected = x1*exp(-x1);
			returned = fct.apply(new double[]{x1});
			assertEquals(expected,returned, delta);
		}
	}

	@Test
	@Timeout(1)
	//@Disabled
	public void testCalculatedArguments() throws Exception {
		task = define("f(x)=3*x + 2; x=%.8f;  y=f(x/3-1)",x1);
		expected = x1-1;
		returned = resultOf(task,7);
		assertEquals(expected,returned, delta);
	}

	@Test
	@Timeout(1)
	@SuppressWarnings("unchecked")
	//@Disabled
	public void testTwoDefinitions() throws Exception {
		task = "f(x)= x*exp(-x)";
		blackboard.write(task);
		task = "h(x)=f(3*x)";
		Object ref = blackboard.answer(Function.class, task);
		assertNotNull(ref,"function reference is null");
		assertTrue(ref instanceof Function);
		fct = (Function<double[],Double>)Function.class.cast(ref);
		for(int j = 0;j<10; j++) {
			x1 = rnd();
			expected = 3*x1*exp(-3*x1);
			returned = fct.apply(new double[]{x1});
			assertEquals(expected,returned, delta);
		}
	}

	@Test
	@Timeout(1)
	@SuppressWarnings("unchecked")
	//@Disabled
	public void testSinConvolution() throws Exception {
		task = "f(x)= asin(sin(x))";
		Object ref = blackboard.answer(Function.class, task);
		assertNotNull(ref,"function reference is null");
		assertTrue(ref instanceof Function);
		fct = (Function<double[],Double>)Function.class.cast(ref);
		for(int j = 0;j<10; j++) {
			x1 = rnd();
			expected = x1;
			returned = fct.apply(new double[]{x1});
			assertEquals(expected,returned, delta);
		}
	}
	@Test
	@Timeout(1)
	@SuppressWarnings("unchecked")
	//@Disabled
	public void testCosConvolution() throws Exception {
		task = "f(x)= acos(cos(x))";
		Object ref = blackboard.answer(Function.class, task);
		assertNotNull(ref,"function reference is null");
		assertTrue(ref instanceof Function);
		fct = (Function<double[],Double>)Function.class.cast(ref);
		for(int j = 0;j<10; j++) {
			x1 = rnd();
			expected = x1;
			returned = fct.apply(new double[]{x1});
			assertEquals(expected,returned, delta);
		}
	}
	@Test
	@Timeout(1)
	@SuppressWarnings("unchecked")
	//@Disabled
	public void testExpConvolution() throws Exception {
		task = "f(x)= ln(exp(x))";
		Object ref = blackboard.answer(Function.class, task);
		assertNotNull(ref,"function reference is null");
		assertTrue(ref instanceof Function);
		fct = (Function<double[],Double>)Function.class.cast(ref);
		for(int j = 0;j<10; j++) {
			x1 = rnd();
			expected = x1;
			returned = fct.apply(new double[]{x1});
			assertEquals(expected,returned, delta);
		}
	}

}
