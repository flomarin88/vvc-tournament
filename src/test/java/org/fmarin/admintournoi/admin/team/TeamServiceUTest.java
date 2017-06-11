package org.fmarin.admintournoi.admin.team;

import org.fmarin.admintournoi.admin.round.Round;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.fmarin.admintournoi.admin.round.RoundBuilder.aRound;

@RunWith(MockitoJUnitRunner.class)
public class TeamServiceUTest {

    @InjectMocks
    private TeamService service;

    @Test
    public void getAllPreviousRounds() {
        // Given
        Round first = aRound().build();
        Round second = aRound().withPreviousRound(first).build();
        Round round = aRound().withPreviousRound(second).build();

        // When
        List<Round> result = service.getAllPreviousRounds(round);

        // Then
        assertThat(result).hasSize(2).contains(first, second);

    }
}