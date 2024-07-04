package com.amorgakco.backend.member.domain;

import jakarta.persistence.*;

@Entity
public class RoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne private Member member;

    private Role role;
}
