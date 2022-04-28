package com.devu.backend.service;

import com.devu.backend.common.exception.PostNotFoundException;
import com.devu.backend.common.exception.UserNotFoundException;
import com.devu.backend.config.s3.S3Uploader;
import com.devu.backend.controller.post.PostRequestCreateDto;
import com.devu.backend.controller.post.PostRequestUpdateDto;
import com.devu.backend.controller.post.PostResponseDto;
import com.devu.backend.entity.Image;
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
    private final S3Uploader s3Uploader;
    private final ImageRepository imageRepository;
    private final TagService tagService;

    private List<String> getImageUrl(Post post) {
        List<String> images = new ArrayList<>();
        for (Image image : post.getImages()) {
            images.add(image.getPath());
        }
        return images;
    }

    /*
    * images,tags 엔티티가 생성되지 않은 시점에서 응답으로 필요한 데이터라서 new ArrayList<>로 직접 넣어줘야하지 않을까
    * likes => list 넣어줄 필요 없음
    * */
    @Transactional
    public PostResponseDto createChat(PostRequestCreateDto requestPostDto) throws IOException {
        Chat chat = Chat.builder()
                .user(
                        userRepository.findByUsername(requestPostDto.getUsername())
                                .orElseThrow(UserNotFoundException::new)
                )
                .title(requestPostDto.getTitle())
                .content(requestPostDto.getContent())
                .hit(0L)
                .images(new ArrayList<>())
                .tags(new ArrayList<>())
                .build();
        addImage(requestPostDto, chat);
        log.info("Create Chat {} By {}",chat.getTitle(),chat.getUser().getUsername());
        postRepository.save(chat);
        tagService.saveTags(requestPostDto.getTags(), chat.getId());
        return PostResponseDto.builder()
                .title(chat.getTitle())
                .url(getImageUrl(chat))
                .username(chat.getUser().getUsername())
                .tags(chat.getTags())
                .build();
    }

    @Transactional
    public PostResponseDto createStudy(PostRequestCreateDto requestPostDto) throws IOException {
        Study study = Study.builder()
                .user(
                        userRepository.findByUsername(requestPostDto.getUsername())
                                .orElseThrow(UserNotFoundException::new)
                )
                .title(requestPostDto.getTitle())
                .content(requestPostDto.getContent())
                .studyStatus(StudyStatus.ACTIVE)
                .hit(0L)
                .images(new ArrayList<>())
                .tags(new ArrayList<>())
                .build();
        addImage(requestPostDto, study);
        log.info("Create Study {} By {}",study.getTitle(),study.getUser().getUsername());
        postRepository.save(study);
        tagService.saveTags(requestPostDto.getTags(), study.getId());
        return PostResponseDto.builder()
                .title(study.getTitle())
                .url(getImageUrl(study))
                .username(study.getUser().getUsername())
                .tags(study.getTags())
                .build();
    }

    @Transactional
    public PostResponseDto createQuestion(PostRequestCreateDto requestPostDto) throws IOException {
        Question question = Question.builder()
                .user(
                        userRepository.findByUsername(requestPostDto.getUsername())
                                .orElseThrow(UserNotFoundException::new)
                )
                .title(requestPostDto.getTitle())
                .content(requestPostDto.getContent())
                .qnaStatus(QuestionStatus.UNSOLVED)
                .hit(0L)
                .images(new ArrayList<>())
                .tags(new ArrayList<>())
                .build();
        addImage(requestPostDto, question);
        log.info("Create Question {} By {}",question.getTitle(),question.getUser().getUsername());
        postRepository.save(question);
        tagService.saveTags(requestPostDto.getTags(), question.getId());
        return PostResponseDto.builder()
                .title(question.getTitle())
                .url(getImageUrl(question))
                .username(question.getUser().getUsername())
                .tags(question.getTags())
                .build();
    }

    @Transactional
    public void addImage(PostRequestCreateDto requestPostDto, Post post) throws IOException {
        if (!CollectionUtils.isEmpty(requestPostDto.getImages())) {
            for (MultipartFile file : requestPostDto.getImages()) {
                String url = s3Uploader.upload(file, "static", post);
                log.info("s3 생성 {}", url);
            }
        }
    }


    public Page<PostResponseDto> findAllChats(Pageable pageable) {
        return postRepository.findAllChats(pageable).map(
                chat -> PostResponseDto
                        .builder()
                        .id(chat.getId())
                        .title(chat.getTitle())
                        .content(chat.getContent())
                        .username(chat.getUser().getUsername())
                        .hit(chat.getHit())
                        .like(chat.getLikes().size())
                        .tags(chat.getTags())
                        .build()
        );
    }

    public Page<PostResponseDto> findAllStudies(Pageable pageable) {
        return postRepository.findAllStudies(pageable).map(
                study -> PostResponseDto
                        .builder()
                        .title(study.getTitle())
                        .content(study.getContent())
                        .username(study.getUser().getUsername())
                        .hit(study.getHit())
                        .studyStatus(study.getStudyStatus())
                        .like(study.getLikes().size())
                        .tags(study.getTags())
                        .build()
        );
    }

    public Page<PostResponseDto> findAllQuestions(Pageable pageable) {
        return postRepository.findAllQuestions(pageable).map(
                question -> PostResponseDto
                        .builder()
                        .title(question.getTitle())
                        .content(question.getContent())
                        .username(question.getUser().getUsername())
                        .hit(question.getHit())
                        .questionStatus(question.getQuestionStatus())
                        .like(question.getLikes().size())
                        .tags(question.getTags())
                        .build()
        );
    }

    @Transactional
    public PostResponseDto findChatById(Long id) {
        log.info("Selected Chat ID : {}",id);
        Chat chat = postRepository.findChatById(id).orElseThrow(PostNotFoundException::new);
        log.info("Selected Chat Title : {}", chat.getTitle());
        chat.plusHit();
        log.info("Current Hit : {}", chat.getHit());
        return PostResponseDto.builder()
                .id(chat.getId())
                .hit(chat.getHit())
                .username(chat.getUser().getUsername())
                .content(chat.getContent())
                .title(chat.getTitle())
                .like(chat.getLikes().size())
                .comments(chat.getComments())
                .tags(chat.getTags())
                .build();
    }

    @Transactional
    public PostResponseDto findStudyById(Long id) {
        log.info("Selected Study ID : {}",id);
        Study study = postRepository.findStudyById(id).orElseThrow(PostNotFoundException::new);
        log.info("Selected Study Title : {}", study.getTitle());
        study.plusHit();
        log.info("Current Hit : {}", study.getHit());
        return PostResponseDto.builder()
                .id(study.getId())
                .hit(study.getHit())
                .username(study.getUser().getUsername())
                .content(study.getContent())
                .title(study.getTitle())
                .studyStatus(study.getStudyStatus())
                .like(study.getLikes().size())
                .comments(study.getComments())
                .tags(study.getTags())
                .build();
    }

    @Transactional
    public PostResponseDto findQuestionById(Long id) {
        log.info("Selected Question ID : {}",id);
        Question question = postRepository.findQuestionById(id).orElseThrow(PostNotFoundException::new);
        log.info("Selected Question Title : {}", question.getTitle());
        question.plusHit();
        log.info("Current Hit : {}", question.getHit());
        return PostResponseDto.builder()
                .id(question.getId())
                .hit(question.getHit())
                .username(question.getUser().getUsername())
                .content(question.getContent())
                .title(question.getTitle())
                .questionStatus(question.getQuestionStatus())
                .like(question.getLikes().size())
                .comments(question.getComments())
                .tags(question.getTags())
                .build();
    }

    @Transactional
    public void updateImage(Post post, PostRequestUpdateDto updateDto) throws IOException {
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
    public void updateChat(Chat chat, PostRequestUpdateDto updateDto) throws IOException {
        updateImage(chat, updateDto);
        chat.updatePost(updateDto);
    }

    @Transactional
    public void updateStudy(Study study, PostRequestUpdateDto updateDto) throws IOException {
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
    public void updateQuestion(Question question, PostRequestUpdateDto updateDto) throws IOException {
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

