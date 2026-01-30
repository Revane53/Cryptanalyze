package com.javarush.barenkov.crypto.io;

import java.io.IOException;

public interface ChunkedTextReader {
    void readChunks(IOConsumer<String> handler) throws IOException;
}
