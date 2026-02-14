package ru.yandex.practicum.wordle.service;

import java.util.*;

public class WordleDictionary {

    private List<String> dictionary;
    private final Random random = new Random();
    private static final int FIVE = 5;

    public WordleDictionary(List<String> dictionary) {
        this.dictionary = wordsFilter(dictionary);
    }

    private List<String> wordsFilter(List<String> words) {
        Set<String> dictionaryWords = new HashSet<>();
        for (String word : words) {
            word = word.trim();
            if (word.length() == FIVE && !word.isBlank()) {
                word = filterWord(word);
                dictionaryWords.add(word);
            }
        }
        return new ArrayList<>(dictionaryWords);
    }

    public List<String> getDictionary() {
        return new ArrayList<>(dictionary);
    }

    public boolean wordIsInDictionary(String word) {
        if (dictionary.contains(word) && word.length() == FIVE) {
            return true;
        }
        return false;
    }

    public String filterWord(String word) {
        word = word.toLowerCase().trim();
        if (word.contains("ё")) {
            return word.replace("ё", "е");
        } else return word;
    }

    public String getRandomWord() {
        List<String> words = dictionary;
        return words.get(random.nextInt(words.size()));
    }
}
