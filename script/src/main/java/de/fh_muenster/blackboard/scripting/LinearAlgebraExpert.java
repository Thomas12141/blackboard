package de.fh_muenster.blackboard.scripting;

public class LinearAlgebraExpert {
    static double[] vectorAddition(double[] firstVector, double[] secondVector){
        if(firstVector.length!= secondVector.length){
            throw new IllegalArgumentException("The vectors have different length in vectorAddition.");
        }
        double[] result = new double[firstVector.length];
        for (int i = 0; i <result.length; i++) {
            result[i] = firstVector[i] + secondVector[i];
        }

        return result;
    }

    static double vectorMultiplication(double[] firstVector, double[] secondVector){
        if(firstVector.length!= secondVector.length){
            throw new IllegalArgumentException("The vectors have different length in vectorMultiplication.");
        }
        double result = 0;
        for (int i = 0; i <firstVector.length; i++) {
            result += firstVector[i] * secondVector[i];
        }
        return result;
    }

    static double[] vectorMatrixMultiplication(double[][] matrix, double[] vector){
        if (matrix.length==0||matrix[0].length==0){
            return new double[0];
        }
        if(vector.length!= matrix.length){
            throw new IllegalArgumentException("The vector column number have different length then the matrix row number in vectorMatrixMultiplication.");
        }
        double[] result = new double[matrix.length];
        for (int i = 0; i <matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                result[i] += vector[j] * matrix[i][j];
            }
        }
        return result;
    }

    static double[][] metricsAddition(double[][] matrix1,double[][] matrix2){
        if (matrix1.length==0||matrix1[0].length==0){
            return new double[0][0];
        }
        if(matrix1.length!=matrix2.length||matrix1[0].length!=matrix2[0].length){
            throw new IllegalArgumentException("The metrics have different size in metricsAddition.");
        }
        double[][] result = new double[matrix1.length][matrix1[0].length];
        for (int i = 0; i < matrix1.length; i++) {
            for (int j = 0; j < matrix1[0].length; j++) {
                result[i][j] = matrix1[i][j] + matrix2[i][j];
            }
        }
        return result;
    }
}
