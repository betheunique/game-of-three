package com.takeaway.game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import javax.websocket.CloseReason;
import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import com.takeaway.game.client.AutomaticPlayerEndPoint;
import com.takeaway.game.client.UserInputEndPoint;
import com.takeaway.game.constants.GameConstants;
import com.takeaway.game.server.GameServerEndPoint;
import org.glassfish.tyrus.client.ClientManager;
import org.glassfish.tyrus.server.Server;

/**
 * <p>Main class of the Game, provide multiple options to choose for the player.
 *
 * @author abhishekrai
 * @since 0.1.0
 */
public class Game {
    private static final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) {

        System.out.println("  ___   __   _  _  ____     __  ____    ____ \n" +
                                   " / __) / _\\ ( \\/ )(  __)   /  \\(  __)  ( __ \\\n" +
                                   "( (_ \\/    \\/ \\/ \\ ) _)   (  O )) _)    (__ (\n" +
                                   " \\___/\\_/\\_/\\_)(_/(____)   \\__/(__)    (____/");

        int choice = 0;
        do {
            try {
                System.out.println();

                System.out.println("#########--MENU--#########");
                System.out.println("-------------------------\n");
                System.out.println("1 - Initiate the Game to play with other player automatically.");
                System.out.println("2 - Initiate the Game to play as User.");
                System.out.println("3 - Start the Game and wait for other player to initiate.");
                System.out.println("4 - Start and initiate the Game, " +
                                           "if player is not available wait for other player to initiate.");
                System.out.println("5 - Quit.");
                System.out.println("Please enter your selection");
                choice = Integer.parseInt(reader.readLine());

                switch (choice) {
                    case 1 : connectWithPlayer();
                    break;

                    case 2 : connectWithPlayerAsUser();
                    break;

                    case 3 : runServer();
                    break;

                    case 4 : startAndConnectWithPlayer();
                    break;

                    case 5 : reader.close();
                    break;

                    default: System.out.println("Wrong selection");
                    break;
                }
            } catch (Exception io) {
                System.out.println("IOException occurred :" + io);
            }
        } while (choice != 5);

    }

    /**
     * <p>This method start the game as receiver for the input,
     * other player can initiate the game by sending random whole number.
     */
    private static void runServer() {
        try {
            System.out.println("Please type port number to play :");
            int port = Integer.parseInt(reader.readLine());
            Server server = new Server("localhost", port, "/game", null,
                                       GameServerEndPoint.class);
            try {
                server.start();
                System.out.println(GameConstants.STOP_SERVER_MESSAGE);
                reader.readLine();
            } catch (DeploymentException | IOException e) {
                throw new RuntimeException(e);
            } finally {
                server.stop();
            }
        } catch (NumberFormatException | IOException io) {
            System.out.println(GameConstants.PORT_NUMBER_NOT_VALID);
        }
    }

    /**
     * <p>This method allow to connect with other player but
     * don't have functionality to receive connect request.
     */
    private static void connectWithPlayer() {
        try {
            System.out.println("please type other player port number to connect");
            String otherPlayerPort = reader.readLine();
            String uri = "ws://localhost:" + otherPlayerPort + "/game/of3";
            ClientManager manager = ClientManager.createClient();
//            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
//            Session session = container.connectToServer(ClientEndPoint.class,  URI.create(uri));
            Session session = manager.connectToServer(AutomaticPlayerEndPoint.class, URI.create(uri));
            System.out.println(GameConstants.STOP_SERVER_MESSAGE);
            reader.readLine();
            session.close(new CloseReason(CloseReason.CloseCodes.NORMAL_CLOSURE, "game end"));
        } catch (NumberFormatException | DeploymentException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * <p>This method allow User to connect as other player,
     * user can type random whole number {@code 0, 1, 2 ... } non negative number.
     */
    private static void connectWithPlayerAsUser() {
        try {
            System.out.println("please type other player port number to connect");
            String otherPlayerPort = reader.readLine();
            String uri = "ws://localhost:" + otherPlayerPort + "/game/of3";

            ClientManager manager =  ClientManager.createClient();
//            WebSocketContainer cc = ContainerProvider.getWebSocketContainer();
//            Session session = cc.connectToServer(UserInputEndPoint.class, URI.create(uri));
            Session session = manager.connectToServer(UserInputEndPoint.class, URI.create(uri));
            System.out.println("Type random whole number non negative and non decimal to start the game ");
            session.getBasicRemote().sendText(reader.readLine());
            String input;
            do {
                input = reader.readLine();
                if(input.equalsIgnoreCase("quit")) {
                    session.close(new CloseReason(CloseReason.CloseCodes.NORMAL_CLOSURE, "game end"));
                } else if(Integer.parseInt(input) == 1) {
                    session.getBasicRemote().sendText("WIN!!");
                } else {
                    session.getBasicRemote().sendText(input);
                }
            } while (!input.equalsIgnoreCase("quit"));
        } catch (NumberFormatException | DeploymentException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * <p>This method allow both receiver and sender of whole number,
     * if other player is not ready to start this will provide player to wait.
     */
    private static void startAndConnectWithPlayer() {
        try {
            System.out.println("Please type port number to start :");
            int port = Integer.parseInt(reader.readLine());
            Server server = new Server("localhost", port, "/game", null,
                                       GameServerEndPoint.class);
            try {
                server.start();
                System.out.println(GameConstants.OTHER_PLAYER_PORT_NUMBER_MESSAGE);
                String otherPlayerPort = reader.readLine();
                String uri = "ws://localhost:" + otherPlayerPort + "/game/of3";
//                ClientManager manager = ClientManager.createClient();
//                Session session = manager.connectToServer(ClientEndPoint.class,  URI.create(uri));
                WebSocketContainer container = ContainerProvider.getWebSocketContainer();
                Session session = container.connectToServer(AutomaticPlayerEndPoint.class, URI.create(uri));
                System.out.println(GameConstants.STOP_SERVER_MESSAGE);
                reader.readLine();
            } catch (DeploymentException e) {
//                throw new RuntimeException(e);
                reader.readLine();
            } finally {
                server.stop();
            }
        } catch (NumberFormatException | IOException e) {
            System.out.println(GameConstants.PORT_NUMBER_NOT_VALID);
        }
    }
}
