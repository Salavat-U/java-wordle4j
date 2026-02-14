package ru.yandex.practicum.wordle.service;

import ru.yandex.practicum.exception.DictionaryIsEmpty;
import ru.yandex.practicum.exception.FileNotFound;
import ru.yandex.practicum.exception.FileReadError;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class WordleDictionaryLoader {

    public WordleDictionary loaderWordle(String filename) throws FileNotFound, FileReadError, DictionaryIsEmpty {
        List<String> wordleDictionary = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename, StandardCharsets.UTF_8))) {
            while (br.ready()) {
                String line = br.readLine();
                if (!line.isBlank()) {
                    wordleDictionary.add(line.trim());
                }
            }
            if (wordleDictionary.isEmpty()) {
                throw new DictionaryIsEmpty("Словарь " + filename + " пуст");
            }
        } catch (FileNotFoundException e) {
            throw new FileNotFound("Файл " + filename + " не найден");
        } catch (IOException e) {
            throw new FileReadError("Ошибка чтения файла " + filename);
        }
        return new WordleDictionary(wordleDictionary);
    }
}
