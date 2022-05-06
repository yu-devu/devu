package com.devu.backend.repository;

import com.devu.backend.entity.QComment;
import com.devu.backend.entity.QLike;
import com.devu.backend.entity.QTag;
import com.devu.backend.entity.Tag;
import com.devu.backend.entity.post.*;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.devu.backend.entity.post.QPost.post;
import static com.devu.backend.entity.post.QStudy.study;

@RequiredArgsConstructor
public class PostRepositoryExtensionImpl implements PostRepositoryExtension{
    private final JPAQueryFactory queryFactory;

    private BooleanExpression studyStatusEq(StudyStatus studyStatus) {
        if (studyStatus == null) {
            return null;
        }
        return study.studyStatus.eq(studyStatus);
    }

    private BooleanExpression questionStatusEq(QuestionStatus questionStatus) {
        if (questionStatus == null) {
            return null;
        }
        return QQuestion.question.questionStatus.eq(questionStatus);
    }

    private BooleanExpression searchSentenceLike(String sentence) {
        if (!StringUtils.hasText(sentence)) {
            return null;
        }
        return post.title.like(sentence);
    }

    private BooleanExpression tagIn(List<PostTags> tags) {
        QTag tag = QTag.tag;
        if (tags == null || tags.size() == 0) {
            return null;
        }
        return tag.postTags.in(tags);
    }

    //SELECT * FROM POST as p join tag t on p.post_id = t.post_id where post_tags like 'REACT';
    @Override
    public Page<Study> findAllStudy(Pageable pageable, PostSearch postSearch) {

        QueryResults<Study> results = queryFactory
                .select(study)
                .from(study)
                .orderBy(study.createAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<Study> studies = results.getResults();
        long total = results.getTotal();
        return new PageImpl<Study>(studies, pageable, total);

    }

    @Override
    public Page<Chat> findAllChat(Pageable pageable, PostSearch postSearch) {
        return null;
    }

    @Override
    public Page<Question> findAllQuestion(Pageable pageable, PostSearch postSearch) {
        return null;
    }
}
