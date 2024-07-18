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

import java.util.function.Function;

import com.sun.jna.Callback;

/** 
 *  Warper for a C function pointer to
 *  be used with JNA
 */
public interface Fct extends Callback {
	/**
	 * Emulate the "C function pointer" as interface.
	 * @param x the argument
	 * @return f(x)
	 */
	double f(double x);
	/**
	 * Decorate a blackboard script or ordinary Function into 
	 * our "C - alike" function pointer interface.
	 * @param f
	 * @return Fct(f)
	 */
	static Fct asFct(Function<double[],Double> f) {
	    Fct fct = (z)->f.apply(asArray(z));
	    //Fct fct = (z)->f.apply(z);
        return fct;			
	}
	/**
	 * Decorate a single double into a double array.
	 * @param x
	 * @return {x}
	 */
	private static double[] asArray(double x) { 
		return new double[] {x};
	}
}
