package org.fmarin.admintournoi.admin.tools;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/tools")
public class GeneratorController {

  private final GeneratorService service;

  @Autowired
  public GeneratorController(GeneratorService service) {
    this.service = service;
  }

  @GetMapping("/tournaments/{tournamentId}/teams")
  public void generateTeams(@PathVariable(name = "tournamentId") Long tournamentId) {
    service.generateTeams(tournamentId);
  }
}
