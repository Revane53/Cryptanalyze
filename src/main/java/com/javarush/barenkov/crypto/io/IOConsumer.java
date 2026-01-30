package com.javarush.barenkov.crypto.io;

@FunctionalInterface
public interface IOConsumer<T> {
    void accept(T value) throws java.io.IOException;
}
