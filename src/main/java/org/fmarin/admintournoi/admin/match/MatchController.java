package org.fmarin.admintournoi.admin.match;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/admin/matches")
public class MatchController {

    private final MatchRepository matchRepository;

    @Autowired
    public MatchController(MatchRepository matchRepository) {
        this.matchRepository = matchRepository;
    }

    @PutMapping("/{matchId}")
    public ResponseEntity update(@PathVariable(name = "matchId") Long matchId, @Valid @ModelAttribute ScorePutView body) {
        Match match = matchRepository.findOne(matchId);
        match.setScoreTeam1(body.getTeamScore1());
        match.setScoreTeam2(body.getTeamScore2());
        matchRepository.save(match);
        return ResponseEntity.ok("{\"result\":\"OK\"}");
    }

}
