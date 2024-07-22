package com.amorgakco.backend.group.repository;

import com.amorgakco.backend.group.domain.Group;

import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    @Query("select g from Group g join fetch g.host where g.id = :groupId ")
    Optional<Group> findByIdWithHost(final Long groupId);

    @Query("select g from Group g where st_dwithin(g.location,:point,:radius)")
    List<Group> findByLocationWithRadius(Point point, double radius);

    @Query("select g from Group g join fetch g.participants where g.id=:groupId")
    Optional<Group> findByIdWithParticipants(Long groupId);
}
