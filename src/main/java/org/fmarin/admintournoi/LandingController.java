package org.fmarin.admintournoi;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class LandingController {
    
    @GetMapping("/")
    public ModelAndView index() {
        return new ModelAndView("landing");
    }
    
}
