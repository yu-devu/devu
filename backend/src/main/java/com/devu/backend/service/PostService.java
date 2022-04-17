package com.devu.backend.service;

import com.devu.backend.common.exception.PostNotFoundException;
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
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final S3Uploader s3Uploader;
    private final ImageRepository imageRepository;

    /*
    * images 초기화 하는 방식 리팩토링 필요!
    * likes => list 넣어줄 필요 없음
    * */
    @Transactional
    public Chat createChat(RequestPostCreateDto requestPostDto) throws IOException {
        List<Image> images = new ArrayList<>();
        Chat chat = Chat.builder()
                .user(
                        userRepository.findByUsername(requestPostDto.getUsername())
                                .orElseThrow(UserNotFoundException::new)
                )
                .title(requestPostDto.getTitle())
                .content(requestPostDto.getContent())
                .hit(0L)
                .images(images)
                .build();
        addImage(requestPostDto, chat);
        log.info("Create Chat {} By {}",chat.getTitle(),chat.getUser().getUsername());
        return postRepository.save(chat);
    }

    @Transactional
    public Study createStudy(RequestPostCreateDto requestPostDto) throws IOException {
        List<Image> images = new ArrayList<>();
        Study study = Study.builder()
                .user(
                        userRepository.findByUsername(requestPostDto.getUsername())
                                .orElseThrow(UserNotFoundException::new)
                )
                .title(requestPostDto.getTitle())
                .content(requestPostDto.getContent())
                .studyStatus(StudyStatus.ACTIVE)
                .hit(0L)
                .images(images)
                .build();
        addImage(requestPostDto, study);
        log.info("Create Study {} By {}",study.getTitle(),study.getUser().getUsername());
        return postRepository.save(study);
    }

    @Transactional
    public Question createQuestion(RequestPostCreateDto requestPostDto) throws IOException {
        List<Image> images = new ArrayList<>();
        Question question = Question.builder()
                .user(
                        userRepository.findByUsername(requestPostDto.getUsername())
                                .orElseThrow(UserNotFoundException::new)
                )
                .title(requestPostDto.getTitle())
                .content(requestPostDto.getContent())
                .qnaStatus(QuestionStatus.UNSOLVED)
                .hit(0L)
                .images(images)
                .build();
        addImage(requestPostDto, question);
        log.info("Create Question {} By {}",question.getTitle(),question.getUser().getUsername());
        return postRepository.save(question);
    }

    @Transactional
    public void addImage(RequestPostCreateDto requestPostDto, Post post) throws IOException {
        if (!CollectionUtils.isEmpty(requestPostDto.getImages())) {
            for (MultipartFile file : requestPostDto.getImages()) {
                String url = s3Uploader.upload(file, "static", post);
                log.info("s3 생성 {}", url);
            }
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
                        .like(chat.getLikes().size())
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
                        .studyStatus(study.getStudyStatus())
                        .like(study.getLikes().size())
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
                        .questionStatus(question.getQuestionStatus())
                        .like(question.getLikes().size())
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
                .username(chat.getUser().getUsername())
                .content(chat.getContent())
                .title(chat.getTitle())
                .like(chat.getLikes().size())
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
                .username(study.getUser().getUsername())
                .content(study.getContent())
                .title(study.getTitle())
                .studyStatus(study.getStudyStatus())
                .like(study.getLikes().size())
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
                .username(question.getUser().getUsername())
                .content(question.getContent())
                .title(question.getTitle())
                .questionStatus(question.getQuestionStatus())
                .like(question.getLikes().size())
                .build();
    }

    @Transactional
    public void updateChat(Chat chat, RequestPostUpdateDto updateDto) throws IOException {
        updateImage(chat, updateDto);
        chat.updatePost(updateDto);
    }

    @Transactional
    public void updateImage(Post post, RequestPostUpdateDto updateDto) throws IOException {
        List<Image> dbImages = post.getImages();
        for (Image image : dbImages) {
            s3Uploader.delete(image.getName());
            log.info("{}", image);
            imageRepository.delete(image);
            log.info("업데이트 삭제");
        }
        for (MultipartFile multipartFile : updateDto.getImages()) {
            s3Uploader.upload(multipartFile, "static", post);
            log.info("업데이트 추가");
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
        deleteImage(chat);
        postRepository.delete(chat);
    }

    @Transactional
    public void deleteImage(Post post) {
        List<Image> dbImages = post.getImages();
        for (Image image : dbImages) {
            s3Uploader.delete(image.getName());
            imageRepository.delete(image);
        }
        log.info("전체 삭제");
    }

    @Transactional
    public void deleteStudy(Study study) {
        deleteImage(study);
        postRepository.delete(study);
    }

    @Transactional
    public void deleteQuestion(Question question) {
        deleteImage(question);
        postRepository.delete(question);
    }
}

