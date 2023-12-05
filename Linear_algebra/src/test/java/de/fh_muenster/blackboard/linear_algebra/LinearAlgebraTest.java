package de.fh_muenster.blackboard.linear_algebra;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class LinearAlgebraTest {

    @Test
    @Timeout(1)
    public void vectorAddition(){
        double[] firstVector = new double[]{1,6};
        double[] secondVector = new double[]{-1,6};
        double[] result = LinearAlgebraExpert.vectorAddition(firstVector, secondVector);
        assertArrayEquals(new double[]{0, 12}, result);
    }

    @Test
    @Timeout(1)
    public void vectorMultiplication(){
        double[] firstVector = new double[]{1,6};
        double[] secondVector = new double[]{-1,6};
        double result = LinearAlgebraExpert.vectorMultiplication(firstVector, secondVector);
        assertEquals(35, result);
    }

    @Test
    @Timeout(1)
    public void vectorMatrixMultiplication(){
        double[][] matrix = new double[][]{{1, 2, -3},{2, 9, 0},{6, -1, -2}};
        double[] vector = new double[]{2,3, -1};
        double[] result = LinearAlgebraExpert.vectorMatrixMultiplication(matrix, vector);
        assertArrayEquals(new double[]{11, 31, 11}, result);
    }

    @Test
    @Timeout(1)
    public void metricsAdditionMultiplication(){
        double[][] matrix1 = new double[][]{{5, 2},{6, 3}};
        double[][] matrix2 = new double[][]{{10, 30},{5, 1}};
        double[][] expected = new double[][]{{15, 32},{11, 4}};
        double[][] returned = LinearAlgebraExpert.metricsAddition(matrix1, matrix2);
        assertTrue(Arrays.deepEquals(expected, returned));
    }

    @Test
    @Timeout(1)
    public void matSeriell_1Test(){
        double[][] matrix1 = new double[][]{{5, 2},{6, 3}};
        double[][] matrix2 = new double[][]{{10, 30},{5, 1}};
        double[][] expected = new double[][]{{60.0 , 152.0},{75.0, 183.0}};
        double[][] returned = LinearAlgebraExpert.matSeriell_1(matrix1, matrix2);
        assertTrue(Arrays.deepEquals(expected, returned));
    }

    @Test
    @Timeout(1)
    public void matParallel_1Test() throws InterruptedException {
        double[][] matrix1 = new double[][]{{5, 2},{6, 3}};
        double[][] matrix2 = new double[][]{{10, 30},{5, 1}};
        double[][] expected = new double[][]{{60.0 , 152.0},{75.0, 183.0}};
        double[][] returned = LinearAlgebraExpert.matParallel_2(matrix1, matrix2);
        assertTrue(Arrays.deepEquals(expected, returned));
    }

    @Test
    @Timeout(1)
    public void matSeriell_3Test(){
        double[][] matrix1 = new double[][]{{5, 2},{6, 3}};
        double[][] matrix2 = new double[][]{{10, 30},{5, 1}};
        double[][] expected = new double[][]{{60.0 , 152.0},{75.0, 183.0}};
        double[][] returned = LinearAlgebraExpert.matSeriell_3(matrix1, matrix2);
        assertTrue(Arrays.deepEquals(expected, returned));
    }

    @Test
    @Timeout(1)
    public void matSeriell_4Test(){
        double[][] matrix1 = new double[][]{{5, 2},{6, 3}};
        double[][] matrix2 = new double[][]{{10, 30},{5, 1}};
        double[][] expected = new double[][]{{60.0 , 152.0},{75.0, 183.0}};
        double[][] returned = LinearAlgebraExpert.matSeriell_4(matrix1, matrix2);
        assertTrue(Arrays.deepEquals(expected, returned));
    }
}
