package org.fmarin.admintournoi.admin.round;

import com.google.common.collect.Lists;
import org.fmarin.admintournoi.admin.match.MatchBuilder;
import org.fmarin.admintournoi.admin.pool.Pool;
import org.fmarin.admintournoi.admin.pool.PoolBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class RoundControllerUTest {

    @InjectMocks
    private RoundController controller;
    
    @Test
    public void getColorStatus_should_return_primary_when_there_is_not_match() {
        // Given
        Pool pool = PoolBuilder.aPool().build();
        
        // When
        String result = controller.getColorStatus(pool);

        // Then
        assertThat(result).isEqualTo("primary");
    }

    @Test
    public void getColorStatus_should_return_primary_when_there_is_not_finished_match() {
        // Given
        Pool pool = PoolBuilder.aPool()
                .withMatches(Lists.newArrayList(MatchBuilder.aMatch().build()))
                .build();

        // When
        String result = controller.getColorStatus(pool);

        // Then
        assertThat(result).isEqualTo("primary");
    }

    @Test
    public void getColorStatus_should_return_success_when_all_matches_are_finished() {
        // Given
        Pool pool = PoolBuilder.aPool()
                .withMatches(Lists.newArrayList(MatchBuilder.aMatch().withScoreTeam1(12).build()))
                .build();

        // When
        String result = controller.getColorStatus(pool);

        // Then
        assertThat(result).isEqualTo("success");
    }
    
}