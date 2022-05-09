package com.devu.backend.repository;

import com.devu.backend.common.exception.OrderNotFoundException;
import com.devu.backend.entity.QPostTag;
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
import static com.devu.backend.entity.QPostTag.postTag;
import static com.devu.backend.entity.post.QChat.chat;
import static com.devu.backend.entity.post.QQuestion.question;
import static com.devu.backend.entity.post.QStudy.study;

@RequiredArgsConstructor
public class PostRepositoryExtensionImpl implements PostRepositoryExtension{
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Chat> findAllChats(Pageable pageable, PostSearch postSearch) {
        QChat chat = QChat.chat;
        if (!StringUtils.hasText(postSearch.getOrder())) {
            List<Chat> fetch = queryFactory
                    .select(chat)
                    .from(chat)
                    .where(
                            chatTitleContains(postSearch.getSentence())
                    )
                    .groupBy(chat.id)
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
            return new PageImpl<>(fetch, pageable, fetch.size());
        } else if (postSearch.getOrder().equals("comments")) {
            List<Chat> fetch = queryFactory
                    .select(chat)
                    .from(chat)
                    .leftJoin(chat.comments, comment)
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
        throw new OrderNotFoundException();
    }

    @Override
    public Page<Study> findAllStudies(Pageable pageable, PostSearch postSearch) {
        QStudy study = QStudy.study;
        QPostTag postTag = QPostTag.postTag;
        if (!StringUtils.hasText(postSearch.getOrder())) {
            List<Study> fetch = queryFactory
                    .select(study)
                    .from(study)
                    .innerJoin(postTag)
                    .on(study.id.eq(postTag.post.id))
                    .where(
                            tagIn(postSearch.getTagId()),
                            studyStatusEq(postSearch.getStudyStatus()),
                            studyTitleContains(postSearch.getSentence())
                    )
                    .groupBy(study.id)
                    .having(sizeEq((long) postSearch.getTagId().size()))
                    .orderBy(study.createAt.desc())
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetch();

            return new PageImpl<>(fetch, pageable, fetch.size());
        } else if (postSearch.getOrder().equals("likes")) {
            List<Study> fetch = queryFactory
                    .select(study)
                    .from(study)
                    .innerJoin(postTag)
                    .on(study.id.eq(postTag.post.id))
                    .leftJoin(study.likes,like)
                    .where(
                            tagIn(postSearch.getTagId()),
                            studyStatusEq(postSearch.getStudyStatus()),
                            studyTitleContains(postSearch.getSentence())
                    )
                    .groupBy(study.id)
                    .having(sizeEq((long) postSearch.getTagId().size()))
                    .orderBy(like.post.id.count().desc())
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetch();

            return new PageImpl<>(fetch, pageable, fetch.size());
        } else if (postSearch.getOrder().equals("comments")) {
            List<Study> fetch = queryFactory
                    .select(study)
                    .from(study)
                    .innerJoin(postTag)
                    .on(study.id.eq(postTag.post.id))
                    .leftJoin(study.comments,comment)
                    .where(
                            tagIn(postSearch.getTagId()),
                            studyStatusEq(postSearch.getStudyStatus()),
                            studyTitleContains(postSearch.getSentence())
                    )
                    .groupBy(study.id)
                    .having(sizeEq((long) postSearch.getTagId().size()))
                    .orderBy(comment.post.id.count().desc())
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetch();

            return new PageImpl<>(fetch, pageable, fetch.size());
        }
        throw new OrderNotFoundException();
    }

    @Override
    public Page<Question> findAllQuestions(Pageable pageable, PostSearch postSearch) {
        QQuestion question = QQuestion.question;
        QPostTag postTag = QPostTag.postTag;
        if (!StringUtils.hasText(postSearch.getOrder())) {
            List<Question> fetch = queryFactory
                    .select(question)
                    .from(question)
                    .innerJoin(postTag)
                    .on(question.id.eq(postTag.post.id))
                    .where(
                            tagIn(postSearch.getTagId()),
                            questionStatusEq(postSearch.getQuestionStatus()),
                            questionTitleContains(postSearch.getSentence())
                    )
                    .groupBy(question.id)
                    .having(sizeEq((long) postSearch.getTagId().size()))
                    .orderBy(question.createAt.desc())
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetch();

            return new PageImpl<>(fetch, pageable, fetch.size());
        } else if (postSearch.getOrder().equals("likes")) {
            List<Question> fetch = queryFactory
                    .select(question)
                    .from(question)
                    .innerJoin(postTag)
                    .on(question.id.eq(postTag.post.id))
                    .leftJoin(question.likes,like)
                    .where(
                            tagIn(postSearch.getTagId()),
                            questionStatusEq(postSearch.getQuestionStatus()),
                            questionTitleContains(postSearch.getSentence())
                    )
                    .groupBy(question.id)
                    .having(sizeEq((long) postSearch.getTagId().size()))
                    .orderBy(like.post.id.count().desc())
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetch();

            return new PageImpl<>(fetch, pageable, fetch.size());
        }else if (postSearch.getOrder().equals("comments")){
            List<Question> fetch = queryFactory
                    .select(question)
                    .from(question)
                    .innerJoin(postTag)
                    .on(question.id.eq(postTag.post.id))
                    .leftJoin(question.comments,comment)
                    .where(
                            tagIn(postSearch.getTagId()),
                            questionStatusEq(postSearch.getQuestionStatus()),
                            questionTitleContains(postSearch.getSentence())
                    )
                    .groupBy(question.id)
                    .having(sizeEq((long) postSearch.getTagId().size()))
                    .orderBy(comment.post.id.count().desc())
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetch();

            return new PageImpl<>(fetch, pageable, fetch.size());
        }
        throw new OrderNotFoundException();
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

    private BooleanExpression tagIn(List<Long> ids) {
        if (ids.isEmpty()) {
            return null;
        }
        return postTag.tag.id.in(ids);
    }

    //postTag.tag.id.count().eq((long) postSearch.getTagId().size())
    private BooleanExpression sizeEq(Long size) {
        System.out.println("size = " + size);
        if (size == 0) {
            return null;
        }
        return postTag.tag.id.count().eq(size);
    }

}
