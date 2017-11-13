package org.fmarin.admintournoi.field;

import org.fmarin.admintournoi.subscription.Tournament;
import org.fmarin.admintournoi.subscription.TournamentRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.Mockito.verify;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FieldServiceUTest {

    @InjectMocks
    private FieldService service;

    @Mock
    private FieldRepository mockedRepository;
    @Mock
    private TournamentRepository mockedTournamentRepository;

    @Test
    public void create_should_instantiate_count_fields() {
        // Given
        int count = 3;
        long tournamentId = 10L;
        Tournament tournament = new Tournament();
        when(mockedTournamentRepository.findOne(tournamentId)).thenReturn(tournament);

        // When
        service.create(count, tournamentId);

        // Then
        ArgumentCaptor<List> argumentCaptor = ArgumentCaptor.forClass(List.class);
        verify(mockedRepository).save(argumentCaptor.capture());
        List<Field> value = argumentCaptor.getValue();
        assertThat(value).hasSize(3)
                .extracting("id", "name", "tournament")
                .containsSequence(
                        tuple(null, "1", tournament),
                        tuple(null, "2", tournament),
                        tuple(null, "3", tournament));
    }
}