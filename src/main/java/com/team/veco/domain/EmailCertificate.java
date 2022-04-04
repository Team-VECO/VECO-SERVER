package com.team.veco.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Email_Certificate")
@Builder @Getter
@AllArgsConstructor @NoArgsConstructor
public class EmailCertificate{

    @Id
    @GeneratedValue
    @Column(name = "certificate_id")
    private Long id;

    @Column(name = "certificate_email", nullable = false)
    private String email;

    @Column(name = "certificate_key", nullable = false)
    private String key;

    @Column(name = "certificate_expiredTime", nullable = false)
    private LocalDateTime expiredTime;
}
