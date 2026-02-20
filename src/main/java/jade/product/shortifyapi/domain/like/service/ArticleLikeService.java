package jade.product.shortifyapi.domain.like.service;

import jade.product.shortifyapi.domain.article.entity.ArticleLike;
import jade.product.shortifyapi.domain.article.repository.ArticleLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ArticleLikeService {

    private final ArticleLikeRepository repository;

    @Transactional
    public boolean toggle(Long articleId, String clientId) {

        if (repository.existsByArticleIdAndClientId(articleId, clientId)) {
            repository.deleteByArticleIdAndClientId(articleId, clientId);
            return false; // 좋아요 취소됨
        } else {
            repository.save(ArticleLike.create(articleId, clientId));
            return true; // 좋아요 눌림
        }
    }

    public long count(Long articleId) {
        return repository.countByArticleId(articleId);
    }

    public boolean isLiked(Long articleId, String clientId) {
        return repository.existsByArticleIdAndClientId(articleId, clientId);
    }
}