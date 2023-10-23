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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

/**
 * Base class for the test of the blackboard script module.
 * Testing simple expressions and variables - state of exam I.
 */
public class Praktikum_I_Test extends AbstractScriptTester {

   /**
    * Generate some random numbers, positive, negative, none null.
    * @throws Exception
    */
	@BeforeEach
	protected void setUp() throws Exception {
		x1 = rnd(-1,1);    
		x2 = rnd(-4,-0.1);
		x3 = rnd(0.1,1);
		x4 = rnd(0,1);
		x5 = rnd(0,1);
		x6 = rnd(0,1);
	}
	@Test
	@Timeout(1)
	public void testIllegalSyntax() throws Exception {
		task = define("&%.4f @  $%.4f",x1,x2);
		try  {
			blackboard.answer(Double.class, task);
		    fail("illegal input not detected");
		} catch(IllegalArgumentException e) {
			// ok
			// log("e: %s",e);
		} catch(Exception e) {
			fail("wrong exception thrown " + e);
		}
	}
	
	@Test
	@Timeout(1)
	public void testConstantInteger() throws Exception {
		task = "3";
		expected = 3;
		returned = blackboard.answer(Double.class, task);
		assertEquals(expected,returned, delta);
	}
	@Test
	@Timeout(1)
	public void testPositveConstantInteger() throws Exception {
		task = "+3";
		expected = 3;
		returned = blackboard.answer(Double.class, task);
		assertEquals(expected,returned, delta);
	}
	@Test
	@Timeout(1)
	public void testNegativeConstantInteger() throws Exception {
		task = "-3";
		expected = -3;
		returned = blackboard.answer(Double.class, task);
		assertEquals(expected,returned, delta);
	}

	@Test
	@Timeout(1)
	public void testConstantDouble() throws Exception {
		for(int j=0;j<10;j++) {
			x1 = rnd();
			task = define("%.4f",x1);
			expected = x1;
			returned = blackboard.answer(Double.class, task);
			assertEquals(expected,returned, delta);
		}
	}
	@Test
	@Timeout(1)
	public void testPositivConstantDouble() throws Exception {
		for(int j=0;j<10;j++) {
			x1 = rnd();
			task = define("+%.4f",x1);
			expected = x1;
			returned = blackboard.answer(Double.class, task);
			assertEquals(expected,returned, delta);
		}
	}

	@Test
	@Timeout(1)
	public void testNegativConstantDouble() throws Exception {
		for(int j=0;j<10;j++) {
			x1 = rnd();
			task = define("-%.4f",x1);
			expected = -x1;
			returned = blackboard.answer(Double.class, task);
			assertEquals(expected,returned, delta);
		}
	}

    @Test
	@Timeout(1)
	public void testPlus() throws Exception {
		for(int j=0;j<10;j++) {
			x1 = rnd();
			x2 = rnd();
			task = define("%.4f + %.4f",x1,x2);
			returned = blackboard.answer(Double.class, task);
			expected = x1 + x2;
			assertEquals(expected,returned, delta);
		}
	}

	@Test
	@Timeout(1)
	public void testPlusMissingOperand() throws Exception {
		task = define("%.4f + ",x1);
		try {
		    blackboard.answer(Double.class, task);
		    fail("missing operand not detected");
		} catch(IllegalArgumentException e) {
			// ok
			//log("e: %s",e);
		}
	}

	@Test
	@Timeout(1)
	public void testMinus() throws Exception {
		for(int j=0;j<10;j++) {
			x1 = rnd();
			x2 = rnd();
			task = define("%.4f - %.4f",x1,x2);
			returned = blackboard.answer(Double.class, task);
			expected = x1 - x2;
			assertEquals(expected,returned, delta);
		}
	}
	@Test
	@Timeout(1)
	public void testMinusMissingOperand() throws Exception {
		task = define("%.4f - ",x1);
		try {
		    blackboard.answer(Double.class, task);
		    fail("missing operand not detected");
		} catch(IllegalArgumentException e) {
			//log("e: %s",e);
		}

	}

	@Test
	@Timeout(1)
	public void testManyMinus() throws Exception {
		for(int j=0;j<10;j++) {
		 	x1 = rnd();
			x2 = rnd();
		 	x3 = rnd();
			x4 = rnd();
			task = define("%.4f - %.4f - %.4f - %.4f",x1,x2,x3,x4);
			returned = resultOf(task,5);
			expected = round(x1 - x2 - x3  - x4,5);
			assertEquals(expected,returned, delta);
		}
	}
	@Test
	@Timeout(1)
	public void testMinusNegativNumber() throws Exception {
		for(int j=0;j<10;j++) {
			x2 = rnd(-2,-1);
		 	x3 = rnd(-3,-1);
			x4 = rnd(-5,-2);
			task = define("-%.4f-%.4f -%.4f - %.4f",x1,x2,x3,x4);
			// log("task: %s",task);
			returned = resultOf(task,5);
			expected = round(-x1 - x2 - x3  - x4,5);
			assertEquals(expected,returned, delta);
		}
	}

