package com.epam.rd.autocode.assessment.appliances.controller;

import com.epam.rd.autocode.assessment.appliances.aspect.Loggable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class IndexController {
    @Loggable
    @GetMapping({"", "index"})
    public String home() {
        return "redirect:/catalog";
    }

    @GetMapping("login")
    public String login() {
        return "login";
    }
}
