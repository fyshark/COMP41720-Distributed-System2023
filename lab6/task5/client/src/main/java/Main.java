import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import service.actor.Client;
import service.core.ClientInfo;
import service.message.ClientMessage;

public class Main {

    public static void main(String[] args) {

        Config config = ConfigFactory.load();

        String broker = "localhost";

        if (args.length != 0) {
            System.out.println("args: " + args);
            broker = args[1];
        }

        ActorSystem system = ActorSystem.create("client", config);
        //Creating an url which allow for communicating with the broker
        ActorSelection brokerActorRef = system.actorSelection("akka.tcp://broker@" + broker + ":2553/user/BrokerActor");

        // create props
        final Props props = Props.create(Client.class);
        // create actor for client
        final ActorRef clientActor = system.actorOf(props);

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        for (ClientInfo client : clients) {
            try {
                // Sending the request to the broker
                brokerActorRef.tell(client, clientActor);
                System.out.println("Client has sent an message to broker：" + client);
            } catch (Exception e) {
                // Handle any errors
                e.printStackTrace();
            }
        }

    }

    public static final ClientInfo[] clients = {
            new ClientInfo("Niki Collier", ClientInfo.FEMALE, 49, 1.5494, 80, false, false),
            new ClientInfo("Old Geeza", ClientInfo.MALE, 65, 1.6, 100, true, true),
            new ClientInfo("Hannah Montana", ClientInfo.FEMALE, 21, 1.78, 65, false, false),
            new ClientInfo("Rem Collier", ClientInfo.MALE, 49, 1.8, 120, false, true),
            new ClientInfo("Jim Quinn", ClientInfo.MALE, 55, 1.9, 75, true, false),
            new ClientInfo("Donald Duck", ClientInfo.MALE, 35, 0.45, 1.6, false, false)
    };
}

