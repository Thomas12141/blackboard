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
        VariablesMap.variables.clear();
        FunctionMap.functions.clear();
        parser = new JavaccParser();
        visitor = new DerivativeVisitor();
        blackboard = Blackboard.getInstance();
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
        expected = 0;
        returned = resultOf(task,7);
        assertEquals(expected,returned, delta);
    }

    @Test
    @Timeout(1)
    public void testCosDerivation() throws Exception {
        task = define("cos'(%.8f)", x1);
        expected = 0;
        returned = resultOf(task,7);
        assertEquals(expected,returned, delta);
    }

    @Test
    @Timeout(1)
    public void testLbDerivation() throws Exception {
        task = define("ln'(%.8f)", x1);
        expected = 0;
        returned = resultOf(task,7);
        assertEquals(expected,returned, delta);
    }

    @Test
    @Timeout(2)
    void testPow3() throws Exception {
        String task = " sin'( 2 )";
        Function<double[], Double> returned = blackboard.answer(Function.class, task);
        double expected = 0;
        assertEquals(expected, returned.apply(new double[]{}));
    }

    @Test
    @Timeout(1)
    public void testFctDerivation() throws Exception {
        task = define("f(x) = x + 2; f'(%.8f)", x1);
        expected = 1;
        System.out.println(x1);
        returned = resultOf(task,7);
        assertEquals(expected,returned, delta);
    }

    @Test
    @Timeout(1)
    public void testPowDerivation() throws Exception {
        task = define("f(x) = pow(x,2); f'(%.8f)", x1);
        expected = 2 * x1;
        returned = resultOf(task,7);
        assertEquals(expected,returned, delta);
    }

    @Test
    @Timeout(1)
    public void testFctDerivation2() throws Exception {
        task = define("f(x) = x^2 + 2; f'(%.8f)", x1);
        expected = 2 * x1;
        returned = resultOf(task,7);
        assertEquals(expected,returned, delta);
    }

    @Test
    @Timeout(1)
    public void testFctDerivation2_2() throws Exception {
        task = define("f(x) = pow(x,2); f''(%.8f)", x1);
        Object ref = blackboard.answer(Function.class, task);
        Function<double[], Double> fct = (Function<double[], Double>)ref;
        returned = fct.apply(new double[]{x1});
        expected = 2.0;
        assertEquals(expected,returned, delta);
    }

    @Test
    @Timeout(1)
    public void testSinInFct() throws Exception {
        task = define("f(x) = sin(x); f'(%.8f)", x1);
        expected = cos(x1);
        returned = resultOf(task,7);
        assertEquals(expected,returned, delta);
    }

    @Test
    @Timeout(1)
    public void testSinInFc2() throws Exception {
        task = define("f(x) = sin(x); f''(%.8f)", x1);
        expected = -sin(x1);
        returned = resultOf(task,7);
        assertEquals(expected,returned, delta);
    }

    @Test
    @Timeout(1)
    public void testCosInFct() throws Exception {
        task = define("f(x) = cos(x); f'(%.8f)", x1);
        expected = -(sin(x1));
        returned = resultOf(task,7);
        assertEquals(expected,returned, delta);
    }

    @Test
    @Timeout(1)
    public void testAsinInFct() throws Exception {
        task = define("f(x) = asin(x); f'(%.8f)", x1);
        expected = (1/(Math.pow(1-Math.pow(x1, 2), 0.5)));
        System.out.println(x1);
        Object ref = blackboard.answer(Function.class, task);
        Function<double[], Double> fct = (Function<double[],Double>)Function.class.cast(ref);
        returned = fct.apply(new double[]{x1});
        assertEquals(expected,returned, delta);
    }

    @Test
    @Timeout(1)
    public void testFirstRule() throws Exception {
        task = define("f(x) = asin(x); h(x) = f(x); h'(x)", x1);
        expected = (1/(Math.pow(1-Math.pow(x1, 2), 0.5)));
        System.out.println(x1);
        Object ref = blackboard.answer(Function.class, task);
        Function<double[], Double> fct = (Function<double[],Double>)Function.class.cast(ref);
        returned = fct.apply(new double[]{x1});
        assertEquals(expected,returned, delta);
    }

    @Test
    //@Timeout(1)
    public void testAsinAndSinInFct4() throws Exception { // Regel 4
        task = define("f(x) = x^2; g(x) = 3 * x; h(x) = f(x)^g(x); h'(%.8f)", x1);
        expected = Math.pow(x1, 6 * x1) * (6 + 3 * Math.log(Math.pow(x1, 2)));
        System.out.println(x1);
        Object ref = blackboard.answer(Function.class, task);
        returned = ((Function<double[], Double>) ref).apply(new double[]{x1});
        assertEquals(expected,returned, delta);
    }

    @Test
    //@Timeout(1)
    public void testAsinAndSinInFct5() throws Exception { // Regel 2
        task = define("f(x) = x^2; g(x) = 3 * x; h(x) = f(x) * g(x); h'(%.8f)", x1);
        expected = (2 * x1 * 3 * x1) + Math.pow(x1, 2) * 3;
        System.out.println(x1);
        returned = resultOf(task, 7);
        assertEquals(expected,returned, delta);
    }
    @Test
    //@Timeout(1)
    public void testAsinAndSinInFct5_2() throws Exception { // Regel 2
        task = define("f(x) = x; g(x) = x; h(x) = f(x) * g(x); h'(%.8f)", x1);
        expected = 2 * x1;
        System.out.println(x1);
        returned = resultOf(task, 7);
        assertEquals(expected,returned, delta);
    }
    @Test
    //@Timeout(1)
    public void testAsinAndSinInFct6() throws Exception { // Regel 3
        task = define("f(x) = x^2; g(x) = 3 * x; h(x) = f(x) / g(x); h'(%.8f)", x1);
        expected = ((2*x1*3*x1)-(Math.pow(x1,2)*3))/Math.pow((3*x1),2);
        System.out.println(x1);
        returned = resultOf(task, 7);
        assertEquals(expected,returned, delta);
    }

    @Test
    //@Timeout(1)
    public void testAsinAndSinInFct7() throws Exception { // Regel 1
        task = define("f(x) = x^2; g(x) = 3 * x; h(x) = f(x) + g(x); h'(%.8f)", x1);
        expected = (2*x1)+(3);
        System.out.println(x1);
        returned = resultOf(task, 7);
        assertEquals(expected,returned, delta);
    }

    @Test
    //@Timeout(1)
    public void testAsinAndSinInFct8() throws Exception { // Regel 5
        task = define("f(x) = x^2; g(x) = 3 * x; h(x) = f(g(x)); h'(%.8f)", x1);
        expected = (2*x1)*(3);
        System.out.println(x1);
        returned = resultOf(task, 7);
        assertEquals(expected,returned, delta);
    }

    @Test
    //@Timeout(1)
    public void testAsinAndSinInFct9() throws Exception { // Regel 5
        task = define("f(x) = 2*x; h(x) = f(x); h'(%.8f)", x1);
        expected = 2;
        System.out.println(x1);
        Object ref = blackboard.answer(Function.class, task);
        Function<double[], Double> fct = (Function<double[],Double>)Function.class.cast(ref);
        returned = fct.apply(new double[]{x1});
        assertEquals(expected,returned, delta);
    }

}
