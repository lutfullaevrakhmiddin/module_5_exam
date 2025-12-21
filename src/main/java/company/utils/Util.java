package company.utils;

import company.model.User;

import java.util.Scanner;

public class Util {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BRIGHT_BLUE = "\u001B[94m";
    public static final String ANSI_BRIGHT_GREEN = "\u001B[92m";
    public static final Scanner scannerString = new Scanner(System.in);
    public static final Scanner scannerInteger = new Scanner(System.in);
    public static User activeUser;

    public static String getString(String text) {
        System.out.print(text + " : ");
        return scannerString.nextLine();
    }

    public static int getInteger(String text) {
        System.out.print(text + " : ");
        return scannerInteger.nextInt();
    }
}
