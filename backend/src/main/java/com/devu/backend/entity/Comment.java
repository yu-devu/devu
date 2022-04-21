package com.devu.backend.entity;

import com.devu.backend.entity.post.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Comment extends BaseTime{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String contents;

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private Post post;

    public void updateContent(String contents) {
        this.contents = contents;
    }

    /*
    * 댓글 삭제 시 "삭제된 댓글입니다." 흔적 남기기
    * */
}
