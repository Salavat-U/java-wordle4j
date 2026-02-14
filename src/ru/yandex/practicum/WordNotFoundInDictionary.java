package ru.yandex.practicum;

import java.io.IOException;

public class WordNotFoundInDictionary extends IOException {
    public WordNotFoundInDictionary(final String message) {
        super(message);
    }

}
