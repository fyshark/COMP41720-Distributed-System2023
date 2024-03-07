package service.broker.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import service.core.Application;
import service.core.ClientInfo;
import service.core.Quotation;


import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

@RestController
public class BrokerController {

    private final List<String> urls = new ArrayList<String>() {
        {
            add("http://localhost:8080/");
            add("http://localhost:8081/");
            add("http://localhost:8082/");
        }
    };

    private Map<Integer, Application> applications = new TreeMap<>();

    @PostMapping(value="/applications", consumes="application/json")
    public ResponseEntity<Application> createApplication(
            @RequestBody ClientInfo info) {
        /**
         * 1. loop urls
         * 2. create application obj
         * 3. contact services, provide client info, post quotation, auldfellas POST http://localhost:8080/quotations - body: ClientInfo
         * 4. get quotation and update it to application obj
         * 5. return a ResponseEntity containing application obj to client
         */
        Application app = new Application(info);

        for(String url: urls) {
            // use RestTemplate to call each service
            RestTemplate template = new RestTemplate();
            ResponseEntity<Quotation> response = template.postForEntity(url + "/quotations", info, Quotation.class);
            app.addQuotation(response.getBody());
        }
        applications.put(app.getId(), app);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(app);

    }

    @GetMapping(value="/applications", consumes="application/json")
    public ResponseEntity<ArrayList<String>> getApplications() {
        ArrayList<String> list = new ArrayList<>();
        for (Application app: applications.values()) {
            list.add("http://" + getHost() + "/applications/" + app.getId());
        }
        return ResponseEntity.status(HttpStatus.OK).body(list);
    }

    @GetMapping(value="/applications/{id}", produces={"application/json"})
    public ResponseEntity<Application> getApplication(@PathVariable Integer id) {
        Application application = applications.get(id);
        if (application == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(application);
    }

    private String getHost() {
        try {
            return InetAddress.getLocalHost().getHostAddress() + ":" + port;
        } catch (UnknownHostException e) {
            return "localhost:" + port;
        }
    }

    @Value("${server.port}")
    private int port;

}
