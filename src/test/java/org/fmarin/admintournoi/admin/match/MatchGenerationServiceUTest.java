package org.fmarin.admintournoi.admin.match;

import com.google.common.collect.Lists;
import org.assertj.core.groups.Tuple;
import org.fmarin.admintournoi.admin.pool.Pool;
import org.fmarin.admintournoi.admin.round.Round;
import org.fmarin.admintournoi.subscription.TeamBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.fmarin.admintournoi.admin.pool.PoolBuilder.aPool;
import static org.fmarin.admintournoi.admin.round.RoundBuilder.aRound;

@RunWith(MockitoJUnitRunner.class)
public class MatchGenerationServiceUTest {

  @InjectMocks
  private MatchGenerationService service;

  @Test
  public void generatePoolMatches_should_generate_3_matches_AB_AC_BC() {
    // Given
    Pool pool = aPool()
      .withTeam1(TeamBuilder.aTeam().withName("A").build())
      .withTeam2(TeamBuilder.aTeam().withName("B").build())
      .withTeam3(TeamBuilder.aTeam().withName("C").build())
      .build();

    // When
    service.generatePoolMatches(pool);

    // Then
    assertThat(pool.getMatches()).hasSize(3)
      .extracting("team1.name", "team2.name", "pool")
      .contains(Tuple.tuple("A", "B", pool),
        Tuple.tuple("A", "C", pool),
        Tuple.tuple("B", "C", pool));
  }


  @Test
  public void generate_should_return_matches_for_each_pool() {
    // Given
    Round round = aRound()
      .withFieldRanges("1-2")
      .withPools(Lists.newArrayList(
        aPool()
          .withTeam1(TeamBuilder.aTeam().withName("A1").build())
          .withTeam2(TeamBuilder.aTeam().withName("B1").build())
          .withTeam3(TeamBuilder.aTeam().withName("C1").build())
          .build(),
        aPool()
          .withTeam1(TeamBuilder.aTeam().withName("A2").build())
          .withTeam2(TeamBuilder.aTeam().withName("B2").build())
          .withTeam3(TeamBuilder.aTeam().withName("C2").build())
          .build()
      ))
      .build();

    // When
    service.generate(round);

    // Then
    assertThat(round.getPools().get(0).getMatches()).hasSize(3);
    assertThat(round.getPools().get(1).getMatches()).hasSize(3);
  }
}