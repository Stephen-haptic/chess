package ui;

import client.ServerFacade;
import model.AuthData;
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


            default:
                out.println("Unknown command. Type 'help' for available commands.");
                return true;
        }
    }

    private void help() {
        out.println("help - displays available commands");
        out.println("quit - exit the program");
        out.println("login - enter <USERNAME> and <PASSWORD> to play chess");
        out.println("register - enter <USERNAME> <PASSWORD> and <EMAIL> to create an account");
    }

    private void login() {

    }
}
