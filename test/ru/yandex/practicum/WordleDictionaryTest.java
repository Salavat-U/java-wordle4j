package ru.yandex.practicum;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class WordleDictionaryTest {
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
    @DisplayName("При создании фильтрует список по количеству символов")
    public void testFilterDictionary() throws IOException, FileNotFound, FileReadError, DictionaryIsEmpty {
        //Given
        try (Writer fileWriter = Files.newBufferedWriter(path)) {
            fileWriter.write(WORD_ONE + "\n");
            fileWriter.write("тест" + "\n");
            fileWriter.write("молоко");
        }
        WordleDictionary wordleDictionaryTest = LOADER.loaderWordle(NAME_FILE);
        //When
        int currentSize = wordleDictionaryTest.getDictionary().size();
        //Then
        assertEquals(1, currentSize);
        assertEquals("арбуз", WORD_ONE);
    }

    @Test
    @DisplayName("При создании меняет букву ё на е")
    public void testFilterLetter() throws IOException, FileNotFound, FileReadError, DictionaryIsEmpty {
        //Given
        try (Writer fileWriter = Files.newBufferedWriter(path)) {
            fileWriter.write("актёр");
        }
        WordleDictionary wordleDictionaryTest = LOADER.loaderWordle(NAME_FILE);
        //When
        String resultFilter = wordleDictionaryTest.getRandomWord();
        //Then
        assertEquals("актер", resultFilter);
        assertNotEquals("актёр", resultFilter);
    }

    @Test
    @DisplayName("При создании меняет на нижний регистр")
    public void testFilterToLowerCase() throws IOException, FileNotFound, FileReadError, DictionaryIsEmpty {
        //Given
        try (Writer fileWriter = Files.newBufferedWriter(path)) {
            fileWriter.write(WORD_ONE.toUpperCase());
        }
        WordleDictionary wordleDictionaryTest = LOADER.loaderWordle(NAME_FILE);
        //When
        String resultFilter = wordleDictionaryTest.getRandomWord();
        //Then
        assertEquals("арбуз", resultFilter);
        assertNotEquals("АРБУЗ", resultFilter);
    }

    @Test
    @DisplayName("Метод возвращает список словаря")
    public void testGetListDictionary() {
        //Given
        List<String> listTest = new ArrayList<>(List.of(WORD_ONE, "слово"));
        WordleDictionary wordleDictionaryTest = new WordleDictionary(listTest);
        //When
        List<String> resultList = wordleDictionaryTest.getDictionary();
        // Then
        assertEquals(listTest, resultList);
    }

    @Test
    @DisplayName("Проверка слова в словаре")
    public void testCheckWordInDictionary() throws IOException, FileNotFound, FileReadError, DictionaryIsEmpty {
        //Given
        boolean checkLogic = false;
        try (Writer fileWriter = Files.newBufferedWriter(path)) {
            fileWriter.write(WORD_ONE);
        }
        WordleDictionary wordleDictionaryTest = LOADER.loaderWordle(NAME_FILE);
        //When
        if (wordleDictionaryTest.wordIsInDictionary(WORD_ONE)) {
            checkLogic = true;
        }
        //Then
        assertTrue(checkLogic);
    }
}
