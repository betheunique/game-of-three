package com.takeaway.game.client;

import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Logger;
import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

import com.takeaway.game.constants.GameConstants;
import com.takeaway.game.utils.GameUtils;

/**
 * @author abhishekrai
 * @since 0.1.0
 */
@ClientEndpoint
public class AutomaticPlayerEndPoint {
    private final Logger logger = Logger.getLogger(this.getClass().getName());
    private Session newSession;

    @OnOpen
    public void onOpen(Session session) {
        logger.info("Connected ... " + session.getId());
        try {
            this.newSession = session;
            newSession.getBasicRemote().sendText(String.valueOf(ThreadLocalRandom.current().nextInt(3, 1000 + 1)));
        } catch (IOException io) {
            throw new RuntimeException(io);
        }
    }



    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("Incoming message :" + message);
//        if(message.equals("WIN!!")) {
//            try {
////                Thread.sleep(6000);
//                session.getBasicRemote().sendText(String.valueOf(ThreadLocalRandom.current()
//                                                                         .nextInt(3, 100 + 1)));
//            } catch ( IOException e) {
//
//            }
//        }
//        int input = Integer.parseInt(message);

//        OptionalInt result = Arrays.stream(a).map(n -> n + input).filter(n -> (n % 3 == 0)).findFirst();
//        try {
//            if(result.isPresent()) {
//                if((result.getAsInt() / 3) == 1)
//                    newSession.getBasicRemote().sendText( "WIN!!");
//                else
//                    newSession.getBasicRemote().sendText(String.valueOf(result.getAsInt() / 3));
//            }
//        } catch (IOException io) {
//            throw new RuntimeException(io);
//        }

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
                        System.out.println(GameConstants.GAME_WIN_MESSAGE);
                        newSession.getBasicRemote().sendText( GameConstants.GAME_WIN_MESSAGE);
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
    public void onError(Session session, Throwable throwable) {
        try {
            newSession.close(new CloseReason(CloseReason.CloseCodes.CLOSED_ABNORMALLY, throwable.toString()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
