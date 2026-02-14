package ru.yandex.practicum;

import java.util.*;

public class WordleDictionary {

    private List<String> dictionary;
    private final Random random = new Random();

    public WordleDictionary(List<String> dictionary) {
        this.dictionary = wordsFilter(dictionary);
    }

    private List<String> wordsFilter(List<String> words) {
        Set<String> dictionaryWords = new HashSet<>();
        for (String word : words) {
            if (word.length() == 5 && !word.isBlank()) {
                word = word.toLowerCase();
                if (word.contains("ё")) {
                    word = word.replace("ё", "е");
                }
                dictionaryWords.add(word);
            }
        }
        return new ArrayList<>(dictionaryWords);
    }

    public List<String> getDictionary() {
        return new ArrayList<>(dictionary);
    }

    public boolean wordIsInDictionary(String word) {
        if (dictionary.contains(word) && word.length() == 5) {
            return true;
        }
        return false;
    }

    public String getRandomWord() {
        List<String> words = dictionary;
        return words.get(random.nextInt(words.size()));
    }
}
