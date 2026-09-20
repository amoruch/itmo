package com.example.server.utility;

import java.util.Scanner;

/** Console abstraction used by the server managers and server console. */
public interface Console {

    void print(Object object);

    void println(Object object);

    String readln();

    boolean isCanReadln();

    void printError(Object object);

    void printTable(Object left, Object right);

    void prompt();

    String getPrompt();

    void selectFileScanner(Scanner scanner);

    void selectConsoleScanner();
}
