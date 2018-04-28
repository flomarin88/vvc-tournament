package org.fmarin.admintournoi.admin;

import com.google.common.collect.Maps;
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

  private final BigDecimal PAYPAL_UNIT_PRICE = new BigDecimal(31.63);

  @Autowired
  public DashboardService(TournamentRepository tournamentRepository) {
    this.tournamentRepository = tournamentRepository;
  }

  public Map<String, Object> getCurrentSubscriptionsStats() {
    int currentYear = TimeMachine.now().getYear();
    Tournament menTournament = tournamentRepository.findByYearAndGender(currentYear, Gender.MEN);
    Tournament womenTournament = tournamentRepository.findByYearAndGender(currentYear, Gender.WOMEN);
    Map<String, Object> result = Maps.newHashMap();
    result.putAll(getCurrentSubscriptionsStats(menTournament));
    result.putAll(getCurrentSubscriptionsStats(womenTournament));
    result.put("paypal_sales_total", getPaypalSalesTotal(menTournament, womenTournament));
    return result;
  }

  Map<String, Object> getCurrentSubscriptionsStats(Tournament tournament) {
    Map<String, Object> result = Maps.newHashMap();
    String prefix = tournament.getGender().name().toLowerCase();
    result.put(prefix + "_teams_limit", tournament.getTeamLimit());
    result.put(prefix + "_teams_subscribed", tournament.getSubscribedTeams().size());
    return result;
  }

  private String getPaypalSalesTotal(Tournament men, Tournament women) {
    return NumberFormat.getCurrencyInstance(Locale.FRANCE).format(getPaypalSales(men).add(getPaypalSales(women)));
  }

  private BigDecimal getPaypalSales(Tournament tournament) {
    return PAYPAL_UNIT_PRICE.multiply(BigDecimal.valueOf(tournament.getSubscribedTeams().size()));
  }

}
