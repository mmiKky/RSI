package org.example.person_service.exceptions;

import jakarta.xml.ws.WebFault;
@WebFault
public class PersonNotFoundException extends Exception {
    public PersonNotFoundException() {
        super("The specified person does not exist");
    }
}
