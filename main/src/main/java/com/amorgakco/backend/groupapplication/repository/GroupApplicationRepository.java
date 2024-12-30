package com.amorgakco.backend.groupapplication.repository;

import com.amorgakco.backend.group.domain.Group;
import com.amorgakco.backend.groupapplication.domain.GroupApplication;
import com.amorgakco.backend.member.domain.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupApplicationRepository extends JpaRepository<GroupApplication, Long> {

    @Query(
        "select gp from GroupApplication gp join fetch gp.group g join fetch g.host join fetch "
            + "gp.applicant where gp.group.id =:groupId and gp.applicant.id =:memberId")
    Optional<GroupApplication> findByGroupIdAndMemberId(
        @Param("groupId") Long groupId, @Param("memberId") Long memberId);

    boolean existsByGroupAndApplicant(Group group, Member applicant);

    void deleteByGroup(Group group);
}
