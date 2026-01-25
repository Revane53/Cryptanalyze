package com.javarush.barenkov.crypto.cipher;

import java.util.HashMap;
import java.util.Map;

public class CaesarCipher implements  Cipher {
    private final Map<Character, Integer> charToIndex = new HashMap<>();
    private final String alphabet;
    private final int shift;

    public CaesarCipher(String alphabet, int shift) {
        this.alphabet = alphabet.toLowerCase();
        for (int i = 0; i < alphabet.length(); i++) {
            charToIndex.put(alphabet.charAt(i), i);
        }
        this.shift = shift;
    }

    @Override
    public String encrypt(String plainText) {
        return transform(plainText, true);
    }

    @Override
    public String decrypt(String cipherText) {
        return transform(cipherText, false);
    }

    private String transform(String text, boolean encrypt) {
        StringBuilder sb = new StringBuilder();
        String lowerCase = text.toLowerCase();
        int delta = encrypt ? shift : -shift;
        for (int i = 0; i < lowerCase.length(); i++) {
            char c = lowerCase.charAt(i);
            var index = charToIndex.get(c);
            if(index != null) {
                sb.append(alphabet.charAt(Math.floorMod(index + delta, alphabet.length())));
            } else {
                sb.append(c);
            }
        }
        return sb.toString() ;
    }
}
