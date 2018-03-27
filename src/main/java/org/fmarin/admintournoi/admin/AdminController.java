package org.fmarin.admintournoi.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class AdminController {

    private final DashboardService dashboardService;

    @Autowired
    public AdminController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/admin")
    public ModelAndView index() {
      return new ModelAndView("admin_index", dashboardService.getCurrentSubscriptionsStats());
    }

    @GetMapping("/login")
    public ModelAndView login() {
      return new ModelAndView("login");
    }
}
