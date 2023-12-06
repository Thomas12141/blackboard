package de.fh_muenster.blackboard.linear_algebra;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class LinearAlgebraTest {
    private static double delta = 0.1;

    private double[][] bigMatrix = new double[4096][4096];
    private double[][] hilbertBigMatrix = LinearAlgebraExpert.hilbertMatrix(4096);
    private double[][] hilbertBigInvers = LinearAlgebraExpert.hilbertInverse(4096);

    public LinearAlgebraTest() throws InterruptedException {
    }

    @BeforeEach
    void setUp(){
        for (int i = 0; i < 4096; i++) {
            for (int j = 0; j < 4096; j++) {
                bigMatrix[i][j] = (Math.random()+0.5)*2;
            }
        }
    }
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
        assertMatrixEquals(expected, returned);
    }

    @Test
    @Timeout(1)
    public void matSeriell_1Test(){
        double[][] matrix1 = new double[][]{{5, 2},{6, 3}};
        double[][] matrix2 = new double[][]{{10, 30},{5, 1}};
        double[][] expected = new double[][]{{60.0 , 152.0},{75.0, 183.0}};
        double[][] returned = LinearAlgebraExpert.matSeriell_1(matrix1, matrix2);
        assertMatrixEquals(expected, returned);
    }

    @Test
    @Timeout(1)
    public void matParallel_1Test() throws InterruptedException {
        double[][] matrix1 = new double[][]{{5, 2},{6, 3}};
        double[][] matrix2 = new double[][]{{10, 30},{5, 1}};
        double[][] expected = new double[][]{{60.0 , 152.0},{75.0, 183.0}};
        double[][] returned = LinearAlgebraExpert.matParallel_2(matrix1, matrix2);
        assertMatrixEquals(expected, returned);
    }

    @Test
    @Timeout(1)
    public void matParallel_1TestWithHilbert() throws InterruptedException {
        double[][] matrix1 = new double[][]{{5, 2},{6, 3}};
        double[][] hilbert = LinearAlgebraExpert.hilbertMatrix(2);
        double[][] inverse = LinearAlgebraExpert.hilbertInverse(2);
        double[][] returned = LinearAlgebraExpert.matParallel_2(matrix1, hilbert);
        returned = LinearAlgebraExpert.matParallel_2(returned, inverse);
        assertMatrixEquals(matrix1, returned);
    }

    @Test
    public void matParallelRandomized_1TestWithHilbert() throws InterruptedException {
        double[][] returned = LinearAlgebraExpert.matParallel_2(bigMatrix, hilbertBigMatrix);
        returned = LinearAlgebraExpert.matParallel_2(returned, hilbertBigInvers);
        assertMatrixEquals(returned, returned);
    }

    @Test
    @Timeout(1)
    public void matSeriell_3Test(){
        double[][] matrix1 = new double[][]{{5, 2},{6, 3}};
        double[][] matrix2 = new double[][]{{10, 30},{5, 1}};
        double[][] expected = new double[][]{{60.0 , 152.0},{75.0, 183.0}};
        double[][] returned = LinearAlgebraExpert.matSeriell_3(matrix1, matrix2);
        assertMatrixEquals(expected, returned);
    }

    @Test
    @Timeout(1)
    public void matSeriell_4Test(){
        double[][] matrix1 = new double[][]{{5, 2},{6, 3}};
        double[][] matrix2 = new double[][]{{10, 30},{5, 1}};
        double[][] expected = new double[][]{{60.0 , 152.0},{75.0, 183.0}};
        double[][] returned = LinearAlgebraExpert.matSeriell_4(matrix1, matrix2);
        assertMatrixEquals(expected, returned);
    }

    public static int[][] getRandomMatrix(int row, int col) {
        Random random = new Random();

        int[][] matrix = new int[row][col];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                matrix[i][j] = random.nextInt();
            }
        }

        return matrix;
    }

    public static void assertMatrixEquals(double[][] matrix1, double[][] matrix2){
        if(matrix1.length!= matrix2.length||matrix1[0].length!=matrix2[0].length){
            throw new AssertionError();
        }
        for (int i = 0; i < matrix1.length; i++) {
            assertArrayEquals(matrix1[i], matrix2[i], delta);
        }
    }
}
