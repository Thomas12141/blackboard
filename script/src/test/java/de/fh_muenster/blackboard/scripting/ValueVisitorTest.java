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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import de.fh_muenster.blackboard.Blackboard;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Very basic simple script parser test.
 */
class ValueVisitorTest {
	double delta = 1.E-14;
	Blackboard blackboard;
	Parser parser;
	ExpertenKoordinator visitor;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		blackboard = Blackboard.getInstance();
		visitor = new ExpertenKoordinator();
		parser = new JavaccParser();
	}

	@Test
	@Timeout(2)
	void testPlus() throws Exception {
		String task = "  4    +  3.2";
		AST<?> ast = parser.solve(blackboard, task);
		assertNotNull(ast, "ast is null");
		double returned = (double) ast.accept(visitor);
		double expected = 4 + 3.2;
		assertEquals(expected, returned, delta);
	}

	@Test
	@Timeout(2)
	void testAddition() throws Exception {
		String task = "  3   +  3.2 + 2";
		double expected = 8.2;
		double returned = blackboard.answer(Double.class, task);
		assertEquals(expected, returned, delta);
	}

	@Test
	@Timeout(2)
	void testArithmetic() throws Exception {
		String task = "  7.5 + 3 * 5 - 2.25";
		double expected = 20.25;
		double returned = blackboard.answer(Double.class, task);
		assertEquals(expected, returned, delta);
	}

	@Test
	@Timeout(2)
	void testArrPot() throws Exception {
		String task = "  3 ** 2";
		double expected = 9;
		double returned = blackboard.answer(Double.class, task);
		assertEquals(expected, returned, delta);
	}

	@Test
	@Timeout(2)
	void testPowerCaret() throws Exception {
		String task = "  3 ^ 2 ";
		double expected = 9;
		double returned = blackboard.answer(Double.class, task);
		assertEquals(expected, returned, delta);
	}

	@Test
	@Timeout(2)
	void testPowerCaret2() throws Exception {
		String task = "  3 ^ 2 - 2";
		double expected = 7;
		double returned = blackboard.answer(Double.class, task);
		assertEquals(expected, returned, delta);
	}

	@Test
	@Timeout(2)
	void testMinus() throws Exception {
		String task = "  2 - 2";
		double expected = 0;
		double returned = blackboard.answer(Double.class, task);
		assertEquals(expected, returned, delta);
	}

	@Test
	@Timeout(2)
	void testDivide() throws Exception {
		String task = "  0 / 2";
		double expected = 0;
		double returned = blackboard.answer(Double.class, task);
		assertEquals(expected, returned, delta);
	}

	@Test
	@Timeout(2)
	void testDivide2() throws Exception {
		String task = "  2 / 2 / 1";
		double expected = 1;
		double returned = blackboard.answer(Double.class, task);
		assertEquals(expected, returned, delta);
	}

	@Test
	@Timeout(2)
	void testDivideByZero() throws Exception {
		String task = "  5 / 0";
		try  {
			blackboard.answer(Double.class, task);
			fail("division by zero not detected");
		} catch(IllegalArgumentException e) {
			assertTrue(e.getMessage().contains("division by zero"));
		} catch(Exception e) {
			fail("wrong exception thrown " + e);
		}
	}

	@Test
	@Timeout(2)
	void testMore() throws Exception {
		String task = " 1 + 4    *  3.2";
		double expected = 13.8;
		double returned = blackboard.answer(Double.class, task);
		assertEquals(expected, returned, delta);
	}

	@Test
	@Timeout(2)
	void testMore2() throws Exception {
		String task = " 1 + 4    *  3.2 ^ 2";
		double expected = 1 + 4 * Math.pow(3.2, 2);
		double returned = blackboard.answer(Double.class, task);
		assertEquals(expected, returned, delta);
	}


	@Test
	@Timeout(2)
	void testLogBaseTwo() throws Exception {
		String task = "lb(8)";
		double expected = 3;
		double returned = blackboard.answer(Double.class, task);
		assertEquals(expected, returned, delta);
	}

	@Test
	@Timeout(2)
	void testPow3() throws Exception {
		String task = "pow(2, 2)";
		double expected = 4;
		double returned = blackboard.answer(Double.class, task);
		assertEquals(expected, returned, delta);
	}
}

