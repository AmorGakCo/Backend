package com.amorgakco.backend.notification.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum NotificationType {
    PARTICIPATION_REQUEST("참여 요청", "%s 님께서 %s 모각코에 참여하길 원합니다."),
    PARTICIPATION_APPROVED("참여 허가", "%s 모각코 참여가 허가됐습니다."),
    PARTICIPATION_REJECTED("참여 거절", "%s 모각코 참여가 거절됐습니다."),
    PARTICIPATION_TARDINESS("모각코 지각", "%s 님께서 %s 모각코에 %d분 지각 알림을 보냈습니다."),
    PARTICIPATION_WITHDRAW("모각코 탈퇴", "%s 님께서 %s 모각코를 탈퇴하셨습니다.");
    private final String title;
    private final String content;
}
