package de.fh_muenster.blackboard.linear_algebra;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LinearAlgebraExpert {
    private final static ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    private final static ExecutorService singleThreadexecutorService = Executors.newSingleThreadExecutor();
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

    static double[][] matSeriell_1(double[][] matrix1,double[][] matrix2){
        if(matrix1.length!=matrix2.length||matrix1[0].length!=matrix2[0].length){
            throw new IllegalArgumentException("The metrics have different size in matSeriell_1.");
        }
        double[][] result = new double[matrix1.length][matrix1[0].length];
        for (int i = 0; i < matrix1.length; i++) {
            for (int j = 0; j < matrix2[0].length; j++) {
                for (int k = 0; k < matrix1[0].length; k++) {
                    result[i][j] += matrix1[i][k] * matrix2[k][j];
                }
            }
        }
        return result;
    }

    static double[][] matSeriell_2(double[][] matrix1,double[][] matrix2){
        if(matrix1.length!=matrix2.length||matrix1[0].length!=matrix2[0].length){
            throw new IllegalArgumentException("The metrics have different size in matSeriell_2.");
        }
        double[][] result = new double[matrix1.length][matrix1[0].length];
        for (int i = 0; i < matrix1.length; i++) {
            for (int j = 0; j < matrix2[0].length; j++) {
                for (int k = 0; k < matrix1[0].length; k++) {
                    result[i][j] += matrix1[i][k] * matrix2[k][j];
                }
            }
        }
        return result;
    }

    static double[][] matParallel_2(double[][] matrix1,double[][] matrix2) throws InterruptedException {
        if(matrix1.length!=matrix2.length||matrix1[0].length!=matrix2[0].length){
            throw new IllegalArgumentException("The metrics have different size in matParallel_2.");
        }
        double[][] result = new double[matrix1.length][matrix1[0].length];
        CountDownLatch countDownLatch = new CountDownLatch(matrix1.length);
        for (int i = 0; i < matrix1.length; i++) {
            int temp = i;
            boolean parallel = true;
            Runnable task = new Runnable(){
                @Override
                public void run() {
                    for (int j = 0; j < matrix2[0].length; j++) {
                        for (int k = 0; k < matrix1[0].length; k++) {
                            result[temp][j] += matrix1[temp][k] * matrix2[k][j];
                        }
                    }
                    countDownLatch.countDown();
                }
            };
            if(parallel){
                executorService.submit(task);
            }else {
                singleThreadexecutorService.submit(task);
            }
        }
        countDownLatch.await();
        return result;
    }

    static double[][] matSeriell_3(double[][] matrix1,double[][] matrix2){
        if(matrix1.length!=matrix2.length||matrix1[0].length!=matrix2[0].length){
            throw new IllegalArgumentException("The metrics have different size in matSeriell_3.");
        }
        double[][] matrix2Transpose = transpose(matrix2);
        double[][] result = new double[matrix1.length][matrix1[0].length];
        for (int i = 0; i < matrix1.length; i++) {
            for (int j = 0; j < matrix2[0].length; j++) {
                result[i][j] = vectorMultiplication(matrix1[i], matrix2Transpose[j]);
            }
        }
        return result;
    }
    static double[][] matSeriell_4(double[][] matrix1,double[][] matrix2){
        if(matrix1.length!=matrix2.length||matrix1[0].length!=matrix2[0].length){
            throw new IllegalArgumentException("The metrics have different size in matSeriell_4.");
        }
        double[][] result = new double[matrix1.length][matrix1[0].length];
        for (int i = 0; i < matrix1.length; i++) {
            for (int j = 0; j < matrix2[0].length; j++) {
                for (int k = 0; k < matrix1[0].length; k++) {
                    result[i][j] += matrix1[i][k] * matrix2[k][j];
                }
            }
        }
        return result;
    }

    private static double[][] transpose(double[][] toTranspose){
        double[][] result = new double[toTranspose[0].length][toTranspose.length];
        for (int i = 0; i <toTranspose.length; i++) {
            for (int j = 0; j < toTranspose[0].length; j++) {
                result[j][i] = toTranspose[i][j];
            }
        }
        return result;
    }
    
    public static double[][] hilbertInverse(int n){
        double[][] inverse = new double[n][n];
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                inverse[i-1][j-1] = Math.pow(-1, i+j)*factorial(n+i-1)*factorial(n+j-1)/((i+j-1)*Math.pow(factorial(i-1)*factorial(j-1), 2)*factorial(n-i)*factorial(n-j));
            }
        }
        return inverse;
    }
    
    public static double[][] hilbertMatrix(int n){
        double[][] matrix = new double[n][n];
        for (int i = 1; i <= matrix.length; i++) {
            for (int j = 1; j <= matrix[0].length; j++) {
                matrix[i-1][j-1] = (double) 1 /(i+j-1);
            }
        }
        return matrix;
    }

    private static int factorial(int n){
        int factorial = 1;
        for (int i = 2; i <= n; i++) {
            factorial*=i;
        }
        return factorial;
    }

}
