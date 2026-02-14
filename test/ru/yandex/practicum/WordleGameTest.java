package ru.yandex.practicum;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

public class WordleGameTest {
    private WordleDictionaryLoader LOADER;
    private WordleDictionary WORDLE_DICTIONARY;
    private WordleGame WORDLE_GAME;
    private static final String FILE_DICTIONARY = "words_ru.txt";
    private static final String NAME_FILE = "testException";
    private static final String WORD_ONE = "арбуз";
    private static final String WORD_TWO = "алиби";
    private Path path;


    @BeforeEach
    public void beforeEach() throws FileNotFound, FileReadError, DictionaryIsEmpty {
        LOADER = new WordleDictionaryLoader();
        WORDLE_DICTIONARY = LOADER.loaderWordle(FILE_DICTIONARY);
        WORDLE_GAME = new WordleGame(WORDLE_DICTIONARY);
        path = Paths.get(NAME_FILE);
    }

    @Test
    @DisplayName("Метод возвращает актуальное количество попыток")
    public void testGetCorrectAttempt() throws WordNotFound, WordNotFoundInDictionary {
        //Given
        int attemptsStart = WORDLE_GAME.getAttempt();
        //When
        WORDLE_GAME.startGame(WORD_ONE);
        WORDLE_GAME.startGame(WORD_ONE);
        int attemptsCurrent = WORDLE_GAME.getAttempt();
        //Then
        assertEquals(6, attemptsStart);
        assertEquals(4, attemptsCurrent);
    }

    @Test
    @DisplayName("Метод возвращает актуальную историю подсказок")
    public void testGetCorrectHistoryHint() throws WordNotFound, WordNotFoundInDictionary {
        //Given
        int sizeHistoryHintStart = WORDLE_GAME.getHistoryHint().size();
        //When
        WORDLE_GAME.startGame("");
        WORDLE_GAME.startGame("");
        int sizeHistoryHintCurrent = WORDLE_GAME.getHistoryHint().size();
        //Then
        assertEquals(0, sizeHistoryHintStart);
        assertEquals(2, sizeHistoryHintCurrent);
    }

    @Test
    @DisplayName("Метод возвращает актуальную историю слов и подсказок из символов")
    public void testGetCorrectHistoryWord() throws WordNotFound, WordNotFoundInDictionary {
        //Given
        int sizeHistoryHintStart = WORDLE_GAME.getHistoryOfWords().size();
        //When
        WORDLE_GAME.startGame(WORD_ONE);
        WORDLE_GAME.startGame(WORD_TWO);
        int sizeHistoryHintCurrent = WORDLE_GAME.getHistoryOfWords().size();
        //Then
        assertEquals(0, sizeHistoryHintStart);
        assertEquals(2, sizeHistoryHintCurrent);
    }

    @Test
    @DisplayName("При отсутствии попыток возвращает - поражение")
    public void testGetLose() throws WordNotFound, WordNotFoundInDictionary {
        //Given
        String loseGetMethod = "";
        //When
        for (int i = 0; i < 6; i++) {
            loseGetMethod = WORDLE_GAME.startGame(WORD_ONE);
        }
        //Then
        assertEquals("Поражение", loseGetMethod);
    }

    @Test
    @DisplayName("При угадывании слова возвращает - победа")
    public void testGetWin() throws WordNotFound, WordNotFoundInDictionary {
        //Given
        String answerWord = WORDLE_GAME.getAnswer();
        //When
        String winGetMethod = WORDLE_GAME.startGame(answerWord);
        //Then
        assertEquals("Победа", winGetMethod);
    }

    @Test
    @DisplayName("Подсказка не повторяется")
    public void testHintNotRepeats() throws IOException, WordNotFound,
            FileNotFound, FileReadError, DictionaryIsEmpty {
        //Given
        try (Writer fileWriter = Files.newBufferedWriter(path)) {
            fileWriter.write(WORD_ONE + "\n");
            fileWriter.write(WORD_TWO + "\n");
            fileWriter.write("вишня");
        }
        WordleDictionary wordleDictionaryTest = LOADER.loaderWordle(NAME_FILE);
        WordleGame wordleGameTest = new WordleGame(wordleDictionaryTest);
        //When
        wordleGameTest.startGame("");
        String firstHint = wordleGameTest.startGame("");
        String secondHint = wordleGameTest.startGame("");
        //Then
        assertNotEquals(firstHint, secondHint);
    }

