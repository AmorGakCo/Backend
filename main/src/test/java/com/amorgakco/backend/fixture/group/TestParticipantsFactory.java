package com.amorgakco.backend.fixture.group;

import com.amorgakco.backend.member.domain.Member;
import com.amorgakco.backend.participant.domain.Participant;

public class TestParticipantsFactory {

    public static Participant create(final Member participant) {
        return new Participant(participant);
    }
}
