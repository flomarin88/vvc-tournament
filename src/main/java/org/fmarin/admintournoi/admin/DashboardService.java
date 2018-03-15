package org.fmarin.admintournoi.admin;

import com.google.common.collect.Maps;
import org.fmarin.admintournoi.helper.TimeMachine;
import org.fmarin.admintournoi.subscription.Gender;
import org.fmarin.admintournoi.subscription.Tournament;
import org.fmarin.admintournoi.subscription.TournamentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class DashboardService {

    private final TournamentRepository tournamentRepository;

    @Autowired
    public DashboardService(TournamentRepository tournamentRepository) {
        this.tournamentRepository = tournamentRepository;
    }

    public Map<String, Object> getCurrentSubscriptionsStats() {
        Map<String, Object> stats = getCurrentSubscriptionsStats(Gender.MEN);
        stats.putAll(getCurrentSubscriptionsStats(Gender.WOMEN));
        return stats;
    }

    Map<String, Object> getCurrentSubscriptionsStats(Gender gender) {
        int currentYear = TimeMachine.now().getYear();
        Tournament tournament = tournamentRepository.findByYearAndGender(currentYear, gender);
        Map<String, Object> result = Maps.newHashMap();
        String prefix = gender.name().toLowerCase();
        if (tournament != null) {
            result.put(prefix + "_teams_limit", tournament.getTeamLimit());
            result.put(prefix + "_teams_subscribed", getSubscriptionsCount(tournament));
        }
        return result;
    }

    private long getSubscriptionsCount(Tournament tournament) {
        return tournament.getTeams().parallelStream()
                .filter(item -> "Completed".equals(item.getPaymentStatus()))
                .count();
    }
}
