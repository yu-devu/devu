package com.devu.backend.service;

import com.devu.backend.controller.post.PostRequestUpdateDto;
import com.devu.backend.entity.PostTag;
import com.devu.backend.entity.Tag;
import com.devu.backend.entity.post.Chat;
import com.devu.backend.entity.post.Question;
import com.devu.backend.entity.post.Study;
import com.devu.backend.repository.post.PostRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @InjectMocks
    private PostService postService;

    @Mock
    private PostRepository postRepository;

    @Mock
    private TagService tagService;

    @Test
    void updateChat() throws IOException {
        Chat chat = createChat();
        given(postRepository.findChatById(chat.getId())).willReturn(Optional.of(chat));
        PostRequestUpdateDto postRequestUpdateDto = PostRequestUpdateDto.builder()
                                                                    .title("change")
                                                                    .content("test content")
                                                                    .images(new ArrayList<>())
                                                                    .build();

        postService.updateChat(chat.getId(), postRequestUpdateDto);

        assertEquals(chat.getTitle(), "change");
        assertEquals(chat.getContent(), "test content");
    }

    private Chat createChat() {
        return Chat.builder()
                .id(5L)
                .title("test")
                .content("test content")
                .images(new ArrayList<>())
                .build();
    }

    @Test
    void updateStudy() throws IOException {
        Tag tag = createTag();
        PostTag postTag = createPostTag(tag);
        List<PostTag> postTagList = addPostTag(postTag);
        Study study = createStudy(postTagList);
        postTag.changePost(study);
        given(postRepository.findStudyById(study.getId())).willReturn(Optional.of(study));
        given(tagService.findTagName(postTag)).willReturn("SPRING");
        List<String> tags = addTag();
        PostRequestUpdateDto postRequestUpdateDto = createPostRequestUpdateDto(tags);

        postService.updateStudy(study.getId(), postRequestUpdateDto);

        assertEquals(study.getTitle(), "change");
        assertEquals(study.getContent(), "test content");
        assertEquals(study.getPostTags().get(0).getTag().getName(), "SPRING");
    }

    private Study createStudy(List<PostTag> postTagList) {
        return Study.builder()
                .id(5L)
                .title("test")
                .content("content")
                .images(new ArrayList<>())
                .tags(postTagList)
                .build();
    }

    private Tag createTag() {
        return Tag.builder()
                .name("SPRING").build();
    }

    @Test
    void updateQuestion() throws IOException {
        Tag tag = createTag();
        PostTag postTag = createPostTag(tag);
        List<PostTag> postTagList = addPostTag(postTag);
        Question question = createQuestion(postTagList);
        postTag.changePost(question);
        given(postRepository.findQuestionById(question.getId())).willReturn(Optional.of(question));
        given(tagService.findTagName(postTag)).willReturn("SPRING");
        List<String> tags = addTag();
        PostRequestUpdateDto postRequestUpdateDto = createPostRequestUpdateDto(tags);

        postService.updateQuestion(question.getId(), postRequestUpdateDto);

        assertEquals(question.getTitle(), "change");
        assertEquals(question.getContent(), "test content");
        assertEquals(question.getPostTags().get(0).getTag().getName(), "SPRING");
    }

    private Question createQuestion(List<PostTag> postTagList) {
        return Question.builder()
                .id(5L)
                .title("test")
                .content("content")
                .tags(postTagList)
                .images(new ArrayList<>())
                .build();
    }

    private PostRequestUpdateDto createPostRequestUpdateDto(List<String> tags) {
        return PostRequestUpdateDto.builder()
                .title("change")
                .content("test content")
                .images(new ArrayList<>())
                .tags(tags)
                .build();
    }

    private List<String> addTag() {
        List<String> tags = new ArrayList<>();
        tags.add("SPRING");
        return tags;
    }

    private List<PostTag> addPostTag(PostTag postTag) {
        List<PostTag> postTagList = new ArrayList<>();
        postTagList.add(postTag);
        return postTagList;
    }

    private PostTag createPostTag(Tag tag) {
        return PostTag.builder()
                .tag(tag)
                .build();
    }

    @Test
    void deleteChat() {
        Chat chat = createChat();
        postRepository.save(chat);

        assertNotNull(postRepository.findChatById(chat.getId()));
        postService.deleteChat(chat);

        assertEquals(Optional.empty(), postRepository.findChatById(chat.getId()));
    }

    @Test
    void deleteStudy() {
        Tag tag = createTag();
        PostTag postTag = createPostTag(tag);
        List<PostTag> postTagList = addPostTag(postTag);
        Study study = createStudy(postTagList);

        postRepository.save(study);

        assertNotNull(postRepository.findChatById(study.getId()));
        postService.deleteStudy(study);

        assertEquals(Optional.empty(), postRepository.findChatById(study.getId()));
    }

    @Test
    void deleteQuestion() {
        Tag tag = createTag();
        PostTag postTag = createPostTag(tag);
        List<PostTag> postTagList = addPostTag(postTag);
        Question question = createQuestion(postTagList);

        postRepository.save(question);

        assertNotNull(postRepository.findChatById(question.getId()));
        postService.deleteQuestion(question);

        assertEquals(Optional.empty(), postRepository.findChatById(question.getId()));
    }
}