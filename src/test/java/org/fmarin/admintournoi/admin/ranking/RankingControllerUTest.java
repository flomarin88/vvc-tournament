package org.fmarin.admintournoi.admin.ranking;

import com.google.common.collect.Lists;
import org.fmarin.admintournoi.admin.pool.Pool;
import org.fmarin.admintournoi.admin.pool.PoolRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RankingControllerUTest {

  private RankingController controller;

  @Mock
  private PoolRepository mockedPoolRepository;

  @Mock
  private Pool mockedPool;

  @Before
  public void setUp() {
    controller = new RankingController(null, mockedPoolRepository);
  }

  @Test
  public void getPoolRankings() {
    // Given
    when(mockedPoolRepository.findOne(1L)).thenReturn(mockedPool);
    List<Ranking> rankings = Lists.newArrayList(RankingBuilder.aRanking().build());
    when(mockedPool.getRankings()).thenReturn(rankings);

    // When
    ResponseEntity result = controller.getPoolRankings(1L);

    // Then
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(result.getBody()).isEqualTo(rankings);
  }
}