	@Test
	@Timeout(1)
	public void testTimes() throws Exception {
		for(int j=0;j<10;j++) {
			x1 = rnd();
			x2 = rnd();
			task = define("%.4f * %.4f",x1,x2);
			returned = blackboard.answer(Double.class, task);
			expected = x1 * x2;
			assertEquals(expected,returned, delta);
		}
	}
	
	@Test
	@Timeout(1)
	public void testTimesMissingOperand() throws Exception {
		task = define("%.4f * ",x1);
		try {
		    blackboard.answer(Double.class, task);
		    fail("missing operand not detected");
		} catch(IllegalArgumentException e) {
			//log("e: %s",e);
		}

		task = define("* %.4f ",x1);
		try {
		    blackboard.answer(Double.class, task);
		    fail("missing operand not detected");
		} catch(IllegalArgumentException e) {
			//log("e: %s",e);
		}

	}

	@Test
	@Timeout(1)
	public void testDivide() throws Exception {
		for(int j=0;j<10;j++) {
			x1 = rnd();
			x2 = rnd();
			task = define("%.4f / %.4f",x1,x2);
			returned = blackboard.answer(Double.class, task);
			expected = x1 / x2;
			assertEquals(expected,returned, delta);
		}
	}
	@Test
	@Timeout(1)
	public void testDivideLeftAssociative() throws Exception {
		for(int j=0;j<10;j++) {
			x1 = rnd();
			x2 = rnd();
			x3 = rnd();
			expected = x1 / x2 / x3;
			task = define("%.4f / %.4f /%.4f",x1,x2,x3);
			// log("task: %s",task);
			returned = resultOf(task);
			assertEquals(expected,returned, delta);
			task = define("(%.4f / %.4f) /%.4f",x1,x2,x3);
			returned = blackboard.answer(Double.class, task);
			assertEquals(expected,returned, delta);
		}
	}
	
	@Test
	@Timeout(1)
	public void testDivisionByZero() throws Exception {
		for(int j=0;j<2;j++) {
			x1 = rnd(-1,+1);
			x2 = 0.0;
			task = define("%.4f/ %.4f",x1,x2);
			try  {
				blackboard.answer(Double.class, task);
			    fail("division by zero not detected");
			} catch(IllegalArgumentException e) {
				assertTrue(e.getMessage().contains("division by zero"));
			} catch(Exception e) {
				fail("wrong exception thrown " + e);
			}
		}
	}
	@Test
	@Timeout(1)
	public void testDivideMissingOperand() throws Exception {
		task = define("%.4f / ",x1);
		try {
		    blackboard.answer(Double.class, task);
		    fail("missing operand not detected");
		} catch(IllegalArgumentException e) {
			//log("e: %s",e);
		}

		task = define(" / %.4f ",x1);
		try {
		    blackboard.answer(Double.class, task);
		    fail("missing operand not detected");
		} catch(IllegalArgumentException e) {
			//log("e: %s",e);
		}
	}

	@Test
	@Timeout(1)
	public void testPower() throws Exception {
		for(int j=0;j<10;j++) {
			x1 = rnd();
			x2 = rnd();
			task = define("%.4f ** %.4f",x1,x2);
			returned = blackboard.answer(Double.class, task);
			expected = Math.pow(x1,x2);
			assertEquals(expected,returned, delta);
		}
	}
	@Test
	@Timeout(1)
	public void testPowerCaret() throws Exception {
		for(int j=0;j<10;j++) {
			x1 = rnd();
			x2 = rnd();
			task = define("%.4f ^ %.4f",x1,x2);
			returned = blackboard.answer(Double.class, task);
			expected = Math.pow(x1,x2);
			assertEquals(expected,returned, delta);
		}
	}

	@Test
	@Timeout(1)
	public void testPowerNegativIntegerExponent() throws Exception {
		for(int j=0;j<10;j++) {
			x1 = rnd();
			if(j%2==0)x1=-x1;
			x2 = -3;
			task = define("%+7.4f ** %.4f",x1,x2);
			returned = blackboard.answer(Double.class, task);
			//log("task: %s",task);
			expected = 1/(x1*x1*x1);
			assertEquals(expected,returned, delta);
		}
 	}

	@Test
	@Timeout(1)
	public void testComplexPower() throws Exception {
		for(int j=0;j<2;j++) {
			x1 = rnd(-1,-0.5);
			x2 = rnd(0.1,0.9);
			task = define("%.4f ** %.4f",x1,x2);
			try  {
				blackboard.answer(Double.class, task);
			    fail("complex root not detected");
			} catch(IllegalArgumentException e) {
				assertTrue(e.getMessage().contains("complex number"));
			} catch(Exception e) {
				fail("wrong exception thrown " + e);
			}
		}
	}
	
