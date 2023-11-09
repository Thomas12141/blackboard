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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import de.fh_muenster.blackboard.Blackboard;

/**
 * Very basic simple script parser test.
 */
class StringVisitorTest {
	Blackboard blackboard;
	Parser parser;
	StringVisitor visitor;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		visitor = new StringVisitor();
		blackboard = Blackboard.getInstance();
		parser = new JavaccParser();
	}

	@Test
	@Timeout(2)
	void testPlusAst() throws Exception {
		String task = "  4    +  3.2";
		AST<?> ast = parser.solve(blackboard, task);
		assertNotNull(ast, "ast is null");
		String returned = ast.accept(visitor);
		String expected = task.replace(" ", "");
		assertEquals(expected, returned);
	}

	@Test
	@Timeout(2)
	void testPlus() throws Exception {
		String task = "  4    +  3.2";
		String returned = blackboard.answer(String.class, task);
		String expected = task.replace(" ", "");
		assertEquals(expected, returned);
	}

	@Test
	@Timeout(2)
	void testMinus() throws Exception {
		String task = "  4    -  3.2";
		String returned = blackboard.answer(String.class, task);
		String expected = task.replace(" ", "");
		assertEquals(expected, returned);
	}

	@Test
	@Timeout(2)
	void testDivide() throws Exception {
		String task = "  4    /  3.2";
		String returned = blackboard.answer(String.class, task);
		String expected = task.replace(" ", "");
		assertEquals(expected, returned);
	}

	@Test
	@Timeout(2)
	void testDivide2() throws Exception {
		String task = "  4    /  3.2 / 1 / 1 / 3";
		String returned = blackboard.answer(String.class, task);
		String expected = task.replace(" ", "");
		assertEquals(expected, returned);
	}

	@Test
	@Timeout(2)
	void testTimes() throws Exception {
		String task = " 4    *  3.2";
		String returned = blackboard.answer(String.class, task);
		String expected = task.replace(" ", "");
		assertEquals(expected, returned);
	}

	@Test
	@Timeout(2)
	void testTimes2() throws Exception {
		String task = " 4    *  3.2 *3";
		String returned = blackboard.answer(String.class, task);
		String expected = task.replace(" ", "");
		assertEquals(expected, returned);
	}

	@Test
	@Timeout(2)
	void testPow() throws Exception {
		String task = " 4 ^ 2";
		String returned = blackboard.answer(String.class, task);
		String expected = task.replace(" ", "");
		assertEquals(expected, returned);
	}

	@Test
	@Timeout(2)
	void testPow2() throws Exception {
		String task = " 4 ^ 2 ** 3";
		String returned = blackboard.answer(String.class, task);
		String expected = task.replace(" ", "");
		assertEquals(expected, returned);
	}

	@Test
	@Timeout(2)
	void testSemi() throws Exception {
		String task = " 4 ; 2";
		String returned = blackboard.answer(String.class, task);
		String expected = "4;2";
		assertEquals(expected, returned);
	}

	@Test
	@Timeout(2)
	void testAcos() throws Exception {
		String task = " acos( 2 + 2  )";
		String returned = blackboard.answer(String.class, task);
		String expected = task.replace(" ", "");
		assertEquals(expected, returned);
	}

	@Test
	@Timeout(2)
	void testCos() throws Exception {
		String task = " cos( 2 + 2  )";
		String returned = blackboard.answer(String.class, task);
		String expected = task.replace(" ", "");
		assertEquals(expected, returned);
	}

	@Test
	@Timeout(2)
	void testAsin() throws Exception {
		String task = " asin( 2 + 2  )";
		String returned = blackboard.answer(String.class, task);
		String expected = task.replace(" ", "");
		assertEquals(expected, returned);
	}

	@Test
	@Timeout(2)
	void testLn() throws Exception {
		String task = " ln( 2 + 2  )";
		String returned = blackboard.answer(String.class, task);
		String expected = task.replace(" ", "");
		assertEquals(expected, returned);
	}

	@Test
	@Timeout(2)
	void testLb() throws Exception {
		String task = " lb( 2 + 2  )";
		String returned = blackboard.answer(String.class, task);
		String expected = task.replace(" ", "");
		assertEquals(expected, returned);
	}

	@Test
	@Timeout(2)
	void testPow3() throws Exception {
		String task = " pow( 2, 4  )";
		String returned = blackboard.answer(String.class, task);
		String expected = "pow(2,4)";
		assertEquals(expected, returned);
	}


	@Test
	@Timeout(2)
	void testFunction() throws Exception {
		String task = " f(x)=2";
		String returned = blackboard.answer(String.class, task);
		String expected = "f(x)=2";
		assertEquals(expected, returned);
	}
}
