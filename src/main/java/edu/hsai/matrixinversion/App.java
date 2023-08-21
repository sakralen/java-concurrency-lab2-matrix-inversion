package edu.hsai.matrixinversion;

import edu.hsai.matrixinversion.fileio.FileIO;

import java.util.Arrays;
import java.util.stream.Stream;

public class App {
    public static void main(String[] args) throws InterruptedException {
        double[][] matrix = FileIO.loadMatrix(args[0]);
        int threadsCount = Integer.parseInt(args[1]);
        int n = matrix.length;

        long startTime = System.currentTimeMillis();
        var inverseMatrix = MatrixInversion.inverse(matrix);
        long elapsedTime = System.currentTimeMillis() - startTime;
        System.out.printf("Elapsed time for single threaded inversion: %d ms%n", elapsedTime);

//        for (var line : inverseMatrix) {
//            for (var el : line) {
//                System.out.printf("%10.2f ", el);
//            }
//            System.out.println();
//        }

        startTime = System.currentTimeMillis();
        inverseMatrix = ConcurrentMatrixInversion.inverse(matrix, threadsCount);
        elapsedTime = System.currentTimeMillis() - startTime;
        System.out.printf("Elapsed time for concurrent inversion using %d threads: %d ms%n", threadsCount, elapsedTime);

//        for (var line : inverseMatrix) {
//            for (var el : line) {
//                System.out.printf("%10.2f ", el);
//            }
//            System.out.println();
//        }
    }
}