	@Test
	@Timeout(1)
	public void testPowerRightAssociative() throws Exception {
		for(int j=0;j<10;j++) {
			x1 = rnd();
			x2 = rnd();
			x3 = rnd();
			expected = pow(x1,pow(x2,x3));
			task = define("%.4f ** %.4f ** %.4f",x1,x2,x3);
			// log("task: %s",task);
			returned = resultOf(task);
			assertEquals(expected,returned, delta);
			task = define("%.4f ** (%.4f ** %.4f)",x1,x2,x3);
			returned = resultOf(task);
			assertEquals(expected,returned, delta);
		}
	}
	@Test
	@Timeout(1)
	public void testPowerIdentity() throws Exception {
		for(int j=0;j<10;j++) {
			x1 = rnd();
			x2 = rnd();
			x3 = rnd();
			task = define("(%.4f ** %.4f) ** %.4f - %.4f**(%.4f*%4f)",x1,x2,x3,x1,x2,x3);
			returned = resultOf(task);
			expected = 0;
			assertEquals(expected,returned, delta);
		}
	}
	@Test
	@Timeout(1)
	public void testArithmetic() throws Exception {
		//delta = 5.E-12;
		for(int j=0;j<10;j++) {
			x1 = rnd(-1,1);
			x2 = rnd();
			x3 = rnd(0.1,1);
			x4 = rnd();
			x5 = rnd();
			task = define("%.5f + %.5f / %.5f - %.5f**%.5f*%.5f",x1,x2,x3,x4,x5,x6);
			returned = resultOf(task);
			expected = x1 + x2 / x3 - Math.pow(x4,x5)*x6;
			assertEquals(expected,returned, delta);
		}
	}
	@Test
	@Timeout(1)
	public void testBracket() throws Exception {
		//delta = 5.E-12;
		for(int j=0;j<10;j++) {
			x1 = rnd(-1,1);
			x2 = rnd();
			x3 = rnd(0.1,1);
			x4 = rnd();
			x5 = rnd();
			task = define("%.5f / %.5f /(%.5f + %.5f)**-%.5f - %.5f",x1,x2,x3,x4,x5,x6);
			returned = resultOf(task);
			expected = x1 / x2 /pow(x3+x4,-x5) -x6;
			assertEquals(expected,returned, delta);
		}
	}

	@Test
	@Timeout(1)
	public void testAssign() throws Exception {
		for(int j=0;j<10;j++) {
			x1 = rnd(-1,1);
			task = define("x=%.5f",x1);
			returned = resultOf(task);
			expected = x1;
			assertEquals(expected,returned, delta);
		}
	}
	@Test
	@Timeout(1)
	public void testAssignmentVariable() throws Exception {
		for(int j=0;j<10;j++) {
			x1 = rnd(-1,1);
			task = define("x=%.5f; x",x1);
			returned = resultOf(task);
			expected = x1;
			assertEquals(expected,returned, delta);
		}
	}

	@Test
	@Timeout(1)
	public void testVariablesArithmetic() throws Exception {
		for(int j=0;j<10;j++) {
			task = define("x=%.5f; y=%.5f; x+y",x1,x2);
			returned = resultOf(task);
			expected = x1+x2;
			assertEquals(expected,returned, delta);
		}
	}
	@Test
	@Timeout(1)
	public void testVariablesAssignment() throws Exception {
		for(int j=0;j<10;j++) {
			task = define("x=%.5f; y=%.5f; z=x*y",x1,x2);
			returned = resultOf(task);
			expected = x1*x2;
			assertEquals(expected,returned, delta);
		}
	}

	@Test
	@Timeout(1)
	public void testReAssign() throws Exception {
		for(int j=0;j<10;j++) {
			x1 = rnd(0.1,1);
			x2 = rnd(-1,1);
			task = define("x=%.5f; y=%.5f; z=x+y; y=x^y**z; y",x1,x2);
			//log("task: %s",task);
			returned = resultOf(task);
			expected = pow(x1,pow(x2,x1+x2));
			assertEquals(expected,returned, delta);
		}
	}
	@Test
	@Timeout(1)
	public void testBracketWithVariables() throws Exception {
		for(int j=0;j<10;j++) {
			x1 = rnd(0.1,1);
			x2 = rnd(-1,1);
			task = define("x=%.5f; y=%.5f; z=%.5f; a=%.5f; x=a*x**(y+z); x",x1,x2,x3,x4);
			//log("task: %s",task);
			returned = resultOf(task);
			expected = x4*pow(x1,x2+x3);
			assertEquals(expected,returned, delta);
		}
	}
	@Test
	@Timeout(1)
	public void testOpenBracket() throws Exception {
		task = define(" ( %.4f +  %.4f",x1,x2);
		try  {
			blackboard.answer(Double.class, task);
		    fail("open bracket not detected");
		} catch(IllegalArgumentException e) {
			// ok
			// log("e: %s",e);
		} catch(Exception e) {
			fail("wrong exception thrown " + e);
		}
	}

	@Test
	@Timeout(1)
	public void testNoneOpenedBracket() throws Exception {
		task = define("%.4f +  %.4f )",x1,x2);
		assertThrows(IllegalArgumentException.class, ()->blackboard.answer(Double.class,task));
	}

}
