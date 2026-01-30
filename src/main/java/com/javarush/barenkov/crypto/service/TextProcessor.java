package com.javarush.barenkov.crypto.service;

import com.javarush.barenkov.crypto.io.ChunkedTextReader;
import com.javarush.barenkov.crypto.io.ChunkedTextWriter;
import com.javarush.barenkov.crypto.transform.DataTransformer;

import java.io.*;

public class TextProcessor {
    public static void process(ChunkedTextReader reader, ChunkedTextWriter writer, DataTransformer transformer) throws IOException {
        reader.readChunks(chunk -> writer.write(transformer.transform(chunk)));
    }
}
