package client;

import model.AuthData;
import ui.PostloginUI;
import ui.PreloginUI;

import java.util.Scanner;

import static java.lang.System.out;

public class ClientMain {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ServerFacade facade = new ServerFacade("http://localhost:8080");
        PreloginUI preloginUI = new PreloginUI(facade, scanner);
        PostloginUI postloginUI = null;
        boolean running = true;
        AuthData authData;

        out.println("♕ Welcome to 240 Chess!");

        while (running) {
            out.print(">>> ");
            String input = scanner.nextLine().trim().toLowerCase();
            authData = preloginUI.getAuthData();

            if (authData != null && postloginUI == null) {
                postloginUI = new PostloginUI(facade, scanner, authData);
            }

            if (authData == null) {
                running = preloginUI.handleCommand(input);
            }
            else {
                if (!postloginUI.handleCommand(input)) {
                    preloginUI.clearAuthData();
                    postloginUI = null;
                }
            }
        }
    }
}
