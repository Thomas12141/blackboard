package de.fh_muenster.blackboard.scripting;

import de.fh_muenster.blackboard.Blackboard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.function.Function;

import static java.lang.Math.*;
import static org.junit.jupiter.api.Assertions.*;
public class DerivativeVisitorTest extends AbstractScriptTester {
    Blackboard blackboard;
    Parser parser;
    DerivativeVisitor visitor;


    /**
     * @throws Exception
     */
    @BeforeEach
    void setUp() throws Exception {
        visitor = new DerivativeVisitor();
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
    public void testSineDerivation() throws Exception {
        task = define("sin'(%.8f)", x1); // hier Symbol für Ableitung einsetzen
        expected = Math.cos(x1);
        returned = resultOf(task,7);
        assertEquals(expected,returned, delta);
    }

    @Test
    @Timeout(1)
    public void testCosDerivation() throws Exception {
        task = define("cos'(%.8f)", x1);
        expected = Math.sin(x1) * (-1);
        returned = resultOf(task,7);
        assertEquals(expected,returned, delta);
    }

    @Test
    @Timeout(1)
    @Disabled
    public void testLbDerivation() throws Exception {
        task = define("ln'(%.8f)", x1);
        expected = 1/x1;
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
