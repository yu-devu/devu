package com.devu.backend.repository;

import com.devu.backend.entity.QTag;
import com.devu.backend.entity.post.*;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.devu.backend.entity.QComment.comment;
import static com.devu.backend.entity.QLike.like;
import static com.devu.backend.entity.QTag.tag;
import static com.devu.backend.entity.post.QChat.chat;
import static com.devu.backend.entity.post.QQuestion.question;
import static com.devu.backend.entity.post.QStudy.study;

@RequiredArgsConstructor
public class PostRepositoryExtensionImpl implements PostRepositoryExtension{
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Chat> findAllChats(Pageable pageable, PostSearch postSearch) {
        if (!StringUtils.hasText(postSearch.getOrder())) {
            List<Chat> fetch = queryFactory
                    .select(chat)
                    .from(chat)
                    .where(
                            chatTitleContains(postSearch.getSentence())
                    )
                    .orderBy(chat.createAt.desc())
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetch();

            return new PageImpl<>(fetch, pageable, fetch.size());
        } else if (postSearch.getOrder().equals("likes")) {
            List<Chat> fetch = queryFactory
                    .select(chat)
                    .from(chat)
                    .leftJoin(chat.likes,like)
                    .where(
                            chatTitleContains(postSearch.getSentence())
                    )
                    .groupBy(chat.id)
                    .orderBy(like.post.id.count().desc())
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetch();
            return new PageImpl<Chat>(fetch, pageable, fetch.size());
        }
        List<Chat> fetch = queryFactory
                .select(chat)
                .from(chat)
                .leftJoin(chat.comments,comment)
                .where(
                        chatTitleContains(postSearch.getSentence())
                )
                .groupBy(chat.id)
                .orderBy(comment.post.id.count().desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        return new PageImpl<>(fetch, pageable, fetch.size());
    }

    @Override
    public Page<Study> findAllStudies(Pageable pageable, PostSearch postSearch) {
        if (!StringUtils.hasText(postSearch.getOrder())) {
            List<Study> fetch = queryFactory
                    .select(study)
                    .from(study)
                    .where(
                            studyStatusEq(postSearch.getStudyStatus()),
                            studyTitleContains(postSearch.getSentence())
                    )
                    .orderBy(study.createAt.desc())
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetch();

            return new PageImpl<>(fetch, pageable, fetch.size());
        } else if (postSearch.getOrder().equals("likes")) {
            List<Study> fetch = queryFactory
                    .select(study)
                    .from(study)
                    .leftJoin(study.likes,like)
                    .where(
                            studyStatusEq(postSearch.getStudyStatus()),
                            studyTitleContains(postSearch.getSentence())
                    )
                    .groupBy(study.id)
                    .orderBy(like.post.id.count().desc())
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetch();

            return new PageImpl<>(fetch, pageable, fetch.size());
        }
        List<Study> fetch = queryFactory
                .select(study)
                .from(study)
                .leftJoin(study.comments,comment)
                .where(
                        studyStatusEq(postSearch.getStudyStatus()),
                        studyTitleContains(postSearch.getSentence())
                )
                .groupBy(study.id)
                .orderBy(comment.post.id.count().desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(fetch, pageable, fetch.size());
    }

    @Override
    public Page<Question> findAllQuestions(Pageable pageable, PostSearch postSearch) {
        if (!StringUtils.hasText(postSearch.getOrder())) {
            List<Question> fetch = queryFactory
                    .select(question)
                    .from(question)
                    .where(
                            questionStatusEq(postSearch.getQuestionStatus()),
                            questionTitleContains(postSearch.getSentence())
                    )
                    .orderBy(question.createAt.desc())
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetch();

            return new PageImpl<>(fetch, pageable, fetch.size());
        } else if (postSearch.getOrder().equals("likes")) {
            List<Question> fetch = queryFactory
                    .select(question)
                    .from(question)
                    .leftJoin(question.likes,like)
                    .where(
                            questionStatusEq(postSearch.getQuestionStatus()),
                            questionTitleContains(postSearch.getSentence())
                    )
                    .groupBy(question.id)
                    .orderBy(like.post.id.count().desc())
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetch();

            return new PageImpl<>(fetch, pageable, fetch.size());
        }
        List<Question> fetch = queryFactory
                .select(question)
                .from(question)
                .leftJoin(question.comments,comment)
                .where(
                        questionStatusEq(postSearch.getQuestionStatus()),
                        questionTitleContains(postSearch.getSentence())
                )
                .groupBy(question.id)
                .orderBy(comment.post.id.count().desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<Question>(fetch, pageable, fetch.size());
    }

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
        return question.questionStatus.eq(questionStatus);
    }

    private BooleanExpression studyTitleContains(String sentence) {
        if (!StringUtils.hasText(sentence)) {
            return null;
        }
        return study.title.contains(sentence);
    }
    private BooleanExpression questionTitleContains(String sentence) {
        if (!StringUtils.hasText(sentence)) {
            return null;
        }
        return question.title.contains(sentence);
    }
    private BooleanExpression chatTitleContains(String sentence) {
        if (!StringUtils.hasText(sentence)) {
            return null;
        }
        return chat.title.contains(sentence);
    }

}
