package ru.yandex.practicum.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.exception.FileNotFound;
import ru.yandex.practicum.exception.FileReadError;
import ru.yandex.practicum.wordle.service.PrintWriter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class PrintWriterTest {
    private static final String FILE_LOG = "log.txt";
    private static final String FILE_NOT_FOUND = "Файл не найден";
    private static final String FILE_READ_ERROR = "Ошибка чтения файла";

    @Test
    @DisplayName("Лог записывает ошибки")
    public void testGetLogInfo() throws IOException {
        //Given
        PrintWriter printWriter = new PrintWriter();
        Path fileLog = Paths.get(FILE_LOG);
        if (Files.exists(fileLog) && Files.size(fileLog) > 0) {
            Files.write(fileLog, new byte[0]);
        }

        FileNotFound fileNotFound = new FileNotFound(FILE_NOT_FOUND);
        FileReadError fileReadError = new FileReadError(FILE_READ_ERROR);
        boolean checkFileNotFound = false;
        boolean checkFileReadError = false;
        //When
        printWriter.setExceptionInLog(fileNotFound);
        printWriter.setExceptionInLog(fileReadError);
        BufferedReader bufferedReader = new BufferedReader(new FileReader(fileLog.toFile()));
        while (bufferedReader.ready()) {
            String line = bufferedReader.readLine();
            if (line.contains(FILE_NOT_FOUND)){
                checkFileNotFound = true;
            } else if (line.contains(FILE_READ_ERROR)){
                checkFileReadError = true;
            }
        }
        //Then
        assertTrue(checkFileNotFound);
        assertTrue(checkFileReadError);
    }
}
