package ui;

import client.ServerFacade;
import model.AuthData;

import java.io.IOException;
import java.util.Scanner;

import static java.lang.System.out;

public class PreloginUI {
    private final ServerFacade facade;
    private final Scanner scanner;
    private AuthData authData;

    public PreloginUI(ServerFacade facade, Scanner scanner) {
        this.facade = facade;
        this.scanner = scanner;
        this.authData = null;
    }

    public boolean handleCommand(String input) {
        switch (input) {
            case "help":
                help();
                return true;

            case "quit":
                out.println("Goodbye!");
                return false;

            case "login":
                login();
                return true;

            case "register":
                register();
                return true;

            default:
                out.println("Unknown command. Type 'help' for available commands.");
                return true;
        }
    }

    public AuthData getAuthData() {
        return authData;
    }

    private void help() {
        out.println("help - displays available commands");
        out.println("quit - exit the program");
        out.println("login - enter <USERNAME> and <PASSWORD> to play chess");
        out.println("register - enter <USERNAME> <PASSWORD> and <EMAIL> to create an account");
    }

    private void login() {
        out.print("Please enter a Username: ");
        String username = scanner.nextLine().trim();
        out.print("Please enter a Password: ");
        String password = scanner.nextLine();
        try {
            authData = facade.login(username, password);
            out.println("Logged in as: " + authData.username());
        } catch (IOException e) {
            out.println("Login failed: " + e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            out.println("Login interrupted");
        }
    }

    private void register() {
        out.print("Please enter a Username: ");
        String username = scanner.nextLine().trim();
        out.print("Please enter a Password: ");
        String password = scanner.nextLine();
        out.print("Please enter an Email: ");
        String email = scanner.nextLine().trim();
        try {
            authData = facade.register(username, password, email);
            out.println("Logged in as: " + authData.username());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            out.println("Register interrupted");
        }
    }
}
