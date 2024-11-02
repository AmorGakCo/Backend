package com.amorgakco.backend.groupparticipant.service.mapper;

import com.amorgakco.backend.group.domain.Group;
import com.amorgakco.backend.groupparticipant.domain.GroupParticipant;
import com.amorgakco.backend.groupparticipant.dto.GroupParticipationHistory;
import com.amorgakco.backend.groupparticipant.dto.GroupParticipationHistoryResponse;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GroupParticipantMapper {

    public GroupParticipationHistoryResponse toParticipationHistoryPagingResponse(
            final Slice<GroupParticipant> participantSlice) {
        return GroupParticipationHistoryResponse.builder()
                .histories(getParticipationHistories(participantSlice.getContent()))
                .elementSize(participantSlice.getSize())
                .hasNext(participantSlice.hasNext())
                .page(participantSlice.getNumber())
                .build();
    }

    private List<GroupParticipationHistory> getParticipationHistories(final List<GroupParticipant> groupParticipants) {
        return groupParticipants.stream()
                .map(p -> toParticipationHistory(p.getGroup()))
                .toList();
    }

    public GroupParticipationHistory toParticipationHistory(final Group group) {
        return GroupParticipationHistory.builder()
                .groupId(group.getId())
                .name(group.getName())
                .beginAt(group.getDuration().getBeginAt())
                .endAt(group.getDuration().getEndAt())
                .address(group.getAddress())
                .build();
    }
}
