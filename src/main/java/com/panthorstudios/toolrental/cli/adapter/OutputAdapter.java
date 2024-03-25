package com.panthorstudios.toolrental.cli.adapter;

public interface OutputAdapter {
    void print(String message);
    void printLine(String message);
    void printLine();
    void printError(String message);
}
