package client;

import chess.*;
import ui.PreloginUI;

import java.util.Scanner;

public class ClientMain {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ServerFacade facade = new ServerFacade("http://localhost:8080");
        PreloginUI preloginUI = new PreloginUI(facade, scanner);
        boolean running = true;

        System.out.println("♕ Welcome to 240 Chess!");

        while (running) {
            System.out.print(">>> ");

            String input = scanner.nextLine().trim().toLowerCase();

            running = preloginUI.handleCommand(input);
        }
    }
}
