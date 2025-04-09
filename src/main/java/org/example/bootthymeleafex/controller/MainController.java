package org.example.bootthymeleafex.controller;

import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.bootthymeleafex.model.dto.UpdateWordForm;
import org.example.bootthymeleafex.model.dto.WordForm;
import org.example.bootthymeleafex.model.entity.Word;
import org.example.bootthymeleafex.model.repository.WordRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
//        model.addAttribute("words", wordRepository.findAll());
//          model.addAttribute("words",wordRepository.findAll().stream()
//                  .sorted((a,b) -> a.getCreatedAt().compareTo(b.getCreatedAt()))
//                  // (스트림화) 앞,뒤를 불러와서 생성일자 비교 후 정렬 후 리스트로 만듦
//                  // 뒤집는 방법
//                  // 1. reverse 비추 / 2. a,b 순서 바꾸기 / 3. -를 붙인다 (boolean이면 not(!)을 하고)
//                  //
//                  .toList());
          // 쿼리 만들기 (WordRepository) : 기준 createdAt
        model.addAttribute("words",wordRepository.findAllByOrderByCreatedAtDesc());

//        model.addAttribute("data","타임리프 테스트");
//        model.addAttribute("message",message);
        // 타임리프에서 폼을 이미 정의된 걸로 쓰려면 Model을 통해서 전달해야함.
        model.addAttribute("wordForm", new WordForm()); // 주입!
        model.addAttribute("updateWordForm", new UpdateWordForm());
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

    @PostMapping("/update")
    @Transactional // 최종 해결
    public String updateWord(@ModelAttribute UpdateWordForm form, RedirectAttributes redirectAttributes) {
        // JPA는 업데이트용 메서드나 기능이 따로 없음
        // 수정용이 따로 없음. -> 교체 개념임 => put <-> patch : 멱등성
        Word oldWord = wordRepository.findById(form.getUuid()).orElseThrow();
        oldWord.setText(form.getNewWord());
        wordRepository.save(oldWord);
        redirectAttributes.addFlashAttribute("message","%s 교체완료".formatted(oldWord.getUuid()));
        return "redirect:/";
    }

    @PostMapping("/delete")
    public String delete(@RequestParam("id") String uuid, RedirectAttributes redirectAttributes) {
        wordRepository.deleteById(uuid);
        redirectAttributes.addFlashAttribute("message","정상 삭제 %s".formatted(uuid));
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
