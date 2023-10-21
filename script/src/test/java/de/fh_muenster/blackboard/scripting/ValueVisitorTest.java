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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import de.fh_muenster.blackboard.Blackboard;

/**
 * Very basic simple script parser test.
 */
class ValueVisitorTest {
	double delta = 1.E-14;
	Blackboard blackboard;
	Parser parser;
	ValueVisitor visitor;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		blackboard = Blackboard.getInstance();
		visitor = new ValueVisitor();
		parser = new JavaccParser();
	}

	@Test
	@Timeout(2)
	void testPlus() throws Exception {
		String task = "  4    +  3.2";
		AST<?> ast = parser.solve(blackboard, task);
		assertNotNull(ast, "ast is null");
		double returned = ast.accept(visitor);
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
}
