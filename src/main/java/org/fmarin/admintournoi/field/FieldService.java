package org.fmarin.admintournoi.field;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FieldService {

    private final FieldRepository repository;

    @Autowired
    public FieldService(FieldRepository repository) {
        this.repository = repository;
    }

    public void create(int count) {
        repository.save(Lists.newArrayList());
    }
}
