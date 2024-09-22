package com.amorgakco.backend.participant.service;

import com.amorgakco.backend.group.repository.GroupRepository;
import com.amorgakco.backend.member.domain.Member;
import com.amorgakco.backend.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
//@ContextConfiguration(initializers = {TestContainerConfig.class})
class ParticipantServiceTest {

    @Autowired
    private ParticipantService participantService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private GroupRepository groupRepository;

    @Test
    @DisplayName("")
    void test() {
        Member member1 = memberRepository.save(Member.builder().nickname("member1").build());
    }


//    @Test
//    @DisplayName("멀티 스레드 환경에서 온도를 상승시킨 횟수 만큼 온도가 상승돼야한다.")
//    void moGakCoTemperatureConcurrencyTest() throws InterruptedException {
//        Member member1 = memberRepository.save(Member.builder().nickname("member1").build());
//        Member member2 = memberRepository.save(Member.builder().nickname("member2").build());
//        Group group = TestGroupFactory.create(member1);
//        group.addParticipants(new Participant(member2));
//        groupRepository.saveAndFlush(group);
//
//        int memberCount = 20;
//
//        ExecutorService executorService = Executors.newFixedThreadPool(memberCount);
//        CountDownLatch latch = new CountDownLatch(memberCount);
//
//        for (int i = 0; i < memberCount; i++) {
//            executorService.execute(() -> {
//                try {
//                    participantService.upTemperature(1L, 1L, 2L);
//                } catch (Exception e) {
//                } finally {
//                    latch.countDown();
//                }
//            });
//        }
//
//        latch.await();
//
//        Member member = memberRepository.findById(2L).get();
//        Integer moGakCoTemperature = member.getMoGakCoTemperature();
//        Assertions.assertThat(moGakCoTemperature).isEqualTo(memberCount);
//    }
}