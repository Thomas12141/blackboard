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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.function.Function;

import static java.lang.Math.exp;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Very basic simple script parser test.
 */
class FunctionVisitorTest extends AbstractScriptTester{
	Blackboard blackboard;
	Parser parser;
	FunctionVisitor visitor;

	Function<double [], Double> fct;
	/**
	 * @throws Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		visitor = new FunctionVisitor();
		blackboard = Blackboard.getInstance();
		parser = new JavaccParser();
		delta = 1.E-6;
		x1 = rnd(-1,1);
		x2 = rnd(-4,-0.1);
		x3 = rnd(0.1,1);
		x4 = rnd(0,1);
		x5 = rnd(0,1);
		x6 = rnd(0,1);
	}

	@Test
	@Timeout(1)
	@SuppressWarnings("unchecked")
	public void testGetFunction() throws Exception {
		task = define("f(x)= 3+2");
		Object ref = blackboard.answer(Function.class, task);
		assertNotNull(ref,"function reference is null");
		assertTrue(ref instanceof Function);
		fct = (Function<double[],Double>)Function.class.cast(ref);
		for(int j = 0;j<10; j++) {
			expected = 3+2;
			returned = fct.apply(new double[]{x1});
			assertEquals(expected,returned, delta);
		}
	}
}
