package edu.hsai.matrixinversion;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.*;

public class ConcurrentMatrixInversion {
    // Method for calculating the inverse matrix using the Gauss-Jordan elimination
    public static double[][] inverse(double[][] matrix, int threadCount) {
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);

        int n = matrix.length;

        // Create the augmented matrix [A | I]
        double[][] augmentedMatrix = augmentMatrix(matrix);

        // Apply elementary row operations to obtain the identity matrix on the left
        for (int i = 0; i < n; i++) {
            double pivot = augmentedMatrix[i][i];

            // Divide the current row by the value of the pivot element
            var futureDivisions = new LinkedList<Future<?>>();

            for (int j = 0; j < 2 * n; j++) {
                final int finalI = i;
                final int finalJ = j;

                futureDivisions
                        .add(executorService.submit(() -> {
                    augmentedMatrix[finalI][finalJ] /= pivot;
                }));
            }

            futureDivisions.forEach(future -> {
                try {
                    future.get();
                } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
                }
            });

            // Subtract the current row from the other rows
            var futureSubtractions = new ArrayList<Future<?>>();

            for (int k = 0; k < n; k++) {
                if (k != i) {
                    double factor = augmentedMatrix[k][i];
                    for (int j = 0; j < 2 * n; j++) {
                        final int finalI1 = i;
                        final int finalK = k;
                        final int finalJ = j;
                        futureSubtractions
                                .add(executorService.submit(() -> {
                                    augmentedMatrix[finalK][finalJ] -= factor * augmentedMatrix[finalI1][finalJ];
                                }));
                    }
                }
            }

            futureSubtractions.forEach(future -> {
                try {
                    future.get();
                } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        executorService.shutdown();

        // Extract the right part of the augmented matrix, which represents the inverse matrix
        return extractInverseMatrix(augmentedMatrix);
    }

    private static double[][] augmentMatrix(double[][] matrix) {
        int n = matrix.length;
        double[][] augmentedMatrix = new double[n][2 * n];

        for (int i = 0; i < n; i++) {
            System.arraycopy(matrix[i], 0, augmentedMatrix[i], 0, n);
            augmentedMatrix[i][i + n] = 1.0;
        }

        return augmentedMatrix;
    }

    private static double[][] extractInverseMatrix(double[][] augmentedMatrix) {
        int n = augmentedMatrix.length;
        double[][] inverseMatrix = new double[n][n];

        for (int i = 0; i < n; i++) {
            System.arraycopy(augmentedMatrix[i], n, inverseMatrix[i], 0, n);
        }

        return inverseMatrix;
    }
}
