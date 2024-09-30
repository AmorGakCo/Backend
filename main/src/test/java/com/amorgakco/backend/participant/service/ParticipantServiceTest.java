package com.amorgakco.backend.participant.service;

import com.amorgakco.backend.fixture.group.TestGroupFactory;
import com.amorgakco.backend.group.domain.Group;
import com.amorgakco.backend.group.repository.GroupRepository;
import com.amorgakco.backend.member.domain.Member;
import com.amorgakco.backend.member.repository.MemberRepository;
import com.amorgakco.backend.participant.domain.Participant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
class ParticipantServiceTest {

    @Autowired
    private ParticipantService participantService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private GroupRepository groupRepository;

    @Test
    @DisplayName("멀티 스레드 환경에서 온도를 상승시킨 횟수 만큼 온도가 상승돼야한다.")
    void moGakCoTemperatureConcurrencyTest() throws InterruptedException {
        int memberCount = 30;
        ExecutorService executorService = Executors.newFixedThreadPool(memberCount);
        CountDownLatch latch = new CountDownLatch(memberCount);

        Member member1 = memberRepository.save(Member.builder().nickname("member1").build());
        Member member2 = memberRepository.save(Member.builder().nickname("member2").build());
        Group group = TestGroupFactory.create(member1);
        group.addParticipants(new Participant(member2));
        groupRepository.saveAndFlush(group);

        for (int i = 0; i < memberCount; i++) {
            executorService.execute(() -> {
                try {
                    participantService.increaseTemperature(1L, 1L, 2L);
                } catch (Exception e) {
                    System.out.println(e);
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
        Member member = memberRepository.findById(2L).get();
        Integer moGakCoTemperature = member.getMoGakCoTemperature();
        System.out.println("moGakCoTemperature = " + moGakCoTemperature);
    }
}