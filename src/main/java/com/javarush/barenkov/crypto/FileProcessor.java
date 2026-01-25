package com.javarush.barenkov.crypto;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileProcessor {
    public static void processFile(String inputPath, String outputPath, DataTransformer dataTransformer) throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(inputPath));
             BufferedWriter writer = Files.newBufferedWriter(Paths.get(outputPath))) {

            char[] buffer = new char[8192];
            int bytesRead;
            while ((bytesRead = reader.read(buffer)) != -1) {
                String chunk = new String(buffer, 0, bytesRead);
                String transformedChunk = dataTransformer.transform(chunk);
                writer.write(transformedChunk);
            }
        }
    }
}
