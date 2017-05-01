package org.fmarin.admintournoi.subscription;

import javax.xml.bind.ValidationException;

public class TournamentIsFullException extends ValidationException {

    public TournamentIsFullException() {
        super("Tournament is full", "TOURNAMENT_FULL");
    }
}
