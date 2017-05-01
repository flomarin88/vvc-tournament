package org.fmarin.admintournoi.subscription;

import javax.validation.ValidationException;

public class TeamAlreadyExistsException extends ValidationException {

    public TeamAlreadyExistsException() {
        super("Team already exists");
    }
}
