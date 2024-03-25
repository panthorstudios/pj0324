package com.panthorstudios.toolrental.cli.adapter;

import org.springframework.stereotype.Component;

import java.util.Scanner;
/**
 * This class is responsible for reading input from the console.
 */
@Component
public class ConsoleInputAdapter implements InputAdapter {
    private final Scanner scanner = new Scanner(System.in);
    public String readLine() {
        return scanner.nextLine();
    }
}
