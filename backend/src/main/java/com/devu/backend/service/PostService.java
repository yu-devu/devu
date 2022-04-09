package com.devu.backend.service;

import com.devu.backend.common.exception.UserNotFoundException;
import com.devu.backend.controller.post.RequestPostCreateDto;
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
        userService.createUser(test.getEmail(), test.getEmailAuthKey());
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
}
