package com.amorgakco.backend.participant.service.mapper;

import com.amorgakco.backend.group.domain.Group;
import com.amorgakco.backend.participant.domain.Participant;
import com.amorgakco.backend.participant.dto.ParticipationHistory;
import com.amorgakco.backend.participant.dto.ParticipationHistoryResponse;

import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ParticipantMapper {

    public ParticipationHistoryResponse toParticipationHistoryResponse(
            final Slice<Participant> participantSlice) {
        return ParticipationHistoryResponse.builder()
                .activatedGroup(getInactivatedGroup(participantSlice.getContent()))
                .InactivatedGroup(getActivatedGroup(participantSlice.getContent()))
                .elementSize(participantSlice.getSize())
                .hasNext(participantSlice.hasNext())
                .page(participantSlice.getNumber())
                .build();
    }

    private List<ParticipationHistory> getInactivatedGroup(final List<Participant> participants) {
        return participants.stream()
                .filter(p -> p.getGroup().isInactivatedGroup())
                .map(p -> toParticipationHistory(p.getGroup()))
                .toList();
    }

    private List<ParticipationHistory> getActivatedGroup(final List<Participant> participants) {
        return participants.stream()
                .filter(p -> p.getGroup().isActivatedGroup())
                .map(p -> toParticipationHistory(p.getGroup()))
                .toList();
    }

    public ParticipationHistory toParticipationHistory(final Group group) {
        return ParticipationHistory.builder()
                .groupId(group.getId())
                .name(group.getName())
                .beginAt(group.getDuration().getBeginAt())
                .endAt(group.getDuration().getEndAt())
                .address(group.getAddress())
                .build();
    }
}
