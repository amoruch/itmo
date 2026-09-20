package com.example.client.utility;

import java.util.List;
import java.util.Scanner;

/**
 * Console abstraction shared by interactive input and command scripts.
 */
public interface Console {

    void print(Object object);

    void println(Object object);

    String readln();

    boolean isCanReadln();

    void printError(Object object);

    void printTable(Object left, Object right);

    void printTable(List<String> headers, List<List<String>> rows);

    void prompt();

    String getPrompt();

    void selectFileScanner(Scanner scanner);

    void selectConsoleScanner();
}
