import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import akka.testkit.TestKit;
import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;
import service.Quoter;
import service.core.ClientInfo;
import service.message.ClientMessage;
import service.message.QuotationMessage;

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
    public void quoterTest() {
        final Props props = Props.create(Quoter.class);
        final ActorRef subject = system.actorOf(props);
        final TestKit probe = new TestKit(system);
        // props send message to server
        subject.tell(
                new ClientMessage(1l,
                        new ClientInfo("Niki Collier", ClientInfo.FEMALE, 49, 1.5494, 80, false, false)), probe.testActor());
        probe.expectMsgClass(FiniteDuration.apply(10, TimeUnit.SECONDS), QuotationMessage.class);
    }
}