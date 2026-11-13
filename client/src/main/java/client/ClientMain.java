package client;

import model.AuthData;
import ui.PreloginUI;

import java.util.Scanner;

import static java.lang.System.out;

public class ClientMain {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ServerFacade facade = new ServerFacade("http://localhost:8080");
        PreloginUI preloginUI = new PreloginUI(facade, scanner);
        boolean running = true;
        AuthData authData;

        out.println("♕ Welcome to 240 Chess!");

        while (running) {
            authData = preloginUI.getAuthData();
            out.print(">>> ");
            String input = scanner.nextLine().trim().toLowerCase();

            if (authData == null) {
                running = preloginUI.handleCommand(input);
            }
            else {
                out.println("NAN");
            }
        }
    }
}
