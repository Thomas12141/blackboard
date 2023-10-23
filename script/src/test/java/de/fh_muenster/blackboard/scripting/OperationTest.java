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
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * 
 */
class OperationTest {

	/**
	 * @throws java.lang.Exception throw an exeption
	 */
	@BeforeEach
	void setUp() throws Exception {
	}

	@Test
	void testPlus() {
		Operation op = Operation.of("+");
		assertEquals(Operation.PLUS, op);
	}

	@Test
	void testMinus() {
		Operation op = Operation.of("-");
		assertEquals(Operation.MINUS, op);
	}

	@Test
	void testTimes() {
		Operation op = Operation.of("*");
		assertEquals(Operation.TIMES, op);
	}

	@Test
	void testDivide() {
		Operation op = Operation.of("/");
		assertEquals(Operation.DIVIDE, op);
	}

	@Test
	void testPower() {
		Operation op = Operation.of("**");
		assertEquals(Operation.POWER, op);
	}

	@Test
	void testUnknown() {
		assertThrows(IllegalArgumentException.class, () -> Operation.of("?"));
	}

}
