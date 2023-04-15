package org.example.person_service;

import org.example.person_service.exceptions.PersonExistsException;
import org.example.person_service.exceptions.PersonNotFoundException;

import java.util.ArrayList;
import java.util.List;

public class PersonRepositoryImpl implements PersonRepository {
    private List<Person> personList;

    public PersonRepositoryImpl() {
        personList = new ArrayList<>();
        personList.add(new Person(1, "Marius", 9));
        personList.add(new Person(2, "Andrew", 10));
        personList.add(new Person(3, "Thomas", 11));
    }

    public List<Person> getAllPersons() {
        return personList;
    }

    public Person getPerson(int id) throws PersonNotFoundException {
        for (Person person : personList) {
            if (person.getId() == id) {
                return person;
            }
        }
        throw new PersonNotFoundException();
    }

    public Person updatePerson(int id, String name, int age) throws PersonNotFoundException {
        for (Person person : personList) {
            if (person.getId() == id) {
                person.setFirstName(name);
                person.setAge(age);
                return person;
            }
        }
        throw new PersonNotFoundException();
    }

    public boolean deletePerson(int id) throws PersonNotFoundException {
        for (Person person : personList) {
            if (person.getId() == id) {
                personList.remove(person);
                return true;
            }
        }
        throw new PersonNotFoundException();
    }

    public Person addPerson(int id, String name, int age) throws PersonExistsException {
        for (Person person : personList) {
            if (person.getId() == id) {
                throw new PersonExistsException();
            }
        }
        Person person = new Person(id, name, age);
        personList.add(person);
        return person;
    }

    public int countPersons() {
        return personList.size();
    }
}
