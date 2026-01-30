package com.javarush.barenkov.crypto.cipher;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class CaesarCipher implements  Cipher {
    private final Map<Character, Integer> charToIndex = new HashMap<>();
    private final String alphabet;
    private final int shift;

    public CaesarCipher(String alphabet, int shift) {
        this.alphabet = toUniqueLowercase(alphabet);
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
        String lowerCaseText = text.toLowerCase();
        StringBuilder sb = new StringBuilder();
        int delta = encrypt ? shift : -shift;
        for (int i = 0; i < lowerCaseText.length(); i++) {
            char c = lowerCaseText.charAt(i);
            var isLower = text.charAt(i) == c;
            var index = charToIndex.get(c);

            if(index != null) {
                var tmp = alphabet.charAt(Math.floorMod(index + delta, alphabet.length()));
                sb.append(isLower ? tmp : Character.toUpperCase(tmp));
            } else {
                sb.append(c);
            }
        }
        return sb.toString() ;
    }

    private String toUniqueLowercase(String input) {
        if (input == null) return "";

        Set<Character> uniqueChars = new LinkedHashSet<>();
        for (char c : input.toLowerCase().toCharArray()) {
            uniqueChars.add(c);
        }

        StringBuilder sb = new StringBuilder();
        for (char c : uniqueChars) {
            sb.append(c);
        }
        return sb.toString();
    }
}
