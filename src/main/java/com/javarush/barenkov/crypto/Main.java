package com.javarush.barenkov.crypto;

import com.javarush.barenkov.crypto.cipher.CaesarCipher;
import com.javarush.barenkov.crypto.cipher.Cipher;

import java.io.IOException;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        Cipher cipher = new CaesarCipher("absdifghigklmnop", 3);
        try {
            FileProcessor.processFile("eqe", "qwe", cipher::decrypt);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}