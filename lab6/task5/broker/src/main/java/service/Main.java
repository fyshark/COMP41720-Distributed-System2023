package service;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import service.message.Broker;

public class Main {

    public static void main(String[] args) {


        Config config = ConfigFactory.load();

        ActorSystem system = ActorSystem.create("broker", config);
        // create props
        final Props props = Props.create(Broker.class);
        // create broker actor
        final ActorRef broker = system.actorOf(props, "BrokerActor");
    }

}
