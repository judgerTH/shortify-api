package jade.product.shortifyapi.domain.daily.dto.response;

import jade.product.shortifyapi.domain.article.entity.NewsInsight;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class NewsInsightDto {
    private Long id;
    private Integer positivity;
    private Integer stability;
    private Integer tension;
    private String summary;

    // 전일 대비 변화값
    private Integer positivityDiff;
    private Integer stabilityDiff;
    private Integer tensionDiff;

    private LocalDateTime createdAt;

    public static NewsInsightDto of(
            NewsInsight today,
            NewsInsight yesterday
    ) {

        return NewsInsightDto.builder()
                .id(today.getId())
                .positivity(today.getPositivity())
                .stability(today.getStability())
                .tension(today.getTension())

                .positivityDiff(
                        calculateDiff(today.getPositivity(),
                                yesterday != null ? yesterday.getPositivity() : null)
                )
                .stabilityDiff(
                        calculateDiff(today.getStability(),
                                yesterday != null ? yesterday.getStability() : null)
                )
                .tensionDiff(
                        calculateDiff(today.getTension(),
                                yesterday != null ? yesterday.getTension() : null)
                )

                .summary(today.getSummary())
                .createdAt(today.getCreatedAt())
                .build();
    }

    private static Integer calculateDiff(Integer today, Integer yesterday) {

        if (yesterday == null) {
            return 0;
        }

        return today - yesterday;
    }
}
