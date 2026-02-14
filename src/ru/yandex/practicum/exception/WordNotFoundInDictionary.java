package ru.yandex.practicum.exception;

import java.io.IOException;

public class WordNotFoundInDictionary extends IOException {
    public WordNotFoundInDictionary(final String message) {
        super(message);
    }

}
