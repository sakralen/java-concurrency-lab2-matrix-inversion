package edu.hsai.matrixinversion;

import edu.hsai.matrixinversion.fileio.FileIO;

public class App {
    public static void main(String[] args) throws InterruptedException {
        double[][] matrix = FileIO.loadMatrix(args[0]);
        int threadsCount = Integer.parseInt(args[1]);
        int n = matrix.length;

        long startTime = System.currentTimeMillis();
        MatrixInversion.inverse(matrix);
        long elapsedTime = System.currentTimeMillis() - startTime;
        System.out.printf("Elapsed time for single threaded inversion: %d ms%n", elapsedTime);

        startTime = System.currentTimeMillis();
        ConcurrentMatrixInversion.inverse(matrix, threadsCount);
        elapsedTime = System.currentTimeMillis() - startTime;
        System.out.printf("Elapsed time for concurrent inversion: %d ms%n", elapsedTime);
    }
}
