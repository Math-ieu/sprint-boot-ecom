package fr.smartshop.productservice.controller;

import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@SuppressWarnings("unused")
@Controller
public class MainController {

          @GetMapping("/")
          public String home(Model model) {
                    model.addAttribute("message", "Bienvenue sur le frontend Thymeleaf !");
                    return "layout/main"; // Correspond au fichier "templates/index.html"
          }
}
