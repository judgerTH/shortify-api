package jade.product.shortifyapi.domain.like.controller;

import jade.product.shortifyapi.domain.like.service.ArticleLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.UUID;

@RestController
@RequestMapping("/v1/articles/{articleId}/likes")
@RequiredArgsConstructor
public class ArticleLikeController {

    private final ArticleLikeService service;

    @PostMapping("/toggle")
    public ResponseEntity<?> toggle(
            @PathVariable Long articleId,
            HttpServletRequest request,
            HttpServletResponse response
    ) {

        String clientId = getOrCreateClientId(request, response);
        boolean liked = service.toggle(articleId, clientId);

        return ResponseEntity.ok(liked);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> count(@PathVariable Long articleId) {
        return ResponseEntity.ok(service.count(articleId));
    }

    @GetMapping("/status")
    public ResponseEntity<Boolean> status(
            @PathVariable Long articleId,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        String clientId = getOrCreateClientId(request, response);
        return ResponseEntity.ok(service.isLiked(articleId, clientId));
    }

    private String getOrCreateClientId(
            HttpServletRequest request,
            HttpServletResponse response
    ) {

        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("SHORTIFY_CLIENT_ID".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }

        String clientId = UUID.randomUUID().toString();

        Cookie cookie = new Cookie("SHORTIFY_CLIENT_ID", clientId);
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60 * 24 * 365);
        response.addCookie(cookie);

        return clientId;
    }
}