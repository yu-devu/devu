package com.devu.backend.repository.comment;

import com.devu.backend.entity.Comment;
import com.devu.backend.entity.QComment;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

@RequiredArgsConstructor
public class CommentRepositoryExtensionImpl implements CommentRepositoryExtension{

    private final JPAQueryFactory queryFactory;

    public Page<Comment> findByPostId(Long postId, Pageable pageable) {
        QComment comment = QComment.comment;
        List<Comment> content = queryFactory
                .selectFrom(comment)
                .where(comment.post.id.eq(postId))
                .orderBy(
                        comment.groupNum.asc(),
                        comment.id.asc()
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Comment> countQuery = queryFactory
                .selectFrom(comment)
                .where(comment.post.id.eq(postId));

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchCount);
    }
}
