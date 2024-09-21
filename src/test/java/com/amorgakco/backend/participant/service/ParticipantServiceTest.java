package com.amorgakco.backend.participant.service;

import com.amorgakco.backend.fixture.group.TestGroupFactory;
import com.amorgakco.backend.group.domain.Group;
import com.amorgakco.backend.group.repository.GroupRepository;
import com.amorgakco.backend.member.domain.Member;
import com.amorgakco.backend.member.repository.MemberRepository;
import com.amorgakco.backend.participant.domain.Participant;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

@SpringBootTest
class ParticipantServiceTest {

    @Autowired
    private ParticipantService participantService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private GroupRepository groupRepository;


    @Test
    void multithreadTest() throws InterruptedException {
        Member member1 = memberRepository.save(Member.builder().nickname("member1").build());
        Member member2 = memberRepository.save(Member.builder().nickname("member2").build());
        Group group = TestGroupFactory.create(member1);
        group.addParticipants(new Participant(member2));
        groupRepository.saveAndFlush(group);

        int memberCount = 20;

        ExecutorService executorService = Executors.newFixedThreadPool(memberCount);
        CountDownLatch latch = new CountDownLatch(memberCount);
        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failCount = new AtomicInteger();

        for (int i = 0; i < memberCount; i++) {
            executorService.execute(() -> {
                try {
                    participantService.upTemperature(1L, 1L, 2L);
                    successCount.incrementAndGet();
                } catch (Exception e) {
                    System.out.println("e.getMessage() = " + e.getMessage());
                    failCount.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        System.out.println("failCount = " + failCount);
        System.out.println("successCount = " + successCount);

        Member member = memberRepository.findById(2L).get();
        Integer moGakCoTemperature = member.getMoGakCoTemperature();
        Assertions.assertThat(moGakCoTemperature).isEqualTo(memberCount);
    }
}