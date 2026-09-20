package com.example.client.utility;

import java.util.NoSuchElementException;
import java.util.List;
import java.util.Scanner;

/**
 * Standard console implementation for the client application.
 */
public final class StandardConsole implements Console {

    private static final String PROMPT = "> ";
    private static Scanner fileScanner;
    private static final Scanner consoleScanner = new Scanner(System.in);

    @Override
    public void print(Object object) {
        System.out.print(object);
    }

    @Override
    public void println(Object object) {
        System.out.println(object);
    }

    @Override
    public String readln() throws NoSuchElementException, IllegalStateException {
        return currentScanner().nextLine();
    }

    @Override
    public boolean isCanReadln() throws IllegalStateException {
        return currentScanner().hasNextLine();
    }

    @Override
    public void printError(Object object) {
        System.err.println("Error: " + object);
    }

    @Override
    public void printTable(Object left, Object right) {
        System.out.printf(" %-35s%-1s%n", left, right);
    }

    @Override
    public void printTable(List<String> headers, List<List<String>> rows) {
        int[] widths = headers.stream().mapToInt(String::length).toArray();
        for (List<String> row : rows) {
            if (row.size() != headers.size()) {
                throw new IllegalArgumentException("Each table row must have the same number of cells as headers");
            }
            for (int index = 0; index < row.size(); index++) {
                widths[index] = Math.max(widths[index], row.get(index).length());
            }
        }

        String border = border(widths);
        println(border);
        println(row(headers, widths));
        println(border);
        for (List<String> values : rows) {
            println(row(values, widths));
        }
        println(border);
    }

    @Override
    public void prompt() {
        print(PROMPT);
    }

    @Override
    public String getPrompt() {
        return PROMPT;
    }

    @Override
    public void selectFileScanner(Scanner scanner) {
        fileScanner = scanner;
    }

    @Override
    public void selectConsoleScanner() {
        fileScanner = null;
    }

    private static Scanner currentScanner() {
        return fileScanner == null ? consoleScanner : fileScanner;
    }

    private static String border(int[] widths) {
        StringBuilder border = new StringBuilder("+");
        for (int width : widths) {
            border.append("-".repeat(width + 2)).append('+');
        }
        return border.toString();
    }

    private static String row(List<String> values, int[] widths) {
        StringBuilder row = new StringBuilder("|");
        for (int index = 0; index < values.size(); index++) {
            row.append(' ').append(String.format("%-" + widths[index] + "s", values.get(index)))
                    .append(" |");
        }
        return row.toString();
    }
}
