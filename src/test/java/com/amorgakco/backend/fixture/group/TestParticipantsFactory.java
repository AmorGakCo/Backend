package com.amorgakco.backend.fixture.group;

import com.amorgakco.backend.group.domain.Participants;
import com.amorgakco.backend.member.domain.Member;

public class TestParticipantsFactory {

    public static Participants create(final Member participant) {
        return new Participants(participant);
    }
}
