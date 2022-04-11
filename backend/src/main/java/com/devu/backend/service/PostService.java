package com.devu.backend.service;

import com.devu.backend.common.exception.PostNotFoundException;
import com.devu.backend.common.exception.UserNotFoundException;
import com.devu.backend.controller.post.RequestPostCreateDto;
import com.devu.backend.controller.post.RequestPostUpdateDto;
import com.devu.backend.controller.post.ResponsePostDto;
import com.devu.backend.entity.User;
import com.devu.backend.entity.post.*;
import com.devu.backend.repository.PostRepository;
import com.devu.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.InputMismatchException;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final UserService userService;


    @Transactional
    public Chat createChat(RequestPostCreateDto requestCreateDto) {
        Chat chat = Chat.builder()
                .user(
                        userRepository.getByUsername(requestCreateDto.getUsername())
                                .orElseThrow(UserNotFoundException::new)
                )
                .title(requestCreateDto.getTitle())
                .content(requestCreateDto.getContent())
                .hit(0L)
                .like(0L)
                .build();
        log.info("Create Chat {} By {}",chat.getTitle(),chat.getUser().getUsername());
        return postRepository.save(chat);
    }

    @Transactional
    public Study createStudy(RequestPostCreateDto requestPostDto) {
        Study study = Study.builder()
                .user(
                        userRepository.getByUsername(requestPostDto.getUsername())
                                .orElseThrow(UserNotFoundException::new)
                )
                .title(requestPostDto.getTitle())
                .content(requestPostDto.getContent())
                .studyStatus(StudyStatus.ACTIVE)
                .hit(0L)
                .like(0L)
                .build();
        log.info("Create Study {} By {}",study.getTitle(),study.getUser().getUsername());
        return postRepository.save(study);
    }

    @Transactional
    public Question createQuestion(RequestPostCreateDto requestPostDto) {
        Question question = Question.builder()
                .user(
                        userRepository.getByUsername(requestPostDto.getUsername())
                                .orElseThrow(UserNotFoundException::new)
                )
                .title(requestPostDto.getTitle())
                .content(requestPostDto.getContent())
                .qnaStatus(QuestionStatus.UNSOLVED)
                .hit(0L)
                .like(0L)
                .build();
        log.info("Create Question {} By {}",question.getTitle(),question.getUser().getUsername());
        return postRepository.save(question);
    }

    /*
    * Test Data
    * */
    @Transactional
    @PostConstruct
    public void init() {
        User test = User.builder()
                .email("test@ynu.ac.kr")
                .username("test")
                .emailAuthKey("test")
                .build();
        userService.createUser(test.getEmail(), test.getEmailAuthKey(), test.getUsername());
        for (int i = 1; i <= 25; i++) {
            RequestPostCreateDto dto = RequestPostCreateDto.builder()
                    .title("test" + i)
                    .username(test.getUsername())
                    .content("test-content" + i)
                    .build();
            createQuestion(dto);
            createChat(dto);
            createStudy(dto);
        }
    }


    public Page<ResponsePostDto> findAllChats(Pageable pageable) {
        return postRepository.findAllChats(pageable).map(
                chat -> ResponsePostDto
                        .builder()
                        .id(chat.getId())
                        .title(chat.getTitle())
                        .content(chat.getContent())
                        .username(chat.getUser().getUsername())
                        .hit(chat.getHit())
                        .like(chat.getLike())
                        .build()
        );
    }

    public Page<ResponsePostDto> findAllStudies(Pageable pageable) {
        return postRepository.findAllStudies(pageable).map(
                study -> ResponsePostDto
                        .builder()
                        .title(study.getTitle())
                        .content(study.getContent())
                        .username(study.getUser().getUsername())
                        .hit(study.getHit())
                        .like(study.getLike())
                        .studyStatus(study.getStudyStatus())
                        .build()
        );
    }

    public Page<ResponsePostDto> findAllQuestions(Pageable pageable) {
        return postRepository.findAllQuestions(pageable).map(
                question -> ResponsePostDto
                        .builder()
                        .title(question.getTitle())
                        .content(question.getContent())
                        .username(question.getUser().getUsername())
                        .hit(question.getHit())
                        .like(question.getLike())
                        .questionStatus(question.getQuestionStatus())
                        .build()
        );
    }

    @Transactional
    public ResponsePostDto findChatById(Long id) {
        log.info("Selected Chat ID : {}",id);
        Chat chat = postRepository.findChatById(id).orElseThrow(PostNotFoundException::new);
        log.info("Selected Chat Title : {}", chat.getTitle());
        chat.plusHit();
        log.info("Current Hit : {}", chat.getHit());
        return ResponsePostDto.builder()
                .id(chat.getId())
                .hit(chat.getHit())
                .like(chat.getLike())
                .username(chat.getUser().getUsername())
                .content(chat.getContent())
                .title(chat.getTitle())
                .build();
    }

    @Transactional
    public ResponsePostDto findStudyById(Long id) {
        log.info("Selected Study ID : {}",id);
        Study study = postRepository.findStudyById(id).orElseThrow(PostNotFoundException::new);
        log.info("Selected Study Title : {}", study.getTitle());
        study.plusHit();
        log.info("Current Hit : {}", study.getHit());
        return ResponsePostDto.builder()
                .id(study.getId())
                .hit(study.getHit())
                .like(study.getLike())
                .username(study.getUser().getUsername())
                .content(study.getContent())
                .title(study.getTitle())
                .studyStatus(study.getStudyStatus())
                .build();
    }

    @Transactional
    public ResponsePostDto findQuestionById(Long id) {
        log.info("Selected Question ID : {}",id);
        Question question = postRepository.findQuestionById(id).orElseThrow(PostNotFoundException::new);
        log.info("Selected Question Title : {}", question.getTitle());
        question.plusHit();
        log.info("Current Hit : {}", question.getHit());
        return ResponsePostDto.builder()
                .id(question.getId())
                .hit(question.getHit())
                .like(question.getLike())
                .username(question.getUser().getUsername())
                .content(question.getContent())
                .title(question.getTitle())
                .questionStatus(question.getQuestionStatus())
                .build();
    }

    @Transactional
    public void updateChat(Chat chat, RequestPostUpdateDto updateDto) {
        chat.updatePost(updateDto);
    }

    @Transactional
    public void updateStudy(Study study, RequestPostUpdateDto updateDto) {
        study.updatePost(updateDto);
        if (updateDto.getStatus().equals("ACTIVE"))
            study.updateStatus(StudyStatus.ACTIVE);
        else if (updateDto.getStatus().equals("CLOSED"))
            study.updateStatus(StudyStatus.CLOSED);
        else
            throw new InputMismatchException("잘못된 입력입니다");
    }

    @Transactional
    public void updateQuestion(Question question, RequestPostUpdateDto updateDto) {
        question.updatePost(updateDto);
        if (updateDto.getStatus().equals("SOLVED"))
            question.updateStatus(QuestionStatus.SOLVED);
        else if (updateDto.getStatus().equals("UNSOLVED"))
            question.updateStatus(QuestionStatus.UNSOLVED);
        else
            throw new InputMismatchException("잘못된 입력입니다");
    }

    @Transactional
    public void deleteChat(Chat chat) {
        postRepository.delete(chat);
    }

    @Transactional
    public void deleteStudy(Study study) {
        postRepository.delete(study);
    }

    @Transactional
    public void deleteQuestion(Question question) {
        postRepository.delete(question);
    }
}
