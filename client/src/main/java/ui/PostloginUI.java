package ui;

import client.ServerFacade;
import model.AuthData;

import java.io.IOException;
import java.util.Scanner;

import static java.lang.System.out;

public class PostloginUI {
    private final ServerFacade facade;
    private final Scanner scanner;
    private final AuthData authData;

    public PostloginUI(ServerFacade facade, Scanner scanner, AuthData authData) {
        this.facade = facade;
        this.scanner = scanner;
        this.authData = authData;
    }

    public boolean handleCommand(String input) {
        switch (input) {
            case "help":
                help();
                return true;

            case "logout":
                return logout();

            default:
                out.println("Unknown command. Type 'help' for available commands.");
                return true;
        }
    }

    private void help() {
        out.println("help - displays available commands");
        out.println("logout - logs you out");
        out.println("create - enter <GAME NAME> to create a chess game");
        out.println("list - lists all existing games");
        out.println("play - enter <GAME ID> to join a game");
        out.println("observe -  enter <GAME ID> to watch a game in progress");
    }

    private boolean logout() {
        try {
            facade.logout(authData.authToken());
            out.println("Logged out");
            return false;
        } catch (IOException e) {
            out.println("Logout failed: " + e.getMessage());
            return true;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            out.println("Logout interrupted");
            return true;
        }
    }
}
