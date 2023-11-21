package de.fh_muenster.blackboard.scripting;

import de.fh_muenster.blackboard.Blackboard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.function.Function;

import static java.lang.Math.exp;
import static java.lang.Math.sin;
import static org.junit.jupiter.api.Assertions.*;
public class DerivativeVisitorTest extends AbstractScriptTester {
    Blackboard blackboard;
    Parser parser;
    FunctionVisitor visitor;

    Function<double [], Double> fct;
    /**
     * @throws Exception
     */
    @BeforeEach
    void setUp() throws Exception {
        visitor = new FunctionVisitor();
        blackboard = Blackboard.getInstance();
        parser = new JavaccParser();
        delta = 1.E-6;
        x1 = rnd(-1,1);
        x2 = rnd(-4,-0.1);
        x3 = rnd(0.1,1);
        x4 = rnd(0,1);
        x5 = rnd(0,1);
        x6 = rnd(0,1);
    }

    @Test
    @Timeout(1)
    @SuppressWarnings("unchecked")
    public void testGetFunction() throws Exception {
        task = define("f(x)= 3+ 2");
        Object ref = blackboard.answer(Function.class, task);
        assertNotNull(ref,"function reference is null");
        assertTrue(ref instanceof Function);
        fct = (Function<double[],Double>)Function.class.cast(ref);
        for(int j = 0;j<10; j++) {
            expected = 3+2;
            returned = fct.apply(new double[]{x1});
            assertEquals(expected,returned, delta);
        }
    }

    @Test
    @Timeout(1)
    public void testSineDerivation() throws Exception {
        task = define("sin(%.8f)",x1); // hier Symbol für Ableitung einsetzen
        expected = Math.cos(x1);
        returned = resultOf(task,7);
        assertEquals(expected,returned, delta);
    }

    @Test
    @Timeout(1)
    public void testCosDerivation() throws Exception {
        task = define("cos(%.8f)",x1);
        expected = Math.sin(x1) * (-1);
        returned = resultOf(task,7);
        assertEquals(expected,returned, delta);
    }

    @Test
    @Timeout(1)
    @Disabled
    public void testLbDerivation() throws Exception {
        task = define("cos(%.8f)",x1);
        expected = Math.sin(x1) * (-1);
        returned = resultOf(task,7);
        assertEquals(expected,returned, delta);
    }

    @Test
    @Timeout(2)
    void testPow3() throws Exception {
        String task = " sin'( 2, 4  )";
        String returned = blackboard.answer(String.class, task);
        String expected = "sin'(2,4)";
        assertEquals(expected, returned);
    }
}
