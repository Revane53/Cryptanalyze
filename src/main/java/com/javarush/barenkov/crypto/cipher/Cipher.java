package com.javarush.barenkov.crypto.cipher;

public interface Cipher {
    String encrypt(String plainText);
    String decrypt(String cipherText);
}
