package org.example.bootthymeleafex.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.bootthymeleafex.model.entity.Word;
import org.example.bootthymeleafex.model.repository.WordRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
public class MainController {
        private final WordRepository wordRepository;

    @GetMapping
    public String index(Model model) {
        Word word = new Word();
        word.setText("강아지");
        wordRepository.save(word);
        model.addAttribute("word", wordRepository.findAll());
        model.addAttribute("data","타임리프 테스트");
        return "index";
    }
}
