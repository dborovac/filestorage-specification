package utils;

import java.util.Scanner;

public class UserInput {
    public static String inputUsernameAndPassword() {
        Scanner input = new Scanner(System.in);
        System.out.println("Enter username: ");
        String username = input.nextLine();
        System.out.println("Enter password: ");
        String password = input.nextLine();
        return username + ":" + password;
    }
}
