package com.personalproject.adminserver.domain;

import javax.persistence.*;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String email;

    private String role;

    private String name;

    private boolean emailVerified;
}
