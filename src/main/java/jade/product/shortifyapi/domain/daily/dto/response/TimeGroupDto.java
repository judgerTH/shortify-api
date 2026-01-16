package jade.product.shortifyapi.domain.daily.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class TimeGroupDto {

    private String timeRange;
    private List<ArticleDto> articles;
}
