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

    Team subscribe(Subscription subscription) throws TeamAlreadyExistsException, TournamentIsFullException {
        Tournament tournament = tournamentRepository.findOne(subscription.getTournamentId());
        if (tournament != null && !isFull(tournament)) {
            if (teamRepository.findByNameAndPaymentStatus(subscription.getName(), "Completed") == null) {
                Team team = create(subscription, tournament);
                return teamRepository.save(team);
            }
            throw new TeamAlreadyExistsException();
        }
        throw new TournamentIsFullException();
    }

    boolean isFull(Tournament tournament) {
        long count = tournament.getTeams().stream().filter(team -> "Completed".equals(team.getPaymentStatus())).count();
        return count >= tournament.getTeamLimit();
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
