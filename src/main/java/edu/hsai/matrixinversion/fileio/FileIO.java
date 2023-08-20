package edu.hsai.matrixinversion.fileio;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Stream;

public class FileIO {
    public static double[][] loadMatrix(String path) {
        List<String> lines;
        try {
            lines = Files.readAllLines(Path.of(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return lines.stream()
                        .map(line -> Stream.of(line.split("\\s+"))
                        .mapToDouble(Double::parseDouble).toArray())
                        .toArray(double[][]::new);
    }
}
