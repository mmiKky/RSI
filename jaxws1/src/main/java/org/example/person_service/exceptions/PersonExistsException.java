package org.example.person_service.exceptions;

import jakarta.xml.ws.WebFault;

@WebFault
public class PersonExistsException extends Exception {
    public PersonExistsException() {
        super("This person already exists");
    }
}
