package com.devu.backend.repository;

import com.devu.backend.entity.post.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> ,PostRepositoryExtension{
    @Query("select p from Post p where TYPE(p) IN(Chat) order by p.createAt desc")
    Page<Chat> findAllChats(Pageable pageable);

    @Query("select p from Post p where TYPE(p) IN(Study) order by p.createAt desc")
    Page<Study> findAllStudies(Pageable pageable);

    @Query("select p from Post p where TYPE(p) IN(Question) order by p.createAt desc")
    Page<Question> findAllQuestions(Pageable pageable);

    Optional<Chat> findChatById(Long id);

    Optional<Question> findQuestionById(Long id);

    Optional<Study> findStudyById(Long id);

    Optional<List<Chat>> findTop3ChatByOrderByHitDesc();

    Optional<List<Study>> findTop3StudyByOrderByHitDesc();

    Optional<List<Question>> findTop3QuestionByOrderByHitDesc();

    @Query(value = "select p from Post p" +
            " left join Like l on p.id = l.post.id " +
            " where Type(p) IN(Chat)" +
            " group by p.id order by count(l.post.id) desc")
    Optional<List<Chat>> findTop3ChatByOrderByLikes(Pageable pageable);

    /*
    * @Query를 사용하는데 들고오는 갯수의 제한이 필요한 경우 아래처럼
    * */
    default Optional<List<Chat>> findTop3ChatByOrderByLikes() {
        return findTop3ChatByOrderByLikes(PageRequest.of(0,3));
    }

    @Query(value = "select p from Post p" +
            " left join Like l on p.id = l.post.id " +
            " where Type(p) IN(Study)" +
            " group by p.id order by count(l.post.id) desc")
    Optional<List<Study>> findTop3StudyByOrderByLikes(Pageable pageable);

    default Optional<List<Study>> findTop3StudyByOrderByLikes() {
        return findTop3StudyByOrderByLikes(PageRequest.of(0,3));
    }

    @Query(value = "select p from Post p" +
            " left join Like l on p.id = l.post.id " +
            " where Type(p) IN(Question )" +
            " group by p.id order by count(l.post.id) desc")
    Optional<List<Question>> findTop3QuestionByOrderByLikes(Pageable pageable);

    default Optional<List<Question>> findTop3QuestionByOrderByLikes() {
        return findTop3QuestionByOrderByLikes(PageRequest.of(0,3));
    }

    @Query("select s from Study s " +
            "where Type(s) In(Study) and " +
            "s.studyStatus = :studyStatus " +
            "order by s.createAt desc")
    Page<Study> findAllStudyByStatus(Pageable pageable, @Param("studyStatus") StudyStatus studyStatus);


    @Query("select q from Question q " +
            "where Type(q) In(Question) and " +
            "q.questionStatus = :questionStatus " +
            "order by q.createAt desc")
    Page<Question> findAllQuestionByStatus(Pageable pageable,@Param("questionStatus") QuestionStatus questionStatus);

}
