package com.amorgakco.backend.fixture.group;

import com.amorgakco.backend.fixture.member.TestMemberFactory;
import com.amorgakco.backend.group.domain.Group;
import com.amorgakco.backend.group.dto.GroupBasicResponse;
import com.amorgakco.backend.group.dto.GroupLocation;
import com.amorgakco.backend.group.dto.GroupRegisterRequest;
import com.amorgakco.backend.group.dto.GroupSearchResponse;
import com.amorgakco.backend.member.domain.Member;

import java.time.LocalDateTime;
import java.util.List;

public class TestGroupFactory {

    private static final double LONGITUDE = 126.9748397;
    private static final double LATITUDE = 37.5703901;
    private static final int GROUP_CAPACITY = 3;
    private static final String ADDRESS = "서울특별시 종로구 신문로1가 23";
    private static final String DESCRIPTION = "모각코 합시다.";
    private static final String NAME = "AmorGakCo";
    private static final LocalDateTime BEGIN_AT = LocalDateTime.now();
    private static final LocalDateTime END_AT = LocalDateTime.now().plusHours(3);

    public static Group create(final Member host) {
        return Group.builder()
                .host(host)
                .groupCapacity(GROUP_CAPACITY)
                .location(TestLocationFactory.create(LONGITUDE, LATITUDE))
                .beginAt(BEGIN_AT)
                .endAt(END_AT)
                .description(DESCRIPTION)
                .name(NAME)
                .address(ADDRESS)
                .build();
    }

    public static GroupRegisterRequest createGroupRegisterRequest(
            final LocalDateTime beginAt, final LocalDateTime endAt) {
        return GroupRegisterRequest.builder()
                .groupCapacity(GROUP_CAPACITY)
                .address(ADDRESS)
                .longitude(LONGITUDE)
                .latitude(LATITUDE)
                .name(NAME)
                .description(DESCRIPTION)
                .beginAt(beginAt)
                .endAt(endAt)
                .build();
    }

    public static GroupBasicResponse createGroupBasicResponse() {
        final Member member = TestMemberFactory.create(1L);
        return GroupBasicResponse.builder()
                .hostNickname(member.getNickname())
                .groupCapacity(GROUP_CAPACITY)
                .address(ADDRESS)
                .hostImgUrl(member.getImgUrl())
                .currentParticipants(3)
                .beginAt(BEGIN_AT)
                .endAt(END_AT)
                .build();
    }

    public static GroupSearchResponse createGroupSearchResponse() {
        return GroupSearchResponse.builder()
                .locations(
                        List.of(
                                new GroupLocation(23.1111, 122.2222, 1L),
                                new GroupLocation(12.1341, 123.2222, 2L),
                                new GroupLocation(11.1111, 221.2222, 3L)))
                .build();
    }
}
