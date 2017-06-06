package org.fmarin.admintournoi.admin.match;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MatchControllerUTest {

    @InjectMocks
    private MatchController controller;

    @Mock
    private MatchRepository mockedMatchRepository;


    @Test
    public void update() {
        // Given
        Long matchId = 1L;
        ScorePutView scores = new ScorePutView(21, 15);

        Match match = MatchBuilder.aMatch()
                .withId(matchId)
                .build();

        when(mockedMatchRepository.findOne(matchId)).thenReturn(match);

        // When
        controller.update(matchId, scores);
        
        // Then
        assertThat(match.getScoreTeam1()).isEqualTo(21);
        assertThat(match.getScoreTeam2()).isEqualTo(15);
        verify(mockedMatchRepository).save(match);
    }
}