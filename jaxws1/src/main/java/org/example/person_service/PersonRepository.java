package org.example.person_service;

import org.example.person_service.exceptions.PersonExistsException;
import org.example.person_service.exceptions.PersonNotFoundException;

import java.util.List;

public interface PersonRepository {
    List<Person> getAllPersons();
    Person getPerson(int id) throws PersonNotFoundException;
    Person updatePerson(int id, String name, int age) throws PersonNotFoundException;
    boolean deletePerson(int id) throws PersonNotFoundException;
    Person addPerson(int id, String name, int age) throws PersonExistsException;
    int countPersons();
}
