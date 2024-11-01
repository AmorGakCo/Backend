package com.amorgakco.backend.fixture.participant;

import com.amorgakco.backend.group.dto.LocationVerificationRequest;
import com.amorgakco.backend.participant.dto.ParticipationHistory;
import com.amorgakco.backend.participant.dto.ParticipationHistoryPagingResponse;

import java.time.LocalDateTime;
import java.util.List;

public class TestParticipantFactory {

    private static final String ADDRESS = "서울특별시 종로구 신문로1가 23";
    private static final String NAME = "AmorGakCo";
    private static final double LONGITUDE = 126.9748397;
    private static final double LATITUDE = 37.5703901;
    private static final LocalDateTime BEGIN_AT = LocalDateTime.now();
    private static final LocalDateTime END_AT = LocalDateTime.now().plusHours(3);

    public static ParticipationHistoryPagingResponse currentParticipationHistoryResponse() {
        List<ParticipationHistory> currentGroups = currentGroups();
        return ParticipationHistoryPagingResponse.builder()
                .page(0)
                .hasNext(false)
                .elementSize(currentGroups.size())
                .histories(currentGroups)
                .build();
    }

    public static ParticipationHistoryPagingResponse pastParticipationHistoryResponse() {
        List<ParticipationHistory> pastGroups = pastGroups();
        return ParticipationHistoryPagingResponse.builder()
                .page(0)
                .hasNext(false)
                .elementSize(pastGroups.size())
                .histories(pastGroups)
                .build();
    }

    private static List<ParticipationHistory> currentGroups() {
        return List.of(
                participationHistory(1L), participationHistory(2L), participationHistory(3L));
    }

    private static List<ParticipationHistory> pastGroups() {
        return List.of(
                participationHistory(4L), participationHistory(5L), participationHistory(6L));
    }

    private static ParticipationHistory participationHistory(final Long groupId) {
        return ParticipationHistory.builder()
                .groupId(groupId)
                .address(ADDRESS)
                .name(NAME)
                .endAt(END_AT)
                .beginAt(BEGIN_AT)
                .build();
    }

    public static LocationVerificationRequest locationVerificationRequest() {
        return new LocationVerificationRequest(1L, LATITUDE, LONGITUDE);
    }
}
