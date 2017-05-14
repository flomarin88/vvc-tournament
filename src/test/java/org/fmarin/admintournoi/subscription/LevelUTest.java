package org.fmarin.admintournoi.subscription;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class LevelUTest {

    @Test
    public void valueOf_shoudlReturnLevelEnumFromValue() {
        // Given

        
        // When
        Level nullResult = Level.valueOf(0);
        Level nationalResult = Level.valueOf(1);
        Level regionalResult = Level.valueOf(2);
        Level depResult = Level.valueOf(3);
        Level loisirsResult = Level.valueOf(4);
        
        // Then
        assertThat(nullResult).isNull();
        assertThat(nationalResult).isEqualTo(Level.NATIONAL);
        assertThat(regionalResult).isEqualTo(Level.REGIONAL);
        assertThat(depResult).isEqualTo(Level.DEPARTEMENTAL);
        assertThat(loisirsResult).isEqualTo(Level.LOISIRS);
    }
    
}