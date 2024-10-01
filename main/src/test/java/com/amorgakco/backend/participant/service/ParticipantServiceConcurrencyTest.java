package com.amorgakco.backend.participant.service;

import com.amorgakco.backend.fixture.group.TestGroupFactory;
import com.amorgakco.backend.fixture.member.TestMemberFactory;
import com.amorgakco.backend.group.domain.Group;
import com.amorgakco.backend.group.repository.GroupRepository;
import com.amorgakco.backend.member.domain.Member;
import com.amorgakco.backend.member.repository.MemberRepository;
import com.amorgakco.backend.notification.infrastructure.NotificationPublisherFacade;
import com.amorgakco.backend.participant.domain.Participant;
import com.amorgakco.backend.participant.repository.ParticipantRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ParticipantServiceConcurrencyTest {

    @Autowired
    private ParticipantService participantService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private ParticipantRepository participantRepository;
    @MockBean
    private NotificationPublisherFacade notificationPublisherFacade;

    @AfterEach
    void clear() {
        groupRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("멀티 스레드 환경에서 온도를 상승시킨 횟수 만큼 온도가 상승돼야한다.")
    void increaseTemperatureConcurrencyTest() throws InterruptedException {
        //given
        Member requestMember = TestMemberFactory.createEntity();
        Member targetMember = TestMemberFactory.createEntity();
        memberRepository.save(requestMember);
        memberRepository.save(targetMember);
        Group group = TestGroupFactory.create(requestMember);
        group.addParticipants(new Participant(targetMember));
        groupRepository.save(group);
        int threadCount = 15;

        //when
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        for (int i = 0; i < threadCount; i++) {
            executorService.execute(() -> {
                try {
                    participantService.increaseTemperature(group.getId(), requestMember.getId(), targetMember.getId());
                } catch (Exception e) {
                    System.out.println(e);
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
        //then
        Member updatedMember = memberRepository.findById(targetMember.getId()).get();
        Integer temperature = updatedMember.getMoGakCoTemperature();
        assertThat(temperature).isEqualTo(threadCount);
    }

    @Test
    @DisplayName("멀티 스레드 환경에서 온도를 상승시킨 횟수 만큼 온도가 감소돼야한다.")
    void decreaseTemperatureConcurrencyTest() throws InterruptedException {
        //given
        Member requestMember = TestMemberFactory.createEntity();
        Member targetMember = TestMemberFactory.createEntity();
        memberRepository.save(requestMember);
        memberRepository.save(targetMember);
        Group group = TestGroupFactory.create(requestMember);
        group.addParticipants(new Participant(targetMember));
        groupRepository.save(group);
        int threadCount = 15;

        //then
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.execute(() -> {
                try {
                    participantService.decreaseTemperature(group.getId(), requestMember.getId(), targetMember.getId());
                } catch (Exception e) {
                    System.out.println(e);
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
        Member updatedMember = memberRepository.findById(targetMember.getId()).get();
        Integer temperature = updatedMember.getMoGakCoTemperature();
        assertThat(temperature).isEqualTo(-threadCount);
    }
}