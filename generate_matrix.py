import numpy as np
import sys
import os

def generate_and_save_matrix(matrix_size, output_dir="."):
    random_matrix = np.random.rand(matrix_size, matrix_size) * 100

    # determinant = np.linalg.det(random_matrix)

    # if determinant != 0:
    filename = f"random_matrix_{matrix_size}x{matrix_size}.txt"
    filepath = os.path.join(output_dir, filename)

    with open(filepath, 'w') as file:
        np.savetxt(file, random_matrix, fmt='%.2f', delimiter='\t')
    print(f"Matrix is saved to file '{filepath}'")
    # else:
    #     print("Determinant is zero, matrix is not saved.")

if __name__ == "__main__":
    if len(sys.argv) != 3:
        print("Usage: python generate_matrix.py matrix_size output_dir")
    else:
        try:
            matrix_size = int(sys.argv[1])
            output_dir = sys.argv[2]
            if not os.path.exists(output_dir):
                os.makedirs(output_dir)
            generate_and_save_matrix(matrix_size, output_dir)
        except ValueError:
            print("Matrix size must be an integer.")
