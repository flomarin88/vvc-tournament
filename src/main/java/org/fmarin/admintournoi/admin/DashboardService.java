package org.fmarin.admintournoi.admin;

import com.google.common.collect.Maps;
import org.fmarin.admintournoi.features.FeatureManager;
import org.fmarin.admintournoi.helper.TimeMachine;
import org.fmarin.admintournoi.subscription.Gender;
import org.fmarin.admintournoi.subscription.Tournament;
import org.fmarin.admintournoi.subscription.TournamentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;

@Service
public class DashboardService {

  private final TournamentRepository tournamentRepository;
  private final FeatureManager featureManager;

  private final BigDecimal PAYPAL_UNIT_PRICE = new BigDecimal(31.63);

  @Autowired
  public DashboardService(TournamentRepository tournamentRepository, FeatureManager featureManager) {
    this.tournamentRepository = tournamentRepository;
    this.featureManager = featureManager;
  }

  public Map<String, Object> getCurrentSubscriptionsStats() {
    int currentYear = TimeMachine.now().getYear();
    Tournament menTournament = tournamentRepository.findByYearAndGender(currentYear, Gender.MEN);
    Tournament womenTournament = tournamentRepository.findByYearAndGender(currentYear, Gender.WOMEN);
    Map<String, Object> result = Maps.newHashMap();
    result.putAll(getCurrentStats(menTournament));
    result.putAll(getCurrentStats(womenTournament));
    result.put("show_paypal", !menTournament.isFull() || !womenTournament.isFull());
    result.put("paypal_sales_total", getPaypalSalesTotal(menTournament, womenTournament));
    result.put("checkin_enabled", featureManager.isCheckinEnabled());
    return result;
  }

  Map<String, Object> getCurrentStats(Tournament tournament) {
    Map<String, Object> result = Maps.newHashMap();
    String prefix = tournament.getGender().name().toLowerCase();
    result.put(prefix + "_tournament_id", tournament.getId());
    result.put(prefix + "_teams_limit", tournament.getTeamLimit());
    result.put(prefix + "_teams_subscribed", tournament.getSubscribedTeams().size());
    result.put(prefix + "_teams_checked", tournament.getPresentTeamsCount());
    result.put(prefix + "_full", tournament.isFull());
    return result;
  }

  private String getPaypalSalesTotal(Tournament men, Tournament women) {
    return NumberFormat.getCurrencyInstance(Locale.FRANCE).format(getPaypalSales(men).add(getPaypalSales(women)));
  }

  private BigDecimal getPaypalSales(Tournament tournament) {
    return PAYPAL_UNIT_PRICE.multiply(BigDecimal.valueOf(tournament.getSubscribedTeams().size()));
  }

}
