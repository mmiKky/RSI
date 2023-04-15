package org.example.person_service;

import jakarta.jws.WebMethod;
import jakarta.jws.WebService;
import org.example.person_service.exceptions.PersonExistsException;
import org.example.person_service.exceptions.PersonNotFoundException;

import java.util.List;

@WebService
public interface PersonService {
    @WebMethod
    Person getPerson(int id) throws PersonNotFoundException;
    @WebMethod
    Person updatePerson(int id, String name, int age) throws PersonNotFoundException;
    @WebMethod
    boolean deletePerson(int id) throws PersonNotFoundException;
    @WebMethod
    Person addPerson(int id, String name, int age) throws PersonExistsException;
    @WebMethod
    int countPersons();
    @WebMethod
    List<Person> getAllPersons();
}
