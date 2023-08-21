import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Stream;

public class ProofOfConcept {
    public static void main(String[] args) {
        int threadCount = 20;
        int rows = 3;
        int cols = 6;

        double[][] matrix = {
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0}
        };

        long start = System.currentTimeMillis();

        for (int i = 0; i < rows * cols; i += threadCount) {
            CountDownLatch latch = new CountDownLatch(threadCount);
            for (int j = 0; j < threadCount; j++) {
                int row = (i + j) / cols;
                int col = (i + j) % cols;

                new Thread(() -> {
                    if (row >= rows) {
                        latch.countDown();
                        return;
                    }

                    matrix[row][col] += 1;

                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    latch.countDown();
                }).start();
            }

            try {
                latch.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        long elapsedTime = System.currentTimeMillis() - start;

        System.out.println(elapsedTime + " ms");

        Stream.of(matrix).forEach(line -> {
            System.out.println(Arrays.toString(line));
        });
    }
}
