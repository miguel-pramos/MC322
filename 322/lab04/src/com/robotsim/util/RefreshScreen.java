package com.robotsim.util;


public class RefreshScreen {

    public static void clearScreen() throws Exception {
        if (System.getProperty("os.name").contains("win")) {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } else {
            new ProcessBuilder("clear").inheritIO().start().waitFor();
        }
    }
}