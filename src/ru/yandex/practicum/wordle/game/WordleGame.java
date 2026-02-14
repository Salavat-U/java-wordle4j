package ru.yandex.practicum.wordle.game;

import ru.yandex.practicum.exception.WordNotFound;
import ru.yandex.practicum.exception.WordNotFoundInDictionary;
import ru.yandex.practicum.wordle.service.WordleDictionary;

import java.util.*;

public class WordleGame {

    private String answer;
    private WordleDictionary dictionary;
    private Map<String, String> historyOfWords = new LinkedHashMap<>();
    private List<String> historyHint = new ArrayList<>();
    private int attempts = 6;
    private static final int WORD_LENGTH = 5;
    private static final String STAR = "*";
    private static final String PLUS = "+";
    private static final String TICK = "^";
    private static final String MINUS = "-";
    private static final String EMPTY_STRING = "";
    private static final String BACKSLASH = "\\";

    public WordleGame(WordleDictionary dictionaryWords) {
        this.dictionary = dictionaryWords;
        this.answer = dictionaryWords.getRandomWord();
    }

    public Map<String, String> getHistoryOfWords() {
        return new HashMap<>(historyOfWords);
    }

    public List<String> getHistoryHint() {
        return new ArrayList<>(historyHint);
    }

    public String getAnswer() {
        return answer;
    }

    public int getAttempt() {
        return attempts;
    }

    private String wordAnalysis(String word) {
        StringBuilder answerWord = new StringBuilder(answer);
        char[] arrayAnswer = answer.toCharArray();
        char[] arrayWord = word.toCharArray();
        String[] help = new String[WORD_LENGTH];
        for (int i = 0; i < WORD_LENGTH; i++) {
            if (arrayAnswer[i] == arrayWord[i]) {
                help[i] = PLUS;
                answerWord.replace(i, i + 1, STAR);
            }
        }
        for (int i = 0; i < WORD_LENGTH; i++) {
            if (answerWord.indexOf(String.valueOf(arrayWord[i])) != -1 && help[i] == null) {
                help[i] = TICK;
                int index = answerWord.indexOf(String.valueOf(arrayWord[i]));
                answerWord.replace(index, index + 1, STAR);
            } else if (help[i] == null) {
                help[i] = MINUS;
            }
        }
        return String.join(EMPTY_STRING, help);
    }

    private List<String> getListHints(Map<String, String> historyOfWords) {
        Set<String> dictionaryWords = new HashSet<>(dictionary.getDictionary());

        for (Map.Entry<String, String> entry : historyOfWords.entrySet()) {
            String wordInHistory = entry.getKey();
            String charsByWords = entry.getValue();

            dictionaryWords = findWordsByChar(dictionaryWords, wordInHistory, charsByWords, BACKSLASH + PLUS);
            dictionaryWords = findWordsByChar(dictionaryWords, wordInHistory, charsByWords, BACKSLASH + TICK);
            dictionaryWords = findWordsByChar(dictionaryWords, wordInHistory, charsByWords, MINUS);
        }
        return new ArrayList<>(dictionaryWords);
    }

    private Set<String> findWordsByChar(Set<String> dictionaryWords, String wordInHistory,
                                        String charsByWords, String symbol) {
        Set<String> resultWords = new HashSet<>(dictionaryWords);
        Set<String> hintWords = new HashSet<>();
        String symbolsWord = charsByWords;

        while (symbolsWord.contains(symbol.replace(BACKSLASH, EMPTY_STRING))) {
            int index = symbolsWord.indexOf(symbol.replace(BACKSLASH, EMPTY_STRING));
            char letterBySymbol = wordInHistory.charAt(index);
            for (String word : resultWords) {
                if (checkSymbol(word, letterBySymbol, index, symbol)) {
                    hintWords.add(word);
                }
            }
            if (!hintWords.isEmpty()) {
                resultWords = new HashSet<>(hintWords);
                hintWords.clear();
            }
            symbolsWord = symbolsWord.replaceFirst(symbol, STAR);
        }
        return resultWords;
    }

    private boolean checkSymbol(String word, char letterBySymbol,
                                int index, String symbol) {
        if (symbol.equals(BACKSLASH + PLUS)) return word.charAt(index) == letterBySymbol;
        if (symbol.equals(BACKSLASH + TICK)) return word.contains(String.valueOf(letterBySymbol)) &&
                word.charAt(index) != letterBySymbol;
        if (symbol.equals(MINUS)) return !word.contains(String.valueOf(letterBySymbol));

        return false;
    }

    public String startGame(String wordUser) throws WordNotFound, WordNotFoundInDictionary {
        if (wordUser.isEmpty()) {
            if (historyHint.isEmpty() && historyOfWords.isEmpty()) {
                String randomWord = dictionary.getRandomWord();
                historyHint.add(randomWord);
                return randomWord;
            } else {
                List<String> hintWords = getListHints(historyOfWords);
                Collections.shuffle(hintWords);
                for (String hint : hintWords) {
                    if (!historyHint.contains(hint)) {
                        historyHint.add(hint);
                        return hint;
                    }
                }
                throw new WordNotFound("Подсказка не найдена");
            }
        }
        wordUser = dictionary.filterWord(wordUser);

        if (!dictionary.wordIsInDictionary(wordUser)) {
            throw new WordNotFoundInDictionary("Введено слово не из словаря: " + wordUser);
        }
        attempts--;

        if (wordUser.equals(answer)) return "Победа";
        if (attempts <= 0) return "Поражение";

        String result = wordAnalysis(wordUser);
        historyOfWords.put(wordUser, result);

        return result;
    }
}
