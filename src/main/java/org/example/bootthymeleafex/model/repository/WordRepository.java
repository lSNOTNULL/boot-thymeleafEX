package org.example.bootthymeleafex.model.repository;

import org.example.bootthymeleafex.model.entity.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// UUID 썼으므로 long 아니라 String.
@Repository
public interface WordRepository extends JpaRepository<Word, String> {
}
