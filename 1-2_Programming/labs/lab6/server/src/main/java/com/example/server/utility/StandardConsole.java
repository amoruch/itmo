package com.example.server.utility;

import java.util.NoSuchElementException;
import java.util.Scanner;

/** Standard output implementation of the server console. */
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
}
