package com.amorgakco.backend.fixture.group;

import com.amorgakco.backend.groupparticipant.domain.GroupParticipant;
import com.amorgakco.backend.member.domain.Member;

public class TestParticipantsFactory {

    public static GroupParticipant create(final Member participant) {
        return new GroupParticipant(participant);
    }
}
