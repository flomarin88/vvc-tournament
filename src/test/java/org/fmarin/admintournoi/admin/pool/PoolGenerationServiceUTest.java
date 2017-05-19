package org.fmarin.admintournoi.admin.pool;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;


@Ignore
@RunWith(MockitoJUnitRunner.class)
public class PoolGenerationServiceUTest {

    private PoolGenerationService service = new PoolGenerationService();

    @Test
    public void compose_firstRound() {
        // Given
        List<Integer> teams = new Random().ints(48, 1, 5).boxed().collect(Collectors.toList());

        // When
        service.compose(teams);
        
        // Then
    }
    
}