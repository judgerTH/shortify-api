package jade.product.shortifyapi.domain.daily.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class DailyTimelineResponse {

    private LocalDate date;
    private LocalTime baseTime;
    private List<TimeGroupDto> groups;
}
