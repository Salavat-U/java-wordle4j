package ru.yandex.practicum.game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.exception.DictionaryIsEmpty;
import ru.yandex.practicum.exception.FileNotFound;
import ru.yandex.practicum.exception.FileReadError;
import ru.yandex.practicum.wordle.service.WordleDictionary;
import ru.yandex.practicum.wordle.service.WordleDictionaryLoader;
import ru.yandex.practicum.wordle.game.WordleGame;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WordleTest {

    private WordleDictionaryLoader LOADER;
    private WordleDictionary WORDLE_DICTIONARY;
    private WordleGame WORDLE_GAME;
    private static final String FILE_DICTIONARY = "words_ru.txt";

    @BeforeEach
    public void beforeEach() throws FileNotFound, FileReadError, DictionaryIsEmpty {
        LOADER = new WordleDictionaryLoader();
        WORDLE_DICTIONARY = LOADER.loaderWordle(FILE_DICTIONARY);
        WORDLE_GAME = new WordleGame(WORDLE_DICTIONARY);
    }

    @Test
    @DisplayName("Словарь должен загрузиться")
    public void testDictionaryShouldLoad() throws FileNotFound, FileReadError, DictionaryIsEmpty {
        //Given
        WordleDictionaryLoader loaderTest = new WordleDictionaryLoader();
        WordleDictionary wordleDictionaryTest = loaderTest.loaderWordle(FILE_DICTIONARY);
        boolean checkDictionary = false;
        //When
        if (!WORDLE_DICTIONARY.getDictionary().isEmpty() &&
                WORDLE_DICTIONARY.getDictionary().size() == wordleDictionaryTest.getDictionary().size()) {
            checkDictionary = true;
        }
        //Then
        assertTrue(checkDictionary);
    }

    @Test
    @DisplayName("При создании попыток должно быть 6")
    public void testGetSixAttempt() {
        //Given
        int attempts = 6;
        //When
        int attemptsMethod = WORDLE_GAME.getAttempt();
        //Then
        assertEquals(attempts, attemptsMethod);
    }

    @Test
    @DisplayName("Слово для отгадывание должно быть при запуске")
    public void testGetAnswerWord() {
        //Given
        String answerWord = WORDLE_GAME.getAnswer();
        boolean checkAnswerWord = false;
        //When
        if (!answerWord.isBlank()) {
            checkAnswerWord = true;
        }
        //Then
        assertTrue(checkAnswerWord);
    }
}
