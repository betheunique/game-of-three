package com.takeaway.game.client;

import java.util.logging.Logger;
import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

import com.takeaway.game.constants.GameConstants;

/**
 * @author abhishekrai
 * @since 0.1.0
 */
@ClientEndpoint
public class UserInputEndPoint {

    private final Logger logger = Logger.getLogger(this.getClass().getName());

    @OnOpen
    public void onOpen(Session session) {
        logger.info("Connected ... " + session.getId());
    }


    @OnMessage
    public void onMessage(String message, Session session) {
        if(message.equalsIgnoreCase(GameConstants.GAME_WIN_MESSAGE)) {
            System.out.println("You Lost, Please enter number to start or 'quit' to close");
        } else
            System.out.println("Number received as :" + message + "\n please divide the  number by 3 " +
                                   "after adding -1 or 1 or 0 \n");
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        logger.info(String.format("Session %s closed because of %s", session.getId(), closeReason));
    }
}
