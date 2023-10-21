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

import java.util.Objects;

/**
 * Enumeration of mathematical operations.
 */
public enum Operation {

	PLUS("+"), MINUS("-"), TIMES("*"), DIVIDE("/");

	Operation(String s) {
		op = Objects.requireNonNull(s, "op is null");
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
		return op;
	}

	public static Operation of(String op) {
		for (Operation o : values()) {
			if (o.op.equals(op))
				return o;
		}
		throw new IllegalArgumentException("no operation: " + op);
	}

	private String op;
}
