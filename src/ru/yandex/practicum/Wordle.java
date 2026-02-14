package ru.yandex.practicum;

import java.util.Scanner;

public class Wordle {
    private static Scanner scanner;

    public static void main(String[] args) {
        PrintWriter printWriter = new PrintWriter();
        try {
            scanner = new Scanner(System.in);
            WordleDictionaryLoader loader = new WordleDictionaryLoader();
            WordleDictionary wordleDictionary = loader.loaderWordle("words_ru.txt");
            WordleGame wordleGame = new WordleGame(wordleDictionary);

            while (true) {
                System.out.println("Введите слово или пустую строку для подсказки:");
                String word = scanner.nextLine().toLowerCase().trim();
                try {
                    if (word.isEmpty()) {
                        String hint = wordleGame.startGame(word);
                        System.out.println("Подсказка: " + hint + "\n");
                        continue;
                    }
                    String resultGame = wordleGame.startGame(word);

                    if (resultGame.equals("Победа")) {
                        System.out.println("\n" + resultGame);
                        break;
                    } else if (resultGame.equals("Поражение")) {
                        System.out.println(resultGame + ". Было загадано слово: " + wordleGame.getAnswer());
                        break;
                    } else {
                        System.out.println("Результат: " + resultGame);
                        System.out.println("Осталось ходов: " + wordleGame.getAttempt() + "\n" + "-".repeat(20));
                    }
                } catch (WordNotFoundInDictionary e) {
                    System.out.println("Такого слова нет в словаре, попробуйте еще раз");
                    printWriter.setExceptionInLog(e);
                } catch (WordNotFound e) {
                    System.out.println("Подсказка не найдена");
                    printWriter.setExceptionInLog(e);
                }
            }
        } catch (FileNotFound | FileReadError | DictionaryIsEmpty e) {
            printWriter.setExceptionInLog(e);
        } finally {
            scanner.close();
        }
    }
}
