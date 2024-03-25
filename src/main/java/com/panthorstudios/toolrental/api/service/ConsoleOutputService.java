package com.panthorstudios.toolrental.api.service;

import org.springframework.stereotype.Component;

/**
 * This class is responsible for writing output to the console.
 */
@Component
public class ConsoleOutputService implements OutputService {
    public void print(String message) {
        System.out.print(message);
    }
    public void printLine() {
        System.out.println();
    }
    public void printLine(String message) {
        System.out.println(message);
    }
    public void printError(String message) {
        System.err.println(message);
    }
}
