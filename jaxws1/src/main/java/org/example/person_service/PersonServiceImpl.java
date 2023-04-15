package org.example.person_service;

import jakarta.jws.WebMethod;
import jakarta.jws.WebService;
import org.example.person_service.exceptions.PersonExistsException;
import org.example.person_service.exceptions.PersonNotFoundException;

import java.util.List;

@WebService(serviceName = "PersonService", endpointInterface = "org.example.person_service.PersonService")
public class PersonServiceImpl implements PersonService {
    private PersonRepository dataRepository = new PersonRepositoryImpl();

    @WebMethod
    public Person getPerson(int id) throws PersonNotFoundException {
        System.out.println("...called getPerson id="+id);
        return dataRepository.getPerson(id);
    }

    @WebMethod
    public Person updatePerson(int id, String name, int age) throws PersonNotFoundException {
        System.out.println("...called updatePerson");
        return dataRepository.updatePerson(id, name, age);
    }

    @WebMethod
    public boolean deletePerson(int id) throws PersonNotFoundException {
        System.out.println("...called deletePerson");
        return dataRepository.deletePerson(id);
    }

    @WebMethod
    public Person addPerson(int id, String name, int age) throws PersonExistsException {
        System.out.println("...called addPerson");
        return dataRepository.addPerson(id, name, age);
    }
    @WebMethod
    public int countPersons() {
        System.out.println("...called countPerson");
        return dataRepository.countPersons();
    }
    @WebMethod
    public List<Person> getAllPersons() {
        System.out.println("...called getAllPersons");
        return dataRepository.getAllPersons();
    }
}
