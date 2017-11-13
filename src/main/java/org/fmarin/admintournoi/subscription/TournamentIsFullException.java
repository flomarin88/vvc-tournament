package org.fmarin.admintournoi.subscription;

public class TournamentIsFullException extends RuntimeException {

    public TournamentIsFullException() {
        super("Tournament is full");
    }
}
