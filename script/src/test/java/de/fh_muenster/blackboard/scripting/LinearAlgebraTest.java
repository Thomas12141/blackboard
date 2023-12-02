package de.fh_muenster.blackboard.scripting;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
}
