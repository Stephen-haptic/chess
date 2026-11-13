package ui;

import client.ServerFacade;
import model.AuthData;
import model.CreateGameResult;
import model.GameData;
import model.ListGamesResult;

import java.io.IOException;
import java.util.Locale;
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

            case "create":
                create();
                return true;

            case "list":
                list();
                return true;

            case "play":
                play();
                return true;

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

    private void create() {
        out.print("Please enter a Game Name: ");
        String gameName = scanner.nextLine().trim();
        try {
            CreateGameResult result = facade.createGame(gameName, authData.authToken());
            out.println("Game Created! Game ID: " + result.gameID());
        } catch (IOException e) {
            out.println("Game creation failed: " + e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            out.println("Game creation interrupted");
        }
    }

    private void list() {
        String white;
        String black;
        try {
            ListGamesResult result = facade.listGames(authData.authToken());
            if (result.games() == null || result.games().isEmpty()) {
                out.println("No games available.");
            }
            else {
                for (GameData game : result.games()) {
                    if (game.whiteUsername() != null) {
                        white = game.whiteUsername();
                    }
                    else {
                        white = "Open";
                    }
                    if (game.blackUsername() != null) {
                        black = game.blackUsername();
                    }
                    else {
                        black = "Open";
                    }
                    out.println("Game ID: " + game.gameID() + " | "
                    + "Name: " + game.gameName() + " | "
                    + "White: " + white + " | "
                    + "Black: " + black);
                }
            }
        } catch (IOException e) {
            out.println("List failed: " + e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            out.println("List interrupted");
        }
    }

    private void play() {
        boolean colorCorrect = false;
        boolean IDCorrect = false;
        String color = "";
        int gameID = -1;
        while (!IDCorrect) {
            out.print("Please enter a ID: ");
            try {
                gameID = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                out.println("Invalid game ID. Please enter a number.");
                continue;
            }
            IDCorrect = true;
        }
        while (!colorCorrect) {
            out.println("White or Black: ");
            color = scanner.nextLine().toLowerCase(Locale.ROOT);
            if (color.isEmpty()) {
                out.println("Please enter WHITE or BLACK.");
                continue;
            }
            if (color.charAt(0) == 'w') {
                color = "WHITE";
                colorCorrect = true;
            } else if (color.charAt(0) == 'b') {
                color = "BLACK";
                colorCorrect = true;
            }
        }
        try {
            facade.joinGame(color, gameID, authData.authToken());
        } catch (IOException e) {
            out.println("Play game failed: " + e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            out.println("Play game interrupted");
        }
    }
}
