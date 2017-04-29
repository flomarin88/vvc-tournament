package org.fmarin.admintournoi.subscription;

import org.fmarin.admintournoi.helper.TimeMachine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubscriptionService {

    private final TeamRepository teamRepository;
    private final TournamentRepository tournamentRepository;

    @Autowired
    public SubscriptionService(TeamRepository teamRepository, TournamentRepository tournamentRepository) {
        this.teamRepository = teamRepository;
        this.tournamentRepository = tournamentRepository;
    }

    Long subscribe(Subscription subscription) {
        Tournament tournament = tournamentRepository.findOne(subscription.getTournamentId());
        if (tournament != null) {
            if (tournament.getTeamLimit() > tournament.getTeams().size()) {
                Team team = create(subscription, tournament);
                team = teamRepository.save(team);
                return team.getId();
            }
        }
        return null;
    }

    Team create(Subscription subscription, Tournament tournament) {
        Team team = new Team();
        team.setTournament(tournament);
        team.setName(subscription.getName());
        team.setLevel(subscription.getLevel());
        team.setCaptainName(subscription.getCaptainName());
        team.setCaptainEmail(subscription.getCaptainEmail());
        team.setCaptainPhone(subscription.getCaptainPhone());
        team.setPlayer2Name(subscription.getPlayer2Name());
        team.setPlayer2Email(subscription.getPlayer2Email());
        team.setPlayer3Name(subscription.getPlayer3Name());
        team.setPlayer3Email(subscription.getPlayer3Email());
        team.setCreatedAt(TimeMachine.now());
        return team;
    }
}
