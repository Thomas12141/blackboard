package de.fh_muenster.blackboard.linear_algebra;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LinearAlgebraExpert {
    public static int coresNum = Runtime.getRuntime().availableProcessors();
    private final static ExecutorService executorService = Executors.newFixedThreadPool(coresNum);
    private final static ExecutorService singleThreadexecutorService = Executors.newSingleThreadExecutor();
    private static boolean parallel = true;
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
        if(!isItARealMatrix(matrix)){
            throw new IllegalArgumentException("Keine gültige Matrix!");
        }
        double[] result = new double[matrix.length];
        for (int i = 0; i <matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                result[i] += vector[j] * matrix[i][j];
            }
        }
        return result;
    }

    static double[][] metricsAddition(double[][] matrix1, double[][] matrix2){
        if(matrix1.length!=matrix2.length||matrix1[0].length!=matrix2[0].length){
            throw new IllegalArgumentException("The metrics have different size in metricsAddition.");
        }
        if(!isItARealMatrix(matrix1)){
            throw new IllegalArgumentException("Keine gültige Matrix!");
        }
        if(!isItARealMatrix(matrix2)){
            throw new IllegalArgumentException("Keine gültige Matrix!");
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
        if(matrix1[0].length!=matrix2.length){
            throw new IllegalArgumentException("The metrics cant be multiplied matSeriell_1.");
        }
        if(!isItARealMatrix(matrix1)){
            throw new IllegalArgumentException("Keine gültige Matrix!");
        }
        if(!isItARealMatrix(matrix2)){
            throw new IllegalArgumentException("Keine gültige Matrix!");
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
        if(matrix1[0].length!=matrix2.length){
            throw new IllegalArgumentException("The metrics cant be multiplied matSeriell_2.");
        }
        if(!isItARealMatrix(matrix1)){
            throw new IllegalArgumentException("Keine gültige Matrix!");
        }
        if(!isItARealMatrix(matrix2)){
            throw new IllegalArgumentException("Keine gültige Matrix!");
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
        if(matrix1[0].length!=matrix2.length){
            throw new IllegalArgumentException("The metrics cant be multiplied.");
        }
        if(!isItARealMatrix(matrix1)){
            throw new IllegalArgumentException("Keine gültige Matrix!");
        }
        if(!isItARealMatrix(matrix2)){
            throw new IllegalArgumentException("Keine gültige Matrix!");
        }
        double[][] result = new double[matrix1.length][matrix1[0].length];
        CountDownLatch countDownLatch = new CountDownLatch(matrix1.length);
        for (int i = 0; i < matrix1.length; i++) {
            int temp = i;
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
                executorService.execute(task);
            }else {
                singleThreadexecutorService.execute(task);
            }
        }
        countDownLatch.await();
        return result;
    }

    static double[][] matSeriell_3(double[][] matrix1,double[][] matrix2){
        if(matrix1.length!=matrix2.length||matrix1[0].length!=matrix2[0].length){
            throw new IllegalArgumentException("The metrics have different size in matSeriell_3.");
        }
        if(!isItARealMatrix(matrix1)){
            throw new IllegalArgumentException("Keine gültige Matrix!");
        }
        if(!isItARealMatrix(matrix2)){
            throw new IllegalArgumentException("Keine gültige Matrix!");
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
        if(!isItARealMatrix(matrix1)){
            throw new IllegalArgumentException("Keine gültige Matrix!");
        }
        if(!isItARealMatrix(matrix2)){
            throw new IllegalArgumentException("Keine gültige Matrix!");
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
    
    public static double[][] hilbertInverse(int n) throws InterruptedException {
        double[][] inverse = new double[n][n];
        CountDownLatch countDownLatch = new CountDownLatch(n);
        for (int i = 1; i <= n; i++) {
            int finalI = i;
            Runnable task = () ->{
                for (int j = 1; j <= n; j++) {
                        inverse[finalI -1][j -1] = Math.pow(-1, finalI + j)*factorial(n+ finalI -1)*factorial(n+ j -1)/((finalI + j -1)*Math.pow(factorial(finalI -1)*factorial(j -1), 2)*factorial(n- finalI)*factorial(n- j));
                    countDownLatch.countDown();

                }
            };
            executorService.execute(task);
        }
        countDownLatch.await();
        return inverse;
    }
    
    public static double[][] hilbertMatrix(int n) throws InterruptedException {
        double[][] matrix = new double[n][n];
        CountDownLatch countDownLatch = new CountDownLatch(n);
        for (int i = 1; i <= matrix.length; i++) {
            int finalI = i;
            Runnable task = new Runnable() {
                @Override
                public void run() {
                    for (int j = 1; j <= matrix[0].length; j++) {
                        matrix[finalI -1][j-1] = (double) 1 /(finalI +j-1);
                    }
                    countDownLatch.countDown();
                }
            };
            executorService.execute(task);
        }
        countDownLatch.await();
        return matrix;
    }

    private static int factorial(int n){
        int factorial = 1;
        for (int i = 2; i <= n; i++) {
            factorial*=i;
        }
        return factorial;
    }

    private static boolean isItARealMatrix(double[][] matrix){
        int size = matrix[0].length;
        for (int i = 1; i < matrix.length; i++) {
            if(size!=matrix[i].length){
                return false;
            }
        }
        return true;
    }

    public static double[][] getRandomMatrix(int row, int col) {
        Random random = new Random();

        double[][] matrix = new double[row][col];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                matrix[i][j] = random.nextInt();
            }
        }

        return matrix;
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println(" dim | baseline      |        optimized    |       parallelized  | speedup | Amdahl");
        System.out.println("-----+---------------+---------------+-----+--------------+------+---------+--------");
        System.out.println("  n  | b[us]         |        o[us]  | b/o |        p[us] | b/p  |     o/p | pi[%]");
        for (int n = 128; n<=4096; n*=2){
            double[][] hilbertMatrix = hilbertMatrix(n);
            double[][] randomizedMatrix = getRandomMatrix(n, n);

            parallel = false;
            Long timeBefore = System.nanoTime();
            matParallel_2(randomizedMatrix,hilbertMatrix);
            Long timeAfter = System.nanoTime();
            Long timeBaseine = timeAfter-timeBefore;

            parallel = false;
            timeBefore = System.nanoTime();
            matParallel_2(randomizedMatrix,hilbertMatrix);
            timeAfter = System.nanoTime();
            Long timeOptimized = timeAfter-timeBefore;

            parallel = true;
            timeBefore = System.nanoTime();
            matParallel_2(randomizedMatrix,hilbertMatrix);
            timeAfter = System.nanoTime();
            Long timeParallel = timeAfter-timeBefore;

            double speedup = (double) timeOptimized /timeParallel;
            double bDividedByO = (double) timeBaseine /timeOptimized;
            double bDividedByP = (double) timeBaseine /timeParallel;
            double amdahl = ((double) coresNum /(coresNum-1))*(speedup-1);
            String output = String.format("%4d |%15d|%15d|%5.1f|%14d|%6.1f|%9.1f|%5.1f",n, timeBaseine, timeOptimized, bDividedByO, timeParallel, bDividedByP, speedup, 100 - amdahl);
            System.out.println(output);
        }
        executorService.shutdown();
        singleThreadexecutorService.shutdown();
    }

}
