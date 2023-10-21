/*
 * Project: core
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
package de.fh_muenster.blackboard;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.concurrent.Future;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * 
 */
class BlackboardTest {
	Blackboard bb;

	@BeforeEach
	void setUp() throws Exception {
		bb = Blackboard.getInstance();
	}

	/**
	 * Test method for {@link de.fh_muenster.blackboard.Blackboard#getInstance()}.
	 */
	@Test
	void testGetInstance() {
		assertNotNull(bb, "no instance found by reflection");
	}

	/**
	 * Test method for
	 * {@link de.fh_muenster.blackboard.Blackboard#write(java.lang.Object)}.
	 */
	@Test
	void testWrite() {
		String task = "3 + 4";
		Future<?> solution = bb.write(task);
		assertNotNull(solution, "no solution");
	}

}
