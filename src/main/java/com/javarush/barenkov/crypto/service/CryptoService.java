package com.javarush.barenkov.crypto.service;

import com.javarush.barenkov.crypto.analyzer.CaesarAnalyzer;
import com.javarush.barenkov.crypto.analyzer.TextAnalyzer;
import com.javarush.barenkov.crypto.cipher.CaesarCipher;
import com.javarush.barenkov.crypto.cipher.Cipher;
import com.javarush.barenkov.crypto.io.FileChunkedTextReader;
import com.javarush.barenkov.crypto.io.FileChunkedTextWriter;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

public class CryptoService {
    private static final String DEFAULT_ALPHABET = "абвгдеёжзийклмнопрстуфхцчшщъыьэюя .,!:\"-?";
    private static final Set<String> DEFAULT_DICTIONARIES = Set.of(
            "я", "ты", "он", "она", "оно", "мы", "вы", "они",
            "меня", "тебя", "его", "её", "нас", "вас", "их",
            "мне", "тебе", "ему", "ей", "нам", "вам", "им",
            "мной", "тобой", "ним", "ней", "нами", "вами", "ими",
            "мой", "твой", "наш", "ваш",
            "себя", "себе", "собой",
            "быть", "есть", "был", "была", "было", "были",
            "иметь", "делать", "сказать", "думать", "знать", "хотеть",
            "мочь", "должен", "идти", "видеть", "работать", "жить",
            "стать", "дать", "пойти", "прийти", "говорить", "писать",
            "не", "ни", "же", "ли", "бы", "только", "уже", "ещё",
            "очень", "так", "где", "когда", "почему", "зачем",
            "тут", "там", "здесь", "всегда", "иногда", "сейчас",
            "и", "а", "но", "или", "что", "чтобы", "если", "как",
            "в", "на", "с", "у", "к", "от", "из", "до", "по", "за", "над",
            "под", "о", "об", "про", "без", "для", "через", "между",
            "время", "человек", "год", "день", "рука", "голова", "место",
            "слово", "дело", "жизнь", "работа", "дом", "страна", "мир",
            "вода", "лицо", "конец", "город", "глаз", "система", "часть",
            "привет", "здравствуйте", "пока", "спасибо", "да", "нет", "ладно"
    );

    private String alphabet = DEFAULT_ALPHABET;
    private Set<String> dictionary = DEFAULT_DICTIONARIES;

    public String getAlphabet() {
        return alphabet;
    }

    public void setAlphabet(String alphabet) {
        this.alphabet = alphabet.toLowerCase();
    }

    public Set<String> getDictionary() {
        return dictionary;
    }

    public void setDictionary(Set<String> dictionary) {
        this.dictionary = dictionary.stream()
                .map(String::toLowerCase)
                .collect(Collectors.toSet());
    }

    public void processFile(String inputPath, String outputPath, Mode mode, int shift) throws IOException {
        switch (mode) {
            case CAESAR_DECRYPTOR -> ceaserDecoder(inputPath, outputPath, shift, alphabet);
            case CAESAR_ENCRYPTOR -> ceaserEncoder(inputPath, outputPath, shift, alphabet);
            case CAESAR_BRUTEFORCE -> {
                int bestShift = searchBestCeaserShift(inputPath, alphabet, dictionary);
                ceaserDecoder(inputPath, outputPath, bestShift, alphabet);
            }
        }
    }

    private void ceaserDecoder(String inputPath, String outputPath, int shift, String alphabet) throws IOException {
        CaesarCipher cipher = new CaesarCipher(alphabet, shift);
        try (var writer = new FileChunkedTextWriter(outputPath)) {
            var reader = new FileChunkedTextReader(inputPath);
            TextProcessor.process(reader, writer, cipher::decrypt);
        }
    }

    private void ceaserEncoder(String inputPath, String outputPath, int shift, String alphabet) throws IOException {
        Cipher cipher = new CaesarCipher(alphabet, shift);
        try (var writer = new FileChunkedTextWriter(outputPath)) {
            var reader = new FileChunkedTextReader(inputPath);
            TextProcessor.process(reader, writer, cipher::encrypt);
        }
    }

    private int searchBestCeaserShift(String inputPath, String alphabet, Set<String> dictionary) throws IOException {
        TextAnalyzer textAnalyzer = new CaesarAnalyzer(alphabet, dictionary);
        new FileChunkedTextReader(inputPath).readChunks(textAnalyzer::analyze);
        return textAnalyzer.getBestShift();
    }
}
