package org.example.client;

import org.example.server_topdown.Person;
import org.example.server_topdown.PersonNotFoundException_Exception;
import org.example.server_topdown.PersonService;
import org.example.server_topdown.PersonService_Service;

import java.net.MalformedURLException;
import java.net.URL;

public class Client {
    public static void main(String[] args) throws MalformedURLException, PersonNotFoundException_Exception {
        int num = -1;
        URL address = new URL("http://localhost:8081/personservice?wsdl");
        PersonService_Service pService = new PersonService_Service();
        PersonService pServiceProxy = pService.getPersonServiceImplPort();
        num = pServiceProxy.countPersons();
        System.out.println("Num of Persons = " + num);
        Person person = pServiceProxy.getPerson(2);
        System.out.println("Person "+person.getFirstName()+", id = "+person.getId());
    }
}
