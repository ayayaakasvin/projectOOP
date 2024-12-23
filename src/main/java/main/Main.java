package main;

import Utils.UserDAO;
// import core.model.User; // Removed unused import

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Scanner;

public class Main {
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/uni_system";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "1488";

    public static void main(String[] args) {
        new Main().run();
    }

    private void run() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }

        Scanner scanner = new Scanner(System.in);
        ResourceBundle messages = selectLanguage(scanner);

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            UserDAO userDAO = new UserDAO(connection, messages);

            while (true) {
                System.out.println("\n=================================");
                System.out.println(messages.getString("welcome"));
                System.out.println("1. " + messages.getString("log_in"));
                System.out.println("2. " + messages.getString("exit"));
                System.out.println("=================================");

                System.out.print(messages.getString("select_action"));
                if (scanner.hasNextInt()) {
                    int choice = scanner.nextInt();
                    scanner.nextLine();

                    switch (choice) {
                        case 1:
                            logIn(scanner, messages, userDAO);
                            break;

                        case 2:
                            System.out.println(messages.getString("exit_message"));
                            scanner.close();
                            return;

                        default:
                            System.out.println(messages.getString("invalid_choice"));
                    }
                } else {
                    System.out.println(messages.getString("invalid_choice"));
                    scanner.next(); // consume the invalid input
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static ResourceBundle selectLanguage(Scanner scanner) {
        System.out.println("Select language: 1. English 2. Russian 3. Kazakh");
        int choice = scanner.nextInt();
        scanner.nextLine();
        Locale locale;
        switch (choice) {
            case 2:
                locale = new Locale.Builder().setLanguage("ru").setRegion("RU").build();
                break;
            case 3:
                locale = new Locale.Builder().setLanguage("kk").setRegion("KZ").build();
                break;
            default:
                locale = new Locale.Builder().setLanguage("en").setRegion("US").build();
                break;
        }
        return ResourceBundle.getBundle("messages", locale);
    }

    private void logIn(Scanner scanner, ResourceBundle messages, UserDAO userDAO) {
        System.out.print(messages.getString("enter_email"));
        String email = scanner.next();
        System.out.print(messages.getString("enter_password"));
        String password = scanner.next();

        userDAO.loginUser(email, password);
    }
}