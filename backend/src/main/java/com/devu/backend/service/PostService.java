package com.devu.backend.service;

import com.devu.backend.api.status.ResponseStatusDto;
import com.devu.backend.common.exception.PostNotFoundException;
import com.devu.backend.common.exception.UserNotFoundException;
import com.devu.backend.common.exception.UserNotMatchException;
import com.devu.backend.config.s3.S3Uploader;
import com.devu.backend.controller.post.PostRequestCreateDto;
import com.devu.backend.controller.post.PostRequestUpdateDto;
import com.devu.backend.controller.post.PostResponseDto;
import com.devu.backend.entity.Image;
import com.devu.backend.entity.Tag;
import com.devu.backend.entity.User;
import com.devu.backend.entity.post.*;
import com.devu.backend.repository.ImageRepository;
import com.devu.backend.repository.PostRepository;
import com.devu.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

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
    * images,tags 엔티티가 생성되지 않은 시점에서 응답으로 필요한 데이터라서 new ArrayList<>로 직접 넣어줘야함
    * 즉,Image 엔티티와 Tag 엔티티가 생성되기 전에 chat 인스턴스에서 접근해서 우선적으로 만드는것
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
                .tags(new LinkedList<>())
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
                .tags(new LinkedList<>())
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
                .tags(new LinkedList<>())
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
                        .id(study.getId())
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
                        .id(question.getId())
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
        log.info("Current Like : {}", chat.getLikes().size());
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
        log.info("Current Like : {}", study.getLikes().size());
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
        log.info("Current Like : {}", question.getLikes().size());
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

    private void updatePostTags(PostRequestUpdateDto updateDto, Post post) {
        //updateWithTags의 리턴값 -> 태그를 새로 설정하기 위해 지워야하는 tags -> 즉, 기존에 설정된 태그들 중, 지워야할 태그
        //but tag id가 존재하지 않음 -> repository에서 find 필수
        List<Tag> orphanTags = new LinkedList<>();
        for (Tag tag : post.updateWithTags(updateDto)) {
            Tag deleteTag = tagService.getTagByPostAndPostTag(tag.getPost().getId(), tag.getPostTags());
            orphanTags.add(deleteTag);
        }
        for (Tag tag : orphanTags) {
            post.getTags().remove(tag);//orphan = true로 인해서 삭제 가능
        }
    }

    @Transactional
    public void updateChat(Long chatId, PostRequestUpdateDto updateDto) throws IOException {
        Chat chat = postRepository.findChatById(chatId).orElseThrow(PostNotFoundException::new);
        if (!updateDto.getImages().isEmpty()) {
            updateImage(chat, updateDto);
        }
        //valid가 true면 실행
        if (chat.tagUpdateValidation(updateDto)) {
            updatePostTags(updateDto, chat);
        }
        chat.updatePost(updateDto);
    }

    @Transactional
    public void updateStudy(Long studyId, PostRequestUpdateDto updateDto) throws IOException {
        Study study = postRepository.findStudyById(studyId).orElseThrow(PostNotFoundException::new);
        updateImage(study, updateDto);
        if (study.tagUpdateValidation(updateDto)) {
            updatePostTags(updateDto, study);
        }
        study.updatePost(updateDto);
    }

    @Transactional
    public void updateQuestion(Long questionId, PostRequestUpdateDto updateDto) throws IOException {
        Question question = postRepository.findQuestionById(questionId).orElseThrow(PostNotFoundException::new);
        updateImage(question, updateDto);
        if (question.tagUpdateValidation(updateDto)) {
            updatePostTags(updateDto,question);
        }
        question.updatePost(updateDto);
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

    @Transactional
    public ResponseStatusDto updateStudyStatus(Long studyId,String username) {
        Study study = postRepository.findStudyById(studyId).orElseThrow(PostNotFoundException::new);
        isOwner(study, username);
        if (study.getStudyStatus() == StudyStatus.ACTIVE) {
            study.updateStatus(StudyStatus.CLOSED);
            return ResponseStatusDto.builder()
                    .studyStatus(study.getStudyStatus())
                    .id(study.getId())
                    .build();
        }
        study.updateStatus(StudyStatus.ACTIVE);
        return ResponseStatusDto.builder()
                .studyStatus(study.getStudyStatus())
                .id(study.getId())
                .build();
    }

    @Transactional
    public ResponseStatusDto updateQuestionStatus(Long questionId,String username) {
        Question question = postRepository.findQuestionById(questionId).orElseThrow(PostNotFoundException::new);
        isOwner(question, username);
        if (question.getQuestionStatus() == QuestionStatus.UNSOLVED) {
            question.updateStatus(QuestionStatus.SOLVED);
            return ResponseStatusDto.builder()
                    .questionStatus(question.getQuestionStatus())
                    .id(question.getId())
                    .build();
        }
        question.updateStatus(QuestionStatus.UNSOLVED);
        return ResponseStatusDto.builder()
                .questionStatus(question.getQuestionStatus())
                .id(question.getId())
                .build();
    }

    private void isOwner(Post post, String username) {
        if (!post.getUser().getUsername().equals(username)) {
            throw new UserNotMatchException();
        }
    }

    public List<PostResponseDto> getTop3ChatByHits() {
        return postRepository.findTop3ChatByOrderByHitDesc()
                .orElseThrow(PostNotFoundException::new)
                .stream().map(c -> PostResponseDto.builder()
                        .id(c.getId())
                        .title(c.getTitle())
                        .hit(c.getHit())
                        .content(c.getContent())
                        .like(c.getLikes().size())
                        .tags(c.getTags())
                        .username(c.getUser().getUsername())
                        .build()
                ).collect(Collectors.toList());
    }

    public List<PostResponseDto> getTop3ChatByLikes() {
        return postRepository.findTop3ChatByOrderByLikes().orElseThrow(PostNotFoundException::new)
                .stream().map(c -> PostResponseDto.builder()
                        .id(c.getId())
                        .title(c.getTitle())
                        .hit(c.getHit())
                        .content(c.getContent())
                        .like(c.getLikes().size())
                        .tags(c.getTags())
                        .username(c.getUser().getUsername())
                        .build()
                ).collect(Collectors.toList());
    }

    public List<PostResponseDto> getTop3StudyByHits() {
        return postRepository.findTop3StudyByOrderByHitDesc().orElseThrow(PostNotFoundException::new)
                .stream().map(s -> PostResponseDto.builder()
                        .id(s.getId())
                        .title(s.getTitle())
                        .hit(s.getHit())
                        .content(s.getContent())
                        .like(s.getLikes().size())
                        .tags(s.getTags())
                        .username(s.getUser().getUsername())
                        .build()
                ).collect(Collectors.toList());
    }

    public List<PostResponseDto> getTop3StudyByLikes() {
        return postRepository.findTop3StudyByOrderByLikes().orElseThrow(PostNotFoundException::new)
                .stream().map(s -> PostResponseDto.builder()
                        .id(s.getId())
                        .title(s.getTitle())
                        .hit(s.getHit())
                        .content(s.getContent())
                        .like(s.getLikes().size())
                        .tags(s.getTags())
                        .username(s.getUser().getUsername())
                        .build()
                ).collect(Collectors.toList());
    }

    public List<PostResponseDto> getTop3QuestionByHits() {
        return postRepository.findTop3QuestionByOrderByHitDesc().orElseThrow(PostNotFoundException::new)
                .stream().map(q -> PostResponseDto.builder()
                        .id(q.getId())
                        .title(q.getTitle())
                        .hit(q.getHit())
                        .content(q.getContent())
                        .like(q.getLikes().size())
                        .tags(q.getTags())
                        .username(q.getUser().getUsername())
                        .build()
                ).collect(Collectors.toList());
    }

    public List<PostResponseDto> getTop3QuestionByLikes() {
        return postRepository.findTop3QuestionByOrderByLikes().orElseThrow(PostNotFoundException::new)
                .stream().map(q -> PostResponseDto.builder()
                        .id(q.getId())
                        .title(q.getTitle())
                        .hit(q.getHit())
                        .content(q.getContent())
                        .like(q.getLikes().size())
                        .tags(q.getTags())
                        .username(q.getUser().getUsername())
                        .build()
                ).collect(Collectors.toList());
    }
}

