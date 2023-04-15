package org.example.person_service;

import jakarta.xml.ws.Endpoint;
import java.io.IOException;

public class ServiceHost {

    public static void main(String[] args) {
        System.out.println("Web Service PersonService is running ...");

        PersonServiceImpl personService = new PersonServiceImpl();
        Endpoint.publish("http://localhost:8081/personservice", personService);
        System.out.println("Press ENTER to STOP PersonService ...");
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
