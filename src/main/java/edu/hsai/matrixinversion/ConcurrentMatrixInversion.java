package edu.hsai.matrixinversion;

import java.util.concurrent.CountDownLatch;

public class ConcurrentMatrixInversion {
    // Method for calculating the inverse matrix using the Gauss-Jordan elimination
    public static double[][] inverse(double[][] matrix, int threadCount) throws InterruptedException {
        int n = matrix.length;

        // Create the augmented matrix [A | I]
        double[][] augmentedMatrix = augmentMatrix(matrix);

        // Apply elementary row operations to obtain the identity matrix on the left
        for (int i = 0; i < n; i++) {
            final double diagonalValue = augmentedMatrix[i][i];

            // Divide the current row by the value of the pivot element
            for (int j = 0; j < 2 * n; j += threadCount) {
                CountDownLatch latch = new CountDownLatch(threadCount);

                for (int threadId = 0; threadId < threadCount; threadId++) {
                    final int row = i;
                    final int col = j + threadId;
                    new Thread(() -> {
                        if (col >= 2 * n) {
                            latch.countDown();
                            return;
                        }

                        augmentedMatrix[row][col] /= diagonalValue;
                        latch.countDown();
                    }).start();
                }

                latch.await();
            }

            // Save factors for further subtraction
            final double[] factors = new double[n];
            for (int j = 0; j < n; j += threadCount) {
                CountDownLatch latch = new CountDownLatch(threadCount);

                for (int threadId = 0; threadId < threadCount; threadId++) {
                    final int index = j + threadId;
                    final int finalI = i;

                    new Thread(() -> {
                        if (index >= n) {
                            latch.countDown();
                            return;
                        }

                        factors[index] = augmentedMatrix[index][finalI];
                        latch.countDown();
                    }).start();
                }

                latch.await();
            }

            // Subtract the current row from the other rows
            for (int j = 0; j < 2 * n * n; j += threadCount) {
                CountDownLatch latch = new CountDownLatch(threadCount);

                for (int threadId = 0; threadId < threadCount; threadId++) {
                    final int row = (j + threadId) / (2 * n);
                    final int col = (j + threadId) % (2 * n);
                    final int finalI = i;

                    new Thread(() -> {
                        if (row == finalI || row >= n) {
                            latch.countDown();
                            return;
                        }

                        augmentedMatrix[row][col] -= factors[row] * augmentedMatrix[finalI][col];
                        latch.countDown();
                    }).start();
                }

                latch.await();
            }
        }

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
