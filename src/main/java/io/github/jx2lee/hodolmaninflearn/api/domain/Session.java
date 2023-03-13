package io.github.jx2lee.hodolmaninflearn.api.domain;

import lombok.Builder;
import lombok.RequiredArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import java.util.UUID;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@RequiredArgsConstructor
public class Session {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String accessToken;

    @ManyToOne
    private User user;

    // private LocalDateTime expiredAt;

    @Builder

    public Session(User user) {
        // UUID
        this.accessToken = UUID.randomUUID().toString();
        this.user = user;
    }
}
