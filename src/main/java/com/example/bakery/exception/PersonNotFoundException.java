package com.example.bakery.exception;

/**
 * Thrown when a user is not found.
 * Lecture 06: Custom Exceptions (extending BakeryException)
 */
public class PersonNotFoundException extends BakeryException {
    public PersonNotFoundException(String username) {
        super("Person not found: " + username, "PERSON_NOT_FOUND");
    }
}
