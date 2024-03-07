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

        ActorSystem system = ActorSystem.create("gaq", config);
        // akka.<protocol>://<actorsystemname>@<hostname>:<port>/<actor path>
        ActorSelection brokerActorRef = system.actorSelection("akka.tcp://broker@localhost:2553/user/BrokerActor");

        // create props
        final Props props = Props.create(Quoter.class);
        // create actor for girlsallowed
        final ActorRef gaqActor = system.actorOf(props);

        brokerActorRef.tell(new RegisterMessage(), gaqActor);
        System.out.println("Girlsallowed has sent an message to broker.");

    }
}