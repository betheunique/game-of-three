package com.takeaway.game.server;

import java.io.IOException;
import java.util.logging.Logger;
import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.takeaway.game.constants.GameConstants;
import com.takeaway.game.utils.GameUtils;

/**
 * @author abhishekrai
 * @since 0.1.0
 */
@ServerEndpoint("/of3")
public class GameServerEndPoint {

    private final Logger logger = Logger.getLogger(this.getClass().getName());
    private Session newSession;

    @OnOpen
    public void onOpen(Session session) {
        logger.info("Connected ... " + session.getId());
        this.newSession = session;
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("Incoming message :" + message);
        try {
            if(message.equalsIgnoreCase(GameConstants.GAME_WIN_MESSAGE)) {
                System.out.println(GameConstants.GAME_LOST_MESSAGE);
            } else {
                int number = Integer.parseInt(message);
                if(number / 3 == 1) {
                    System.out.println(GameConstants.GAME_WIN_MESSAGE);
                    newSession.getBasicRemote().sendText( GameConstants.GAME_WIN_MESSAGE);
                } else {
                    int processedNumber = GameUtils.makeNumberDivisibleByThree(number);

                    if((processedNumber / 3) == 1) {
                        newSession.getBasicRemote().sendText(GameConstants.GAME_WIN_MESSAGE);
                    } else {
                        newSession.getBasicRemote().sendText(String.valueOf(processedNumber / 3));
                    }
                }
            }
        } catch (NumberFormatException | IOException io) {
            throw new RuntimeException(io);
        }

    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        logger.info(String.format("Session %s closed because of %s", session.getId(), closeReason));
    }

    @OnError
    public void onError(Session session, Throwable e) {
        logger.info(String.format("Session %s closed because of %s", session.getId(), e));
        try {
            newSession.close(new CloseReason(CloseReason.CloseCodes.UNEXPECTED_CONDITION, e.toString()));
        } catch (IOException io) {
            throw new RuntimeException(io);
        }
    }

}
