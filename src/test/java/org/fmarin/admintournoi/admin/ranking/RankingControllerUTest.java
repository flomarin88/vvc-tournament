package org.fmarin.admintournoi.admin.ranking;

import com.google.common.collect.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RankingControllerUTest {

    @InjectMocks
    private RankingController controller;

    @Mock
    private RankingService mockedRankingService;
    
    @Test
    public void getPoolRankings() {
        // Given
        Ranking ranking = RankingBuilder.aRanking().build();
        ArrayList<Ranking> rankings = Lists.newArrayList(ranking);
        when(mockedRankingService.getPoolRanking(1L)).thenReturn(rankings);
        
        // When
        ResponseEntity result = controller.getPoolRankings(1L);
        
        // Then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo(rankings);
    }
}