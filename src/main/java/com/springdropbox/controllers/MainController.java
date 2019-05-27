package com.springdropbox.controllers;

import com.springdropbox.configs.CustomAuthenticationSuccessHandler;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpSession;

@Controller
public class MainController {

    @GetMapping("/")
    public String showLoginPage(Model model) {
        return "redirect:/login";
    }
}
