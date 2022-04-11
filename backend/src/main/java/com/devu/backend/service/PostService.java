package com.devu.backend.service;

import com.devu.backend.common.exception.UserNotFoundException;
import com.devu.backend.config.s3.S3Uploader;
import com.devu.backend.controller.post.RequestPostCreateDto;
import com.devu.backend.controller.post.RequestPostUpdateDto;
import com.devu.backend.controller.post.ResponsePostDto;
import com.devu.backend.entity.Image;
import com.devu.backend.entity.User;
import com.devu.backend.entity.post.*;
import com.devu.backend.repository.ImageRepository;
import com.devu.backend.repository.PostRepository;
import com.devu.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final S3Uploader s3Uploader;
    private final ImageRepository imageRepository;

    @Transactional
    public Chat createChat(RequestPostCreateDto requestPostDto) throws IOException {
        Chat chat = Chat.builder()
                .user(
                        userRepository.getByUsername(requestPostDto.getUsername())
                                .orElseThrow(UserNotFoundException::new)
                )
                .title(requestPostDto.getTitle())
                .content(requestPostDto.getContent())
                .hit(0L)
                .like(0L)
                .build();
        for (MultipartFile file : requestPostDto.getImages()) {
            s3Uploader.upload(file, "static", chat);
        }
        log.info("Create Chat {} By {}",chat.getTitle(),chat.getUser().getUsername());
        return postRepository.save(chat);
    }

    @Transactional
    public Study createStudy(RequestPostCreateDto requestPostDto) throws IOException {
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
        for (MultipartFile file : requestPostDto.getImages()) {
            s3Uploader.upload(file, "static", study);
        }
        log.info("Create Study {} By {}",study.getTitle(),study.getUser().getUsername());
        return postRepository.save(study);
    }

    @Transactional
    public Question createQuestion(RequestPostCreateDto requestPostDto) throws IOException {
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
        for (MultipartFile file : requestPostDto.getImages()) {
            s3Uploader.upload(file, "static", question);
        }
        log.info("Create Question {} By {}",question.getTitle(),question.getUser().getUsername());
        return postRepository.save(question);
    }

    /*
    * Test Data
    * */
    @Transactional
    @PostConstruct
    public void init() throws IOException {
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
    public void updateChat(Chat chat, RequestPostUpdateDto updateDto) throws IOException {
        updateImage(chat, updateDto);
        chat.updatePost(updateDto);
    }

    private void updateImage(Post post, RequestPostUpdateDto updateDto) throws IOException {
        List<Image> dbImages = post.getImages();
        for (Image image : dbImages) {
            s3Uploader.delete(image.getName());
            imageRepository.delete(image);
        }
        for (MultipartFile multipartFile : updateDto.getImages()) {
            s3Uploader.upload(multipartFile, "static", post);
        }
    }

    @Transactional
    public void updateStudy(Study study, RequestPostUpdateDto updateDto) throws IOException {
        updateImage(study, updateDto);
        study.updatePost(updateDto);
        if (updateDto.getStatus().equals("ACTIVE"))
            study.updateStatus(StudyStatus.ACTIVE);
        else if (updateDto.getStatus().equals("CLOSED"))
            study.updateStatus(StudyStatus.CLOSED);
        else
            throw new InputMismatchException("잘못된 입력입니다");
    }

    @Transactional
    public void updateQuestion(Question question, RequestPostUpdateDto updateDto) throws IOException {
        updateImage(question, updateDto);
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
