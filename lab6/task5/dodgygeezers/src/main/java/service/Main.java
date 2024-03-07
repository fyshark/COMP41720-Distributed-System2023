package service;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import service.message.RegisterMessage;

public class Main {

    public static void main(String[] args) {

        Config config = ConfigFactory.load();

        String broker = "localhost";

        if (args.length != 0) {
            System.out.println("args: " + args);
            broker = args[1];
        }

        ActorSystem system = ActorSystem.create("dgq", config);
        // akka.<protocol>://<actorsystemname>@<hostname>:<port>/<actor path>
        ActorSelection brokerActorRef = system.actorSelection("akka.tcp://broker@" + broker + ":2553/user/BrokerActor");
        // create props
        final Props props = Props.create(Quoter.class);
        // create actor for dodgygeezers
        final ActorRef dgqActor = system.actorOf(props);

        brokerActorRef.tell(new RegisterMessage(), dgqActor);
        System.out.println("Dodgygeezers has sent an message to broker.");
    }
}