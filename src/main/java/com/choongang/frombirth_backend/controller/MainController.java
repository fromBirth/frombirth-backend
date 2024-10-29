package com.choongang.frombirth_backend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/main")
    public String mainPage(Model model) {
        model.addAttribute("pageName", "main"); // pageName을 모델에 추가
        return "view"; // view.html을 반환
    }
}
