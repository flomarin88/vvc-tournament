package org.fmarin.admintournoi.field;

import com.google.common.collect.Lists;
import org.fmarin.admintournoi.subscription.Tournament;
import org.fmarin.admintournoi.subscription.TournamentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class FieldService {

    private final FieldRepository repository;
    private final TournamentRepository tournamentRepository;

    @Autowired
    public FieldService(FieldRepository repository, TournamentRepository tournamentRepository) {
        this.repository = repository;
        this.tournamentRepository = tournamentRepository;
    }

    public void create(int count, long tournamentId) {
        Tournament tournament = tournamentRepository.findOne(tournamentId);
        ArrayList<Field> fields = Lists.newArrayList();
        for (int i = 0; i < count; i++) {
            Field field = new Field();
            field.setName(String.valueOf(i + 1));
            field.setTournament(tournament);
            fields.add(field);
        }
        repository.save(fields);
    }
}
