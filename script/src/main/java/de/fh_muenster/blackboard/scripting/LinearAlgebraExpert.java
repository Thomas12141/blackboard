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
}
