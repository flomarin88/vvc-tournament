package org.fmarin.admintournoi.field;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class FieldServiceUTest {

    @InjectMocks
    private FieldService service;

    @Mock
    private FieldRepository mockedRepository;

    @Test
    public void create_should_instantiate_count_fields() {
        // Given

        // When
        service.create(3);

        // Then
        verify(mockedRepository).save(anyListOf(Field.class));
    }
}