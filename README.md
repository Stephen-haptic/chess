# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)]([https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA](https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2AMQALADMABwATG4gMP7I9gAWYDoIPoYASij2SKoWckgQaJiIqKQAtAB85JQ0UABcMADaAAoA8mQAKgC6MAD0PgZQADpoAN4ARP2UaMAAtihjtWMwYwA0y7jqAO7QHAtLq8soM8BICHvLAL6YwjUwFazsXJT145NQ03PnB2MbqttQu0WyzWYyOJzOQLGVzYnG4sHuN1E9SgmWyYEoAAoMlkcpQMgBHVI5ACU12qojulVk8iUKnU9XsKDAAFUBhi3h8UKTqYplGpVJSjDpagAxJCcGCsyg8mA6SwwDmzMQ6FHAADWkoGME2SDA8QVA05MGACFVHHlKAAHmiNDzafy7gjySp6lKoDyySIVI7KjdnjAFKaUMBze11egAKKWlTYAgFT23Ur3YrmeqBJzBYbjObqYCMhbLCNQbx1A1TJXGoMh+XyNXoKFmTiYO189Q+qpelD1NA+BAIBMU+4tumqWogVXot3sgY87nae1t+7GWoKDgcTXS7QD71D+et0fj4PohQ+PUY4Cn+Kz5t7keC5er9cnvUexE7+4wp6l7FovFqXtYJ+cLtn6pavIaSpLPU+wgheertBAdZoFByyXAmlDtimGD1OEThOFmEwQZ8MDQcCyxwfECFISh+xXOgHCmF4vgBNA7CMjEIpwBG0hwAoMAADIQFkhRYcwTrUP6zRtF0vQGOo+RoFmipzGsvz-BwVygYKQH+iMykoKp+h-Ds0KPMB4lUEiMAIEJ4oYoJwkEkSYCkm+hi7jS+4MkyU76XOnl3kuwowGKEpujKcplu8SqYCqwYagAkmgVAmkg676TA0AwGpOw3gFDpJr6zpdjAPZ9tu7kWf6zLTJe0BIAAXigHBRjGcaFFphXwMgqYwOmACMBE5qoebzNBRYlvUPi1Xq9VNbsdFNsOBUWVZ4Vbm5grLfSMCHnIKDPvE56Xte20CkF9SPgGJ0bcV7Y6aWDnihkqgAZgD0gdUumEeWnyLN8FFUfW-2aV98LJj12EwLh+GjD90V-WMAOXkDyEg42DGeN4fj+F4KDoDEcSJHjBMOb4WCiYKoH1A00gRvxEbtBG3Q9HJqgKcMgOIeg6HwpUD31FzSHvWZGGrS61lCeTx3wdzaCuXdHm8iO3lgIdMuUXL-nKwVlTLiF4pPjd8iyvKQs83F6rXbLSEwNkdvJalDFnZ9nbdr2-abV11OSjN8Rzc1rUoLGCm85hkNgGmTiDXDw2jQWYwTdAU1+wHC0Y3lOuLuLJWHa+itUre-L1BwKDcMel4a6j2sLudevBdIZdMoYee3Z292i49Uuni9b0fd7YM4XhYddaJQ9OBnWPMf4KLrv42Dihq-FojAADiSoaJTVWlg0q+Myz9hKpzKNyyP-Od4LJ-C-3RVu9ZaLrzmVdaxVW1FztjJq5X5vy5ntf3sFUKRsXzaFNsaK+FtVQakOqjO2ApxQpWQM7d+50c7u3Kl7berpU5QEaoHaMwd2ojwhiUSOfVo5DX5PHcaxZk4KhwXg9O9E-77ldpZCWrd5CvyVrXeoyAciPzUBiGurCLoGwlCaBAa8lQehdgPO+gieQNE6Nwj8F9pFzGXjkXuCBAKdzYd9Q+OYFgNHGEYlACVpALH6uEYIgQVhjE2PEXUKA3Scj2KsMYyRQBqjcZBf6DjzEADl-FIwuJ0UGElwaVDHtDPCBFzGqBMWYpUljrG2PsY45xk4iJjSRg47xIBfG5I8YEpUITEZQgiZPJiOMOAAHY3BOBQE4GIEZghwC4gANngBOQwgiYBFAjlTQejRWgdAPkfP2qMszBKVJExM59YT+h-msEYcy5imWWXzDs7CSp7XRII5+SE1gbK5Nwwu+UP5MnVj-ERgV671CAdbK8oDIo-1ilAl5sD7YIKdiwwKaDSoe1UbfGo2CKJpyDiHeMnUSG9QGpQ3M+YaGTXoZC3B80GzMLkUCzhwALkyBQfUA5KAjnmNOiggBl01waJQLIql8i9n1EUW3PZHdtksqVDovR2yDFgTGOYtJtQbF2IWWLeFUMYYJNSVYkVGTFqY1qQESwZcbKbEJkgBIYBVV9ggBqgAUhAcUdKYiFLVEM0hIyok0yaMyGSPRzHHxtugLM2AEDAFVVAOAEAbJQFObK8VOyBbgJdWgNZ7rPWUB9X6gNcxLFbK-Na5lMAABWxq0BHNWdlD1XqY3QDjRY6QCt248K8jAT+tyIG-0ttA6tcCHaILSgClaYKrJlU9orLBvsMWMOhUQuFMSI5RxjtmKhKLCy0NLNNXtWLFUtuzm2jhxsCVe0uVnUcFamTktlfc3WQonmGzpRFeUQrpCfPijAJoubo2+qyvbE0ZoazhkKLipdJVWVcMwWC-0gYn1hiQv20Og7uqkKjpmWO46xqTrRY+4M5oYC1nrPOt9uyrKftXQXIlVzN1+C0IcpUGIMN7sXI8yU2B8P9JkWywcajOUwCNeKQRPKRZ8qZf6GGxCh1gbiRPZhU8cZeE9Zq7VQn5SIGDLAYA2B3WEDyAUQZW8f07zpgzJmLNjBnweJy1jSagUgG4HgBQMnkAgHk2gYRhKzoksM+6bQegDCWbkWRxu5dDCSOwwS1Dblaigu01+Xz-du2cZA7EzjzCgA))

## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the state of a game.

## Starter Code

As you create your chess application you will move through specific phases of development. This starts with implementing the moves of chess and finishes with sending game moves over the network between your client and server. You will start each phase by copying course provided [starter-code](starter-code/) for that phase into the source code of the project. Do not copy a phases' starter code before you are ready to begin work on that phase.

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`      | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

## Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```
