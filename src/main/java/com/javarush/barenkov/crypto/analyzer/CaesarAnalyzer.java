package com.javarush.barenkov.crypto.analyzer;

import com.javarush.barenkov.crypto.cipher.CaesarCipher;

import java.text.BreakIterator;
import java.util.Objects;
import java.util.Set;

public class CaesarAnalyzer implements TextAnalyzer {
    private final String alphabet;
    private final Set<String> dictionary;
    private final int[] scores;
    private final BreakIterator bi = BreakIterator.getWordInstance();

    public CaesarAnalyzer(String alphabet, Set<String> dictionary) {
        this.alphabet = Objects.requireNonNull(alphabet, "Alphabet must not be null");
        this.dictionary = Objects.requireNonNull(dictionary, "Dictionary must not be null");
        this.scores = new int[alphabet.length()];
    }

    @Override
    public void analyze(String chunk) {
        if (chunk == null || chunk.isEmpty()) return;
        for (int shift = 0; shift < alphabet.length(); shift++) {
            CaesarCipher cipher = new CaesarCipher(alphabet, shift);
            String decrypted = cipher.decrypt(chunk);
            scores[shift] = scoreText(decrypted);
        }
    }

    private int scoreText(String text) {
        if (text == null) return 0;
        String lowerCase = text.toLowerCase();
        bi.setText(lowerCase);

        int count = 0;
        for (int start = bi.first(), end = bi.next(); end != BreakIterator.DONE; start = end, end = bi.next()) {
            String word = lowerCase.substring(start, end);
            if (dictionary.contains(word)) {
                count++;
            }
        }
        return count;
    }

    @Override
    public int getBestShift() {
        int max = Integer.MIN_VALUE;
        int bestShift = 0;
        for (int shift = 0; shift < alphabet.length(); shift++) {
            if (scores[shift] > max) {
                max = scores[shift];
                bestShift = shift;
            }
        }
        return bestShift;
    }
}
