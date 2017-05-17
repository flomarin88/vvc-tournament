package org.fmarin.admintournoi.fixtures;

import org.fmarin.admintournoi.subscription.Level;
import org.fmarin.admintournoi.subscription.TeamBuilder;
import org.fmarin.admintournoi.subscription.Tournament;

public class FixtureTeam {

    public static TeamBuilder withDefaultValues() {
        return TeamBuilder.aTeam()
                .withId(1L)
                .withName("Team")
                .withTournament(new Tournament())
                .withLevel(Level.LOISIRS)
                .withCaptainName("Captain")
                .withCaptainPhone("0123456789")
                .withCaptainEmail("captain@gmail.com");
    }
}
