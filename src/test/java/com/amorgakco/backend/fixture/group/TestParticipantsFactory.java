package com.amorgakco.backend.fixture.group;

import com.amorgakco.backend.group.domain.Participant;
import com.amorgakco.backend.member.domain.Member;

public class TestParticipantsFactory {

    public static Participant create(final Member participant) {
        return new Participant(participant);
    }
}
