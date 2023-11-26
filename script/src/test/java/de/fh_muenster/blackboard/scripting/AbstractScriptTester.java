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


import static java.lang.Math.pow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Locale;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import de.fh_muenster.blackboard.Blackboard;

/**
 * Base class for the test of the blackboard script module as of exam II.
 */

public abstract class AbstractScriptTester {
	protected static Blackboard blackboard;
	protected boolean useLogging = true;
	/** the numeric precision to reach. */
	protected double delta = 5.E-13;
	/** some numbers variables to use.  */
	protected double x1,x2,x3,x4,x5,x6;
	/** number to expect in the test.   */
	protected double expected;
	/** number actual returned in test. */
	protected double returned;
	/** task to be evaluated with the blackboard. */
	protected String task;
	/**
	 * Round to the number of digits.
	 * @param x number to round
	 * @param digits to keep
	 * @return rounded number
	 */
	protected final static double round(double x, int digits) {
		if(x<0) return -round(-x,digits);
		double shift = pow(10, digits);
		x = x*shift + 0.5;
		x = ((long)x);
		x = x/shift;
		return x;		
	}
    /**
     * Generate a random number with specified significant digits.
     * @param digits
     * @return a random with the specified digits
     */
	protected final static double random(int digits) {
		double x = (Math.random()+1)/2;
		x = round(x,digits);
		return x;
	}
	/**
	 * Generate a random between zero and one with three digits after the period.
	 * @return a random in the unit interval
	 */
	protected final static double rnd() {
		return random(3);
	}
	/**
	 * Generate a random between min and max.
	 * @param min minimal value
	 * @param max maximal value
	 * @return random number in the interval
	 */
	protected final static double rnd(double min, double max) {
		double scale = (max - min);
		return min + scale*random(4);
	}
	/**
	 * Test of the internal round operation.
	 */
	@Test
	final void testInternalRound() {
	    assertEquals(0.25,round(0.249999,2),0.0001);
	    assertEquals(0.25,round(0.249,2),0.0001);
	    assertEquals(0.24,round(0.2411,2),0.0001);

	    assertEquals(100.25,round(100.249999,2),0.0001);
	    assertEquals(100.25,round(100.249,2),0.0001);
	    assertEquals(100.24,round(100.2411,2),0.0001);

	    assertEquals(0.25,round(0.249999,4),0.0001);
	    assertEquals(0.249,round(0.249,4),0.0001);
	    assertEquals(0.2411,round(0.2411,4),0.0001);

	    assertEquals(100.25,round(100.249999,4),0.001);
	    assertEquals(100.2490,round(100.249,4),0.0001);
	    assertEquals(100.2411,round(100.2411,4),0.0001);

	    assertEquals(-0.25,round(-0.249999,2),0.0001);
	    assertEquals(-0.25,round(-0.249,2),0.0001);
	    assertEquals(-0.24,round(-0.2411,2),0.0001);

	    assertEquals(-100.25,round(-100.249999,2),0.0001);
	    assertEquals(-100.25,round(-100.249,2),0.0001);
	    assertEquals(-100.24,round(-100.2411,2),0.0001);

	    assertEquals(-0.25,round(-0.249999,4),0.0001);
	    assertEquals(-0.249,round(-0.249,4),0.0001);
	    assertEquals(-0.2411,round(-0.2411,4),0.0001);

	    assertEquals(-100.25,round(-100.249999,4),0.001);
	    assertEquals(-100.2490,round(-100.249,4),0.0001);
	    assertEquals(-100.2411,round(-100.2411,4),0.0001);
	}

	/**
	 * Test of the internal random number generation.
	 */
	@Test
	final void testInternalRnd() {
		double x;
		for(int j=0;j<100;j++) {
			x = rnd();
			//log("rnd: %.5f",x);
			assertTrue(0<=x, "negative rnd: "+x);
			assertTrue(x<=1, "to large rnd: "+x);
		}
	}

	
	/**
	 * Test of the internal random number generation.
	 */
	@Test
	final void testInternalRndMinMax() {
		double x;
		double min = -10;
		double max = +100000;
		for(int j=0;j<100;j++) {
			x = rnd(min,max);
			//log(define("rnd: %.4f",x));
			assertTrue(min<=x, "min rnd violated: "+x);
			assertTrue(x<=max, "max rnd violated: "+x);
		}
	}
	
	/**
	 * Define a String with a period (US) instead of a colon (D) if floating point numbers.
	 * @param fmt format description to use
	 * @param args arguments to format
	 * @return formated String
	 */
	protected final static String define(String fmt, Object...args) {
		return String.format(Locale.US, fmt, args);
	}
	/**
	 * Simple logging function to be turned on or of.
	 * @param fmt format to use
	 * @param args the arguments
	 */
	protected void log(String fmt, Object ...args)  {
		if(useLogging)  {
			System.out.println(String.format(fmt,args));
		}
	}
	
	
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeAll
	final static void internalSetUp() throws Exception {
		blackboard = Blackboard.getInstance();
		// these visitors should register as knowledge source
		// within their constructors ...
		new ValueVisitor();
		new JavaccParser();
		new FunctionVisitor();
		new StringVisitor();
	}
    /**
     * Utility method to get an answer as double
     * @param task to execute with the blackboard
     * @return result
     * @throws Exception in case of an error
     */
	public double resultOf(String task) throws Exception {
		return blackboard.answer(Double.class, task);
	}

    /**
     * Utility method to get an answer as double rounded for digits precision.
     * @param task to execute with the blackboard
     * @param digits to keep
     * @return result rounded
     * @throws Exception in case of an error
     */
	public double resultOf(String task, int digits) throws Exception {
		return round(resultOf(task),digits);
	}
}
