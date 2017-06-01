package org.fmarin.admintournoi.admin.pool;

import com.google.common.collect.Lists;
import org.fmarin.admintournoi.subscription.Level;
import org.fmarin.admintournoi.subscription.Team;
import org.fmarin.admintournoi.subscription.TeamBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(MockitoJUnitRunner.class)
public class PoolGenerationServiceUTest {

    @InjectMocks
    private PoolGenerationService service;

    @Mock
    private PoolRepository mockedPoolRepository;

    @Test
    public void countTeamsByLevel() {
        // Given
        List<Team> teams = Lists.newArrayList(
                TeamBuilder.aTeam().withLevel(Level.NATIONAL).build(),
                TeamBuilder.aTeam().withLevel(Level.REGIONAL).build(),
                TeamBuilder.aTeam().withLevel(Level.NATIONAL).build(),
                TeamBuilder.aTeam().withLevel(Level.NATIONAL).build()
        );

        // When
        Map<Integer, Long> result = service.countTeamsByLevel(teams);

        // Then
        assertThat(result).hasSize(2)
                .containsEntry(1, 3L)
                .containsEntry(2, 1L);
    }

}