import java.io.IOException;
import java.net.URI;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import javax.websocket.DeploymentException;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

import com.takeaway.game.server.GameServerEndPoint;
import org.glassfish.tyrus.client.ClientManager;
import org.glassfish.tyrus.server.Server;
import org.glassfish.tyrus.test.tools.TestContainer;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author abhishekrai
 * @since 0.1.0
 */
public class GameServerEndPointTest extends TestContainer {

    private static Server server = null;

    @BeforeClass
    public static void createAndStartServer() throws DeploymentException {
        server = new Server("localhost", 8025, "/game", null,
                            GameServerEndPoint.class);
        server.start();
    }

    @AfterClass
    public static void stopServer(){
        if( server != null ) {
            server.stop();
        }
    }

    @Test
    public void testSessionIsCreated() throws DeploymentException {
        try {
            final CountDownLatch latch = new CountDownLatch(1);

            ClientManager client = createClient();
            client.connectToServer(new Endpoint() {
                @OnOpen
                public void onOpen(Session session, EndpointConfig config) {
                    latch.countDown();
                }

            }, URI.create("ws://localhost:8025/game/of3"));

            latch.await(1, TimeUnit.SECONDS);
            assertEquals("Session created :",0, latch.getCount());

        } catch (InterruptedException  | IOException e) {
            e.printStackTrace();
//            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Test
    public void testCorrectNumberReceived() throws DeploymentException {
        final AtomicInteger counter = new AtomicInteger(0);
        final CountDownLatch latch = new CountDownLatch(1);
        try {
            ClientManager client = createClient();
            client.connectToServer(new Endpoint() {
                @OnOpen
                public void onOpen(Session session, EndpointConfig config) {
                    try {
                        session.getBasicRemote().sendText(String.valueOf(6));
                        session.addMessageHandler(new MessageHandler.Whole<String>() {
                            @OnMessage
                            public void onMessage(String number) {
                                try {
                                    counter.getAndAdd(Integer.parseInt(number));
                                    latch.countDown();
                                } catch ( NumberFormatException e) {
                                    e.printStackTrace();
                                }
                            }

                        });
                    } catch (IOException io) {
                        // ... do nothing
                    }
                }

            }, URI.create("ws://localhost:8025/game/of3"));

            latch.await(1, TimeUnit.SECONDS);
            assertEquals("Number Received :",2, counter.get());

        } catch ( InterruptedException | IOException e) {
            e.printStackTrace();
//            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Test
    public void testGameWinReceived() throws DeploymentException {
        final AtomicBoolean flag = new AtomicBoolean(false);
        final CountDownLatch latch = new CountDownLatch(1);
        try {
            ClientManager client = createClient();
            client.connectToServer(new Endpoint() {
                @OnOpen
                public void onOpen(Session session, EndpointConfig config) {
                    try {
                        session.getBasicRemote().sendText(String.valueOf(3));
                        session.addMessageHandler(new MessageHandler.Whole<String>() {
                            @OnMessage
                            public void onMessage(String message) {
                                try {
                                    if(message.equalsIgnoreCase("WIN!!")) {
                                        flag.set(true);
                                        latch.countDown();
                                    }
                                } catch ( NumberFormatException e) {
                                    e.printStackTrace();
                                }
                            }

                        });
                    } catch (IOException io) {
                        // ... do nothing
                    }
                }

            }, URI.create("ws://localhost:8025/game/of3"));

            latch.await(1, TimeUnit.SECONDS);
            assertTrue("Game WIN!! :", flag.get());
        } catch ( InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }

}
