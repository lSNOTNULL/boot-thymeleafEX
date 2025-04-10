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
//                  // 1. reverse 비추 / 2. a,b 순서 바꾸기 / 3. () -> a 에 -를 붙인다 (boolean이면 not(!)을 하고)
//                  //
//                  .toList());
          // 쿼리 만들기 (WordRepository) : 기준 createdAt
        model.addAttribute("words",wordRepository.findAllByOrderByCreatedAtDesc());
        //가져온 단어 목록(words)을 Model이라는 데이터 바구니에 담습니다.

//        model.addAttribute("data","타임리프 테스트");
//        model.addAttribute("message",message);
        // 타임리프에서 폼을 이미 정의된 걸로 쓰려면 Model을 통해서 전달해야함.


        // model.addAttribute("wordForm", new WordForm()); 처럼 빈 폼 객체도 미리 만들어서 Model에 담아주면, 화면에서 해당 객체와 입력 필드를 쉽게 연결(th:object, th:field)할 수 있습니다.
        model.addAttribute("wordForm", new WordForm());

        return "index"; // foward
        //index.html이라는 화면 설계도(Thymeleaf)에게 이 바구니를 전달하며 화면을 만들어달라고 요청합니다.
    }
    @PostMapping("/word")
    public String word(WordForm wordForm,RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("message", "끝말잇기 추가");
        // RedirectAttributes는 리다이렉트할 때 딱 한 번만 사용할 메시지("끝말잇기 추가")를 전달하는 용도로 사용됩니다. (addFlashAttribute) 마치 포스트잇처럼 잠깐 붙였다 떼는 느낌이죠.
        Word word = new Word();
        word.setText(wordForm.getWord());
        // Spring이 똑똑하게 폼 데이터를 WordForm 객체에 자동으로 담아줍니다. (wordForm)
        // Word 객체(Entity)를 새로 만들고, WordForm에서 받은 단어 텍스트를 설정합니다.

        wordRepository.save(word);
        // WordRepository.save(word)를 호출하면 JPA가 알아서 DB에 새로운 단어 데이터를 INSERT 해줍니다.
        return "redirect:/";
        // return "redirect:/";는 "작업 끝났으니 다시 메인 화면(/)으로 가세요"라고 브라우저에게 지시하는 것입니다.
    }

    @PostMapping("/update")
    @Transactional // 최종 해결
    // 이 메서드 내의 DB 작업(조회 후 저장)을 하나의 묶음(트랜잭션)으로 처리해줍니다. 중간에 문제가 생기면 모든 변경 사항을 되돌려 데이터 일관성을 지켜줍니다. (작업 꾸러미 비유: 꾸러미 속 작업 중 하나라도 실패하면 꾸러미 전체를 버리는 것)

    public String updateWord(@ModelAttribute UpdateWordForm form, RedirectAttributes redirectAttributes) {
        // @ModelAttribute는 폼 데이터를 UpdateWordForm 객체에 담아줍니다.

        // JPA는 업데이트용 메서드나 기능이 따로 없음
        // 수정용이 따로 없음. -> 교체 개념임 => put <-> patch : 멱등성
        Word oldWord = wordRepository.findById(form.getUuid()).orElseThrow();
        // DB에서 해당 Uuid의 Word 객체를 찾아옵니다. .orElseThrow()는 혹시 ID에 해당하는 단어가 없으면 예외를 발생시키라는 의미입니다.
        oldWord.setText(form.getNewWord());
        // 찾아온 Word 객체(oldWord)의 텍스트를 form.getNewWord()로 받은 새 단어로 변경합니다. (oldWord.setText(...))
        wordRepository.save(oldWord);
        redirectAttributes.addFlashAttribute("message","%s 교체완료".formatted(oldWord.getUuid()));
        return "redirect:/";
    }

    @PostMapping("/delete")
    public String delete(@RequestParam("id") String uuid, RedirectAttributes redirectAttributes) {
        // @RequestParam("id")는 요청 파라미터 중 이름이 'id'인 값(uuid)을 가져옵니다.
        // index.html 의 th:value="${word.getUuid()}" -> delete 메서드의 String uuid
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
        // 리다이렉트될 URL 뒤에 쿼리 파라미터(?message=...)를 붙여서 데이터를 전달합니다. addFlashAttribute와 달리 URL에 노출됩니다.

        //바로 model로 넣어준다.
        // message -> Parameter
        // 주소창에 노출이 된다.
        return "redirect:/";
    }
}
