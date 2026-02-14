package ru.yandex.practicum.wordle.service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class PrintWriter {

    public void setExceptionInLog(Exception exception) {
        try (BufferedWriter log = new BufferedWriter(
                new FileWriter("log.txt", StandardCharsets.UTF_8, true))) {
            log.write(exception.getMessage() + "\n");
            for (StackTraceElement element : exception.getStackTrace()) {
                log.write(element.toString() + "\n");
            }
            log.write("-".repeat(15) + "\n");
        } catch (IOException exp) {
            System.out.println("Не удалось записать в лог");
            exp.printStackTrace();
        }
    }

}
