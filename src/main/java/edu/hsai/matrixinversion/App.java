package edu.hsai.matrixinversion;

import edu.hsai.matrixinversion.fileio.FileIO;

public class App {
    public static void main(String[] args) {
        double[][] matrix = {
                {2, 5, 7},
                {6, 3, 4},
                {5, -2, -3}
        };
//        double[][] matrix = FileIO.loadMatrix(args[0]);
        int n = matrix.length;

        long startTime = System.currentTimeMillis();
        double[][] inverseMatrix = MatrixInversion.inverse(matrix);
        long elapsedTime = System.currentTimeMillis() - startTime;

        System.out.printf("Elapsed time: %d ms%n", elapsedTime);
//        for (int i = 0; i < n; i++) {
//            for (int j = 0; j < n; j++) {
//                System.out.printf("%10.2f ", inverseMatrix[i][j]);
//            }
//            System.out.println();
//        }
    }
}
