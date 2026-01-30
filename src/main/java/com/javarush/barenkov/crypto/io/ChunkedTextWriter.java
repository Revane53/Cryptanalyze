package com.javarush.barenkov.crypto.io;

public interface ChunkedTextWriter extends AutoCloseable{
    void write(String chunk) throws java.io.IOException;
}
