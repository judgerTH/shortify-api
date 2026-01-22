package jade.product.shortifyapi.domain.daily.sse;

/**
 * 일일 타임라인 처리 단계
 *
 * COLLECTING : 기사 수집/요약이 진행 중인 상태
 * DEPLOYED   : 해당 시간대 기사 요약이 완료되어 배포 가능한 상태
 */
public enum TimelinePhase {

    COLLECTING,
    DEPLOYED
}
