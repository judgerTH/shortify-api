package jade.product.shortifyapi.domain.daily.service;

import jade.product.shortifyapi.domain.article.entity.NewsInsight;
import jade.product.shortifyapi.domain.article.repository.NewsInsightRepository;
import jade.product.shortifyapi.domain.daily.dto.response.NewsInsightDto;
import jade.product.shortifyapi.global.error.ErrorCode;
import jade.product.shortifyapi.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DailyNewsInsightService {

    private final NewsInsightRepository newsInsightRepository;

    public NewsInsightDto getYesterdayInsight() {

        List<NewsInsight> insights =
                newsInsightRepository.findTop2ByOrderByCreatedAtDesc();

        if (insights.isEmpty()) {
            throw new CustomException(ErrorCode.NOT_FOUND);
        }

        NewsInsight today = insights.get(0);
        NewsInsight yesterday =
                insights.size() > 1 ? insights.get(1) : null;

        return NewsInsightDto.of(today, yesterday);
    }
}
