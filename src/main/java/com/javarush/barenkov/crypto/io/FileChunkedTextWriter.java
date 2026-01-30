package com.javarush.barenkov.crypto.io;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileChunkedTextWriter implements ChunkedTextWriter {
    private final BufferedWriter writer;

    public FileChunkedTextWriter(String outputPath) throws IOException {
        this.writer = Files.newBufferedWriter(Path.of(outputPath));
    }

    @Override
    public void write(String chunk) throws IOException {
        writer.write(chunk);
    }

    @Override
    public void close() throws IOException {
        writer.close();
    }
}
