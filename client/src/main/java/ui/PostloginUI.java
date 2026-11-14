package ui;

import chess.ChessBoard;
import chess.ChessGame;
import client.ServerFacade;
import model.AuthData;
import model.GameData;
import model.ListGamesResult;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import static java.lang.System.out;

public class PostloginUI {
    private final ServerFacade facade;
    private final Scanner scanner;
    private final AuthData authData;
    private List<GameData> games;

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

            case "observe":
                observe();
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
        out.println("play - enter <GAME NUMBER> to join a game");
        out.println("observe -  enter <GAME NUMBER> to watch a game in progress");
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
            facade.createGame(gameName, authData.authToken());
            out.println("Game Created!");
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
            games = result.games();
            if (games == null || games.isEmpty()) {
                out.println("No games available.");
            } else {
                for (int i = 0; i < games.size(); i++) {
                    GameData game = games.get(i);
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
                    out.println((i + 1) + " | "
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
        if (games == null || games.isEmpty()) {
            out.println("Please list the games before joining one.");
            return;
        }

        boolean colorCorrect = false;
        String color = "";

        int gameNumber = getGameNumber();
        if (gameNumber == -1) {
            return;
        }

        while (!colorCorrect) {
            out.print("White or Black: ");
            color = scanner.nextLine().trim();
            if (color.equalsIgnoreCase("cancel")) {
                return;
            }

            if (color.equalsIgnoreCase("white") || color.equalsIgnoreCase("w")) {
                color = "WHITE";
                colorCorrect = true;
            } else if (color.equalsIgnoreCase("black") || color.equalsIgnoreCase("b")) {
                color = "BLACK";
                colorCorrect = true;
            } else {
                out.println("Please enter WHITE or BLACK.");
            }
        }

        int gameID = games.get(gameNumber - 1).gameID();

        try {
            facade.joinGame(color, gameID, authData.authToken());
            displayBoard(ChessGame.TeamColor.valueOf(color));
            out.println("Successfully joined game " + gameNumber + " as " + color);
        } catch (IOException e) {
            out.println("Play game failed: " + e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            out.println("Play game interrupted");
        }
    }

    private void observe() {
        if (games == null || games.isEmpty()) {
            out.println("Please list the games before observing one.");
            return;
        }

        int gameNumber = getGameNumber();
        if (gameNumber == -1) {
            return;
        }

        GameData selectedGame = games.get(gameNumber - 1);

        out.println("Observing: " + selectedGame.gameName());

        displayBoard(ChessGame.TeamColor.WHITE);
    }

    private int getGameNumber() {
        while (true) {
            out.print("Please enter a Game Number: ");

            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("cancel")) {
                return -1;
            }

            int gameNumber;

            try {
                gameNumber = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                out.println("Invalid game number. Please enter a number.");
                continue;
            }

            if (gameNumber < 1 || gameNumber > games.size()) {
                out.println("Invalid game number. Please select a game from the list.");
                continue;
            }

            return gameNumber;
        }
    }

    private void displayBoard(ChessGame.TeamColor perspective) {
        ChessBoard board = new ChessBoard();
        board.resetBoard();
        ChessBoardUI boardUI = new ChessBoardUI();
        boardUI.drawBoard(board, perspective);
    }
}
