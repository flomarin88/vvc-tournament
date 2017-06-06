package org.fmarin.admintournoi.admin.ranking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class RankingController {

    private final RankingService rankingService;

    @Autowired
    public RankingController(RankingService rankingService) {
        this.rankingService = rankingService;
    }

    @GetMapping("/pools/{id}/rankings")
    public ResponseEntity getPoolRankings(@PathVariable(name = "id") Long poolId) {
        return ResponseEntity.ok(rankingService.getPoolRanking(poolId));
    }
}
