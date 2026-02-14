package ru.yandex.practicum.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.exception.DictionaryIsEmpty;
import ru.yandex.practicum.exception.FileNotFound;
import ru.yandex.practicum.exception.FileReadError;
import ru.yandex.practicum.wordle.service.WordleDictionary;
import ru.yandex.practicum.wordle.service.WordleDictionaryLoader;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

public class WordleDictionaryLoaderTest {
    private WordleDictionaryLoader LOADER;
    private static final String NAME_FILE = "testException";
    private static final String WORD_ONE = "арбуз";
    private Path path;

    @BeforeEach
    public void beforeEach() {
        LOADER = new WordleDictionaryLoader();
        path = Paths.get(NAME_FILE);
    }

    @Test
    @DisplayName("Метод обрезает у слова пробелы по краям")
    public void testCorrectTrim() throws FileNotFound, IOException, FileReadError, DictionaryIsEmpty {
        //Given
        String wordTest = " " + WORD_ONE + " ";
        int sizeWordTest = wordTest.length();
        try (Writer fileWriter = Files.newBufferedWriter(path)) {
            fileWriter.write(wordTest);
        }
        WordleDictionary wordleDictionaryTest = LOADER.loaderWordle(NAME_FILE);
        //When
        int wordWithTrim = wordleDictionaryTest.getRandomWord().length();
        //Then
        assertNotEquals(sizeWordTest, wordWithTrim);
    }

    @Test
    @DisplayName("Если файл не найден выбрасывает ошибку")
    public void testExceptionFileNotFound() throws FileReadError, DictionaryIsEmpty {
        //Given
        boolean checkException = false;
        //When
        try {
            LOADER.loaderWordle("выдуманное название");
        } catch (FileNotFound exp) {
            checkException = true;
        }
        //Then
        assertTrue(checkException);
    }

    @Test
    @DisplayName("Если файл пустой выбрасывает ошибку")
    public void testExceptionDictionaryIsEmpty() throws FileNotFound, IOException, FileReadError {
        //Given
        boolean checkException = false;
        try (Writer fileWriter = Files.newBufferedWriter(path)) {
            fileWriter.write(" ");
        }
        //When
        try {
            LOADER.loaderWordle(NAME_FILE);
        } catch (DictionaryIsEmpty exp) {
            checkException = true;
        }
        //Then
        assertTrue(checkException);
    }
}
