package com.amorgakco.backend.fixture.group;

import com.amorgakco.backend.group.domain.Group;
import com.amorgakco.backend.member.domain.Member;

public class TestGroupFactory {

    public static Group create(final Member host) {
        return Group.builder()
                .host(host)
                .groupCapacity(3)
                .location(TestLocationFactory.create())
                .duration(TestDurationFactory.create(3))
                .description("모각코 합시다.")
                .name("AmorGakCo")
                .address("서울특별시 종로구 신문로1가 23")
                .build();
    }
}