    @Test
    @DisplayName("Выдает правильную подсказку с символом +")
    public void testGetCorrectSymbolPlus() throws WordNotFound, IOException,
            FileNotFound, FileReadError, DictionaryIsEmpty {
        //Given
        String result = "";
        try (Writer fileWriter = Files.newBufferedWriter(path)) {
            fileWriter.write(WORD_ONE + "\n");
            fileWriter.write(WORD_TWO);
        }
        WordleDictionary wordleDictionaryTest = LOADER.loaderWordle(NAME_FILE);
        WordleGame wordleGameTest = new WordleGame(wordleDictionaryTest);
        String answerWord = wordleGameTest.getAnswer();
        //When
        if (!answerWord.equals(WORD_ONE)) {
            result = wordleGameTest.startGame(WORD_ONE);
        } else {
            result = wordleGameTest.startGame(WORD_TWO);
        }
        //Then
        assertEquals('+', result.charAt(0));
    }

    @Test
    @DisplayName("Выдает правильную подсказку с символом ^")
    public void testGetCorrectSymbolTick() throws WordNotFound, IOException,
            FileNotFound, FileReadError, DictionaryIsEmpty {
        //Given
        String result = "";
        try (Writer fileWriter = Files.newBufferedWriter(path)) {
            fileWriter.write(WORD_ONE + "\n");
            fileWriter.write("речка");
        }
        WordleDictionary wordleDictionaryTest = LOADER.loaderWordle(NAME_FILE);
        WordleGame wordleGameTest = new WordleGame(wordleDictionaryTest);
        String answerWord = wordleGameTest.getAnswer();
        //When
        if (!answerWord.equals(WORD_ONE)) {
            result = wordleGameTest.startGame(WORD_ONE);
        } else {
            result = wordleGameTest.startGame("речка");
        }
        //Then
        assertEquals('^', result.charAt(0));
    }

    @Test
    @DisplayName("Выдает правильную подсказку с символом -")
    public void testGetCorrectSymbolMinus() throws WordNotFound, IOException,
            FileNotFound, FileReadError, DictionaryIsEmpty {
        //Given
        String result = "";
        try (Writer fileWriter = Files.newBufferedWriter(path)) {
            fileWriter.write(WORD_ONE + "\n");
            fileWriter.write("слово");
        }
        WordleDictionary wordleDictionaryTest = LOADER.loaderWordle(NAME_FILE);
        WordleGame wordleGameTest = new WordleGame(wordleDictionaryTest);
        String answerWord = wordleGameTest.getAnswer();
        //When
        if (!answerWord.equals(WORD_ONE)) {
            result = wordleGameTest.startGame(WORD_ONE);
        } else {
            result = wordleGameTest.startGame("слово");
        }
        //Then
        assertEquals('-', result.charAt(0));
    }

    @Test
    @DisplayName("Правильная обработка ошибки - слово отсутствует в словаре")
    public void testGetCorrectExceptionWordNotFoundInDictionary() throws WordNotFound {
        //Given
        boolean checkWordNotFound = false;
        //When
        try {
            WORDLE_GAME.startGame("Тест");
        } catch (WordNotFoundInDictionary e) {
            checkWordNotFound = true;
        }
        //Then
        assertTrue(checkWordNotFound);
    }

    @Test
    @DisplayName("Правильная обработка ошибки - подсказка не найдена")
    public void testGetCorrectExceptionWordNotFound() throws IOException,
            FileNotFound, FileReadError, DictionaryIsEmpty {
        //Given
        try (Writer fileWriter = Files.newBufferedWriter(path)) {
            fileWriter.write(WORD_ONE);
        }
        boolean checkWordNotFound = false;
        WordleDictionary wordleDictionaryTest = LOADER.loaderWordle(NAME_FILE);
        WordleGame wordleGameTest = new WordleGame(wordleDictionaryTest);
        //When
        try {
            wordleGameTest.startGame("");
            wordleGameTest.startGame("");
        } catch (WordNotFound e) {
            checkWordNotFound = true;
        }
        //Then
        assertTrue(checkWordNotFound);
    }
}
