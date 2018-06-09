package org.fmarin.admintournoi.admin.match;

import com.google.common.collect.Lists;
import org.fmarin.admintournoi.admin.round.Round;
import org.fmarin.admintournoi.admin.round.RoundRepository;
import org.fmarin.admintournoi.admin.round.RoundType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.LinkedList;
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
    if (RoundType.POOL.equals(round.getType())) {
      List<MatchPaper> matchPapers = round.getPools().stream()
        .map(pool -> new MatchPaper(roundName, pool.getPosition(), pool.getField(),
          pool.getTeam1().getName(), pool.getTeam2().getName(), pool.getTeam3().getName()))
        .collect(Collectors.toList());
      return new ModelAndView("pool_paper", "pages", byPage(matchPapers));
    } else {
      List<MatchPaper> matchPapers = round.getPools().stream()
        .map(pool -> new MatchPaper(roundName, pool.getPosition(), pool.getField(),
          pool.getTeam1().getName(), pool.getTeam2().getName(), null))
        .collect(Collectors.toList());
      return new ModelAndView("match_paper", "pages", byPage(matchPapers));
    }
  }

  private List<MatchPage> byPage(List<MatchPaper> papers) {
    LinkedList<MatchPage> pages = Lists.newLinkedList();
    for (int i = 0; i < papers.size(); i++) {
      MatchPage page;
      if (pages.isEmpty() || i % 2 == 0) {
        page = new MatchPage();
        pages.add(page);
      }
      else {
        page = pages.getLast();
      }
      page.getPapers().add(papers.get(i));
    }
    return pages;
  }
}
