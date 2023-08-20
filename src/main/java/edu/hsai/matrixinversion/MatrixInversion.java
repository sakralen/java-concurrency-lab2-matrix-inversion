package edu.hsai.matrixinversion;

public class MatrixInversion {
    // Method for calculating the inverse matrix using the Gauss-Jordan elimination
    public static double[][] inverse(double[][] matrix) {
        int n = matrix.length;

        // Create the augmented matrix [A | I]
        double[][] augmentedMatrix = augmentMatrix(matrix);

        // Apply elementary row operations to obtain the identity matrix on the left
        for (int i = 0; i < n; i++) {
            double pivot = augmentedMatrix[i][i];

            // Divide the current row by the value of the pivot element
            for (int j = 0; j < 2 * n; j++) {
                augmentedMatrix[i][j] /= pivot;
            }

            // Subtract the current row from the other rows
            for (int k = 0; k < n; k++) {
                if (k != i) {
                    double factor = augmentedMatrix[k][i];
                    for (int j = 0; j < 2 * n; j++) {
                        augmentedMatrix[k][j] -= factor * augmentedMatrix[i][j];
                    }
                }
            }
        }

        // Extract the right part of the augmented matrix, which represents the inverse matrix
        return extractInverseMatrix(augmentedMatrix);
    }

    public static double[][] augmentMatrix(double[][] matrix) {
        int n = matrix.length;
        double[][] augmentedMatrix = new double[n][2 * n];

        for (int i = 0; i < n; i++) {
            System.arraycopy(matrix[i], 0, augmentedMatrix[i], 0, n);
            augmentedMatrix[i][i + n] = 1.0;
        }

        return augmentedMatrix;
    }

    public static double[][] extractInverseMatrix(double[][] augmentedMatrix) {
        int n = augmentedMatrix.length;
        double[][] inverseMatrix = new double[n][n];

        for (int i = 0; i < n; i++) {
            System.arraycopy(augmentedMatrix[i], n, inverseMatrix[i], 0, n);
        }

        return inverseMatrix;
    }
}
