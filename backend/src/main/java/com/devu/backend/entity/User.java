package com.devu.backend.entity;

import lombok.*;

import javax.persistence.*;


//TODO : XtoOne 연관관계는 모두 LAZY로
@Getter
@Builder
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(unique = true)
    private String username;

    private String password;

    private boolean emailConfirm;

    private String emailAuthKey;

    public void updateUserInfo(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void updateAuthKey(String emailAuthKey) {
        this.emailAuthKey = emailAuthKey;
    }

    public void updateEmailConfirm(boolean isConfirm) {
        this.emailConfirm = isConfirm;
    }
}
