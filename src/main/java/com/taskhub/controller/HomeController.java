package com.taskhub.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    
    @GetMapping("/")
    public String home() {
        // Inoltra alla pagina index.html presente in src/main/resources/static/
        return "forward:/index.html";
    }
}
