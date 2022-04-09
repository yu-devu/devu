package com.devu.backend.entity;

import com.devu.backend.entity.post.Post;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


//TODO : XtoOne 연관관계는 모두 LAZY로
@Getter
@Builder
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class User {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    private String username;

    private String password;

    private boolean emailConfirm;

    private String emailAuthKey;

    @OneToMany(mappedBy = "user")
    private List<Post> posts = new ArrayList<>();

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
