package ru.yandex.practicum;

import java.util.*;

public class WordleGame {

    private String answer;
    private int attempt;
    private WordleDictionary dictionary;
    private Map<String, String> historyOfWords = new LinkedHashMap<>();
    private List<String> historyHint = new ArrayList<>();

    public WordleGame(WordleDictionary dictionaryWords) {
        this.dictionary = dictionaryWords;
        this.answer = dictionaryWords.getRandomWord();
        this.attempt = 6;
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
        return attempt;
    }

    private String wordAnalysis(String word) {
        StringBuilder answerWord = new StringBuilder(answer);
        char[] arrayAnswer = answer.toCharArray();
        char[] arrayWord = word.toCharArray();
        String[] help = new String[5];
        for (int i = 0; i < 5; i++) {
            if (arrayAnswer[i] == arrayWord[i]) {
                help[i] = "+";
                answerWord.replace(i, i + 1, "*");
            }
        }
        for (int i = 0; i < 5; i++) {
            if (answerWord.indexOf(String.valueOf(arrayWord[i])) != -1 && help[i] == null) {
                help[i] = "^";
                int index = answerWord.indexOf(String.valueOf(arrayWord[i]));
                answerWord.replace(index, index + 1, "*");
            } else if (help[i] == null) {
                help[i] = "-";
            }
        }
        return String.join("", help);
    }

    private List<String> getListHints(Map<String, String> historyOfWords) {
        Set<String> dictionaryWords = new HashSet<>(dictionary.getDictionary());

        for (Map.Entry<String, String> entry : historyOfWords.entrySet()) {
            String wordInHistory = entry.getKey();
            String charsByWords = entry.getValue();

            dictionaryWords = findWordsByChar(dictionaryWords, wordInHistory, charsByWords, "\\+");
            dictionaryWords = findWordsByChar(dictionaryWords, wordInHistory, charsByWords, "\\^");
            dictionaryWords = findWordsByChar(dictionaryWords, wordInHistory, charsByWords, "-");
        }
        return new ArrayList<>(dictionaryWords);
    }

    private Set<String> findWordsByChar(Set<String> dictionaryWords, String wordInHistory,
                                        String charsByWords, String symbol) {
        Set<String> resultWords = new HashSet<>(dictionaryWords);
        Set<String> hintWords = new HashSet<>();
        String symbolsWord = charsByWords;

        while (symbolsWord.contains(symbol.replace("\\", ""))) {
            int index = symbolsWord.indexOf(symbol.replace("\\", ""));
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
            symbolsWord = symbolsWord.replaceFirst(symbol, "*");
        }
        return resultWords;
    }

    private boolean checkSymbol(String word, char letterBySymbol,
                                int index, String symbol) {
        if (symbol.equals("\\+")) return word.charAt(index) == letterBySymbol;
        if (symbol.equals("\\^")) return word.contains(String.valueOf(letterBySymbol)) &&
                word.charAt(index) != letterBySymbol;
        if (symbol.equals("-")) return !word.contains(String.valueOf(letterBySymbol));

        return false;
    }

    public String startGame(String wordUser) throws WordNotFound, WordNotFoundInDictionary {
        if (wordUser.isEmpty()) {
            if (historyHint.isEmpty()) {
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
        } else if (!dictionary.wordIsInDictionary(wordUser)) {
            throw new WordNotFoundInDictionary("Введено слово не из словаря: " + wordUser);
        }
        attempt--;

        if (wordUser.equals(answer)) return "Победа";
        if (attempt <= 0) return "Поражение";

        String result = wordAnalysis(wordUser);
        historyOfWords.put(wordUser, result);

        return result;
    }
}
