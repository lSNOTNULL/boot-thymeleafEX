package org.example.bootthymeleafex.model.repository;

import org.example.bootthymeleafex.model.entity.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// UUID 썼으므로 long 아니라 String.
@Repository
public interface WordRepository extends JpaRepository<Word, String> {

    // findAll -> PK (Primary Key : 기본키)
    List<Word> findAllByOrderByCreatedAtDesc(); // 최신부터 정렬
    // findAllByOrderByCreatedAtDesc는 JPA가 메서드 이름만 보고 자동으로 'createdAt' 기준으로 내림차순 정렬하는 SQL을 만들어 실행해줍니다
}
