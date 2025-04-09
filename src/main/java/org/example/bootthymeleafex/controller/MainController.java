package org.example.bootthymeleafex.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.bootthymeleafex.model.dto.WordForm;
import org.example.bootthymeleafex.model.entity.Word;
import org.example.bootthymeleafex.model.repository.WordRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequiredArgsConstructor
@Controller
public class MainController {
        private final WordRepository wordRepository;

    @GetMapping
    public String index(Model model, @RequestParam(required = false) String message) {
//        Word word = new Word();
//        word.setText("강아지");
//        wordRepository.save(word);
        model.addAttribute("words", wordRepository.findAll());
//        model.addAttribute("data","타임리프 테스트");
//        model.addAttribute("message",message);
        model.addAttribute("wordForm", new WordForm());
        return "index"; // foward
    }
    @PostMapping("/word")
    public String word(WordForm wordForm,RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("message", "끝말잇기 추가");
        Word word = new Word();
        word.setText(wordForm.getWord());
        wordRepository.save(word);
        return "redirect:/";
    }


    @PostMapping("/reset")
    public String resetWords(RedirectAttributes redirectAttributes) {
        wordRepository.deleteAll(); // 다 날아감.
        // Model을 쓸 수 없는 이유 -> foward
//        redirectAttributes.addAttribute("message","전체삭제");
        // 주소창 통해서 전달했기에 parameter로 해서 받아서 Model로 넣어줘야 했다면..
//        redirectAttributes.addFlashAttribute("message2", "전부 Delete");
        redirectAttributes.addAttribute("message","끝말잇기 기록 reset");
        //바로 model로 넣어준다.
        // message -> Parameter
        // 주소창에 노출이 된다.
        return "redirect:/";
    }
}
