import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import akka.testkit.TestKit;
import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;
import service.core.*;
import service.message.*;

import java.util.concurrent.TimeUnit;

public class QuoterTests {
    static ActorSystem system;

    @BeforeClass
    public static void setup() {system = ActorSystem.create(); }

    @AfterClass
    public static void teardown() {
        TestKit.shutdownActorSystem(system, Duration.apply(2, TimeUnit.SECONDS), false);
        system = null;
    }
    @Test
    public void quoterTest() throws InterruptedException {
        final Props props = Props.create(Broker.class);
        final ActorRef broker = system.actorOf(props); // broker
        final TestKit server = new TestKit(system); // server
        final TestKit client = new TestKit(system); // client

        // server send a RegisterMessage to broker
        broker.tell(new RegisterMessage(), server.testActor());
        server.expectNoMessage(Duration.create(100, TimeUnit.MILLISECONDS)); // Verify that no immediate response is expected
        client.expectNoMessage();

        // Send a ClientMessage to the Quoter actor
        ClientInfo clientInfo = new ClientInfo("Niki Collier", ClientInfo.FEMALE, 49, 1.5494, 80, false, false);
        // client send clientInfo to broker
        broker.tell(clientInfo, client.testActor());

        // server received ClientMessage msg
        server.expectMsgClass(FiniteDuration.apply(10, TimeUnit.SECONDS), ClientMessage.class);

        client.expectMsgClass(FiniteDuration.apply(10, TimeUnit.SECONDS), OfferMessage.class);

    }
}