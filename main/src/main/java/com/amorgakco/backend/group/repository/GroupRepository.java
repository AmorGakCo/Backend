package com.amorgakco.backend.group.repository;

import com.amorgakco.backend.group.domain.Group;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {

    @Query("select g from Group g join fetch g.host where g.id = :groupId")
    Optional<Group> findByIdWithHost(Long groupId);

    @Query("select g from Group g where g.location.cellToken in :cellTokens ")
    List<Group> findByCellToken(List<String> cellTokens);
}
