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

    private static final double longitude = 126.9748397;
    private static final double latitude = 37.5703901;
    private static final int groupCapacity = 3;
    private static final String address = "서울특별시 종로구 신문로1가 23";
    private static final String description = "모각코 합시다.";
    private static final String name = "AmorGakCo";
    private static final LocalDateTime beginAt = LocalDateTime.now();
    private static final LocalDateTime endAt = LocalDateTime.now().plusHours(3);

    public static Group create(final Member host) {
        return Group.builder()
                .host(host)
                .groupCapacity(groupCapacity)
                .location(TestLocationFactory.create(longitude, latitude))
                .duration(TestDurationFactory.create(3))
                .description(description)
                .name(name)
                .address(address)
                .build();
    }

    public static GroupRegisterRequest createGroupRegisterRequest(
            final LocalDateTime beginAt, final LocalDateTime endAt) {
        return GroupRegisterRequest.builder()
                .groupCapacity(groupCapacity)
                .address(address)
                .longitude(longitude)
                .latitude(latitude)
                .name(name)
                .description(description)
                .beginAt(beginAt)
                .endAt(endAt)
                .build();
    }

    public static GroupBasicResponse createGroupBasicResponse() {
        final Member member = TestMemberFactory.create(1L);
        return GroupBasicResponse.builder()
                .hostNickname(member.getNickname())
                .groupCapacity(groupCapacity)
                .address(address)
                .hostImgUrl(member.getImgUrl())
                .currentParticipants(3)
                .beginAt(beginAt)
                .endAt(endAt)
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
