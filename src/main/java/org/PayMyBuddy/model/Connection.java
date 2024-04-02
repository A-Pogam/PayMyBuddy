package org.PayMyBuddy.model;

import jakarta.persistence.*;
import org.springframework.security.core.userdetails.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "connection")
public class Connection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "connection_id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_initializer_id")
    private User initializer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_receiver_id")
    private User receiver;

    private LocalDateTime startingDate;
}

