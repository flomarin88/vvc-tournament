package org.fmarin.admintournoi.admin.pool;

import com.google.common.collect.Lists;
import org.fmarin.admintournoi.subscription.Level;
import org.fmarin.admintournoi.subscription.Team;
import org.fmarin.admintournoi.subscription.TeamBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(MockitoJUnitRunner.class)
public class PoolGenerationServiceUTest {

    @InjectMocks
    private PoolGenerationService service;

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

    @Test
    public void getFieldList() {
        // Given
        // When
        List<Integer> result = service.getFieldList("4-11;20-22");

        // Then
        assertThat(result).hasSize(11)
                .containsSequence(4, 5, 6, 7, 8, 9, 10, 11, 20, 21, 22);
    }

    @Test
    public void getField() {
        // Given
        List<Integer> fields = Lists.newArrayList(4, 5, 6, 7, 8, 9, 10, 11);

        // When
        Integer result1 = service.getField(fields, 1);
        Integer result2 = service.getField(fields, 3);
        Integer result3 = service.getField(fields, 8);
        Integer result4 = service.getField(fields, 12);
        Integer result5 = service.getField(fields, 16);
        Integer result6 = service.getField(fields, 9);
        
        // Then
        assertThat(result1).isEqualTo(4);
        assertThat(result2).isEqualTo(6);
        assertThat(result3).isEqualTo(11);
        assertThat(result4).isEqualTo(7);
        assertThat(result5).isEqualTo(11);
        assertThat(result6).isEqualTo(4);
    }

}