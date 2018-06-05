package org.fmarin.admintournoi.admin.match;

import com.google.common.collect.Lists;
import org.fmarin.admintournoi.admin.pool.Pool;
import org.fmarin.admintournoi.admin.round.Round;
import org.fmarin.admintournoi.admin.round.RoundRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin")
public class MatchController {

    private final MatchRepository matchRepository;
    private final RoundRepository roundRepository;

    @Autowired
    public MatchController(MatchRepository matchRepository, RoundRepository roundRepository) {
        this.matchRepository = matchRepository;
        this.roundRepository = roundRepository;
    }

    @PutMapping("/matches/{matchId}")
    public ResponseEntity update(@PathVariable(name = "matchId") Long matchId, @Valid @ModelAttribute ScorePutView body) {
        Match match = matchRepository.findOne(matchId);
        match.setScoreTeam1(body.getTeamScore1());
        match.setScoreTeam2(body.getTeamScore2());
        matchRepository.save(match);
        return ResponseEntity.ok("{\"result\":\"OK\"}");
    }

    @GetMapping("/rounds/{roundId}/matches/papers")
    public ModelAndView getPaperMatches(@PathVariable(name = "roundId") Long roundId) {
        Round round = roundRepository.findOne(roundId);
        ArrayList<String> elements = Lists.newArrayList(round.getTournament().getFullName(), round.getFullName());
        String roundName = String.join(" / ", elements);
        List<MatchPaper> matchPapers = round.getPools().stream()
                .sorted(Comparator.comparing(Pool::getPosition))
                .map(pool -> new MatchPaper(roundName, pool.getPosition(), pool.getField(),
                        pool.getTeam1().getName(), pool.getTeam2().getName(), pool.getTeam3().getName()))
                .collect(Collectors.toList());
        return new ModelAndView("match_paper", "papers", matchPapers);
    }


}
