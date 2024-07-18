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

/**
 * Test of the pure Java Differentiator.
 * @author nwulff
 * @since  08.12.2023
 *
 */
class IntegratorNativeTest extends AbstractIntegralTesting {

	/* (non-Javadoc)
	 * 
	 * @see de.fh_muenster.blackboard.analysis.DifferentiatorTesting#createDifferentiator()
	 */

	@Override
	protected Integrator createIntegrator() {
		return new Integrator(false);
	}
}
