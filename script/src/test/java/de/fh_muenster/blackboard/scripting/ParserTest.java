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
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.Future;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import de.fh_muenster.blackboard.Blackboard;

/**
 * Very basic simple script parser test.
 */
class ParserTest {
	double delta = 1.E-14;
	Blackboard blackboard;
	Parser parser;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		blackboard = Blackboard.getInstance();
		parser = new JavaccParser();
	}

	@Test
	@Timeout(2)
	void testAddition() throws Exception {
		assertNotNull(blackboard, "blackboar is NP");
		String task = "3 + 4";
		Future<?> ref = blackboard.write(task);
		assertNotNull(ref, "ref is null");
		AST<?> ast = blackboard.answer(AST.class, task);
		assertTrue(ast instanceof PlusNode);
		if (ast instanceof PlusNode pn) {
			AST<?> lhs = pn.left();
			AST<?> rhs = pn.right();
			assertEquals(ast, lhs.parent());
			assertEquals(ast, rhs.parent());
			assertEquals(3L, lhs.data());
			assertEquals(4L, rhs.data());
		}
	}

	@Test
	@Timeout(2)
	void testAdditions() throws Exception {
		assertNotNull(blackboard, "blackboar is NP");
		String task = "3 + 4 + 5";
		AST<?> ast = blackboard.answer(AST.class, task);
		assertTrue(ast instanceof PlusNode);
	}

	@Test
	@Timeout(2)
	void testPlus() throws Exception {
		String task = "4 +  3.2";
		AST<?> ast = parser.solve(blackboard, task);
		assertNotNull(ast, "ast is null");
		assertTrue(ast instanceof PlusNode);
		if (ast instanceof PlusNode pn) {
			assertEquals(4L, pn.left().data());
			assertEquals(3.2, pn.right().data());
		}
	}

	@Test
	@Timeout(2)
	void testAssignment() throws Exception {
		String task = " x  = 3.25";
		AST<?> ast = parser.solve(blackboard, task);
		assertNotNull(ast, "ast is null");
		assertTrue(ast instanceof AssignNode);
		if (ast instanceof AssignNode an) {
			assertEquals("x", an.left().data());
			assertEquals(3.25, an.right().data());
		}
	}

	@Test
	@Timeout(2)
	void testThreePlus() throws Exception {
		String task = "4 +  3.2 + 5";
		AST<?> ast = parser.solve(blackboard, task);
		System.out.printf("ast: %s %n", ast);
		assertNotNull(ast, "ast is null");
		assertTrue(ast instanceof PlusNode);
		if (ast instanceof PlusNode pn) {
			assertEquals(5L, pn.right().data());
			Object lh = pn.left();
			assertTrue(lh instanceof PlusNode);
			if (lh instanceof PlusNode ppn) {
				assertEquals(4L, ppn.left().data());
				assertEquals(3.2, ppn.right().data());
			}
		}
	}

	@Test
	@Timeout(2)
	void testMinus() throws Exception {
		String task = "4.3 - 3.2";
		AST<?> ast = parser.solve(blackboard, task);
		assertNotNull(ast, "ast is null");
		assertTrue(ast instanceof MinusNode);
		if (ast instanceof MinusNode mn) {
			assertEquals(4.3, mn.left().data());
			assertEquals(3.2, mn.right().data());
		}
	}

	@Test
	@Timeout(2)
	void testTimes() throws Exception {
		String task = "3.0 * 5";
		AST<?> ast = parser.solve(blackboard, task);
		assertNotNull(ast, "ast is null");
		assertTrue(ast instanceof TimesNode);
	}

	@Test
	@Timeout(2)
	void testDivide() throws Exception {
		String task = "3 / 4.0";
		AST<?> ast = parser.solve(blackboard, task);
		assertNotNull(ast, "ast is null");
		assertTrue(ast instanceof DivideNode);
	}

}
