package com.javarush.barenkov.crypto.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileChunkedTextReader implements ChunkedTextReader {
    private final Path filePath;

    public FileChunkedTextReader(String path) {
        this.filePath = Path.of(path);
    }
    @Override
    public void readChunks(IOConsumer<String> handler) throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            char[] buffer = new char[8192];
            int bytesRead;
            while ((bytesRead = reader.read(buffer)) != -1) {
                handler.accept(new String(buffer, 0, bytesRead));
            }
        }
    }
}
