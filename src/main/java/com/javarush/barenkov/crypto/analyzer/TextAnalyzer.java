package com.javarush.barenkov.crypto.analyzer;

public interface TextAnalyzer {
    void analyze(String chunk);
    int getBestShift();
}
