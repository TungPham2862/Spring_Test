package com.example.springquiz.controller;

import com.example.springquiz.model.dto.QuizDTO;
import com.example.springquiz.service.impl.QuizService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@AllArgsConstructor
@RequestMapping("/quizzes")
public class QuizController {
    QuizService quizService;

    @PostMapping
    public ResponseEntity<?> createQuestion(@Valid @RequestBody QuizDTO dto, UriComponentsBuilder uriComponentsBuilder) {
        int quizId = quizService.createNewQuiz(dto);
        UriComponents uriComponents = uriComponentsBuilder
                .path("/quizzes/{id}")
                .buildAndExpand(quizId);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(uriComponents.toUri());

        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @GetMapping()
    public ResponseEntity<?> getAllQuizzes() {
        return ResponseEntity.ok(quizService.getAllQuizzes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findQuizById(@PathVariable int id) {
        return ResponseEntity.ok(quizService.getQuizById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateQuizById(@PathVariable int id, @Valid @RequestBody QuizDTO dto) {
        quizService.updateQuiz(id, dto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAnswerById(@PathVariable int id) {
        quizService.deleteQuizById(id);
        return ResponseEntity.ok().build();
    }
}
