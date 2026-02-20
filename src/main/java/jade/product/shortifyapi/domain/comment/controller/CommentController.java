package jade.product.shortifyapi.domain.comment.controller;

import jade.product.shortifyapi.domain.comment.dto.request.CommentCreateRequest;
import jade.product.shortifyapi.domain.comment.dto.response.CommentResponse;
import jade.product.shortifyapi.domain.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/articles/{articleId}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService service;

    @PostMapping
    public ResponseEntity<Long> create(
            @PathVariable Long articleId,
            @RequestBody CommentCreateRequest request
    ) {
        return ResponseEntity.ok(service.create(articleId, request));
    }

    @GetMapping
    public ResponseEntity<List<CommentResponse>> getComments(
            @PathVariable Long articleId
    ) {
        return ResponseEntity.ok(service.getComments(articleId));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> delete(
            @PathVariable Long articleId,
            @PathVariable Long commentId
    ) {
        service.delete(commentId);
        return ResponseEntity.ok().build();
    }
}