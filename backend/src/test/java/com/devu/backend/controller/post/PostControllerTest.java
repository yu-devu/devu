package com.devu.backend.controller.post;

import com.devu.backend.entity.PostTag;
import com.devu.backend.entity.Tag;
import com.devu.backend.entity.post.Chat;
import com.devu.backend.entity.post.Question;
import com.devu.backend.entity.post.Study;
import com.devu.backend.repository.TagRepository;
import com.devu.backend.repository.post.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(RestDocumentationExtension.class)
class PostControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private PostRepository postRepository;
    @Autowired private TagRepository tagRepository;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }

//    @Test
//    @DisplayName("Chat 게시물을 수정한다.")
//    void updateChat() throws Exception {
//        Chat chat = createChat();
//        Chat saveChat = postRepository.save(chat);
//        MockMultipartFile image = new MockMultipartFile("images", "imagefile.jpeg", "image/jpeg", "<<jpeg data>>".getBytes());
//        MockMultipartHttpServletRequestBuilder builder =
//                MockMvcRequestBuilders.multipart("/community/chat/"+ saveChat.getId()).file(image);
//        builder.with(new RequestPostProcessor() {
//            @Override
//            public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
//                request.setMethod("PATCH");
//                return request;
//            }
//        });
//
//        mockMvc.perform(builder
//                //.file(image)
//                .param("title", "change title")
//                .param("content", "change content"))
//                .andExpect(status().isOk())
//                .andDo(document("{method-name}",
//                requestParameters(
//                        parameterWithName("title").description("제목")
//                        ,parameterWithName("content").description("내용")
//                )));
//
//        assertEquals("change title", saveChat.getTitle());
//        assertEquals( "change content", saveChat.getContent());
//    }

    private Chat createChat() {
        Chat chat = Chat.builder()
                .title("title")
                .content("content")
                .images(new ArrayList<>())
                .build();
        return chat;
    }

//    @Test
//    @DisplayName("Study 게시물을 수정한다.")
//    void updateStudy() throws Exception {
//        Tag tag = getTag();
//        PostTag postTag = createPostTag(tag);
//        List<PostTag> tags = addPostTag(postTag);
//        Study study = createStudy(tags);
//
//        postTag.changePost(study);
//        Study saveStudy = postRepository.save(study);
//        MockMultipartFile image = new MockMultipartFile("images", "imagefile.jpeg", "image/jpeg", "<<jpeg data>>".getBytes());
//        MockMultipartHttpServletRequestBuilder builder =
//                MockMvcRequestBuilders.multipart("/community/study/"+ saveStudy.getId()).file(image);
//        builder.with(new RequestPostProcessor() {
//            @Override
//            public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
//                request.setMethod("PATCH");
//                return request;
//            }
//        });
//
//        mockMvc.perform(builder
//                //.file(image)
//                .param("title", "change title")
//                .param("content", "change content")
//                .param("tags", "JAVA"))
//                .andExpect(status().isOk())
//                .andDo(document("{method-name}",
//                        requestParameters(
//                                parameterWithName("title").description("제목")
//                                ,parameterWithName("content").description("내용")
//                                ,parameterWithName("tags").description("태그(List)")
//                        )));
//
//        assertEquals("change title", saveStudy.getTitle());
//        assertEquals( "change content", saveStudy.getContent());
//        assertEquals("JAVA", saveStudy.getPostTags().get(0).getTag().getName());
//    }

    private Study createStudy(List<PostTag> tags) {
        Study study = Study.builder()
                .title("title")
                .content("content")
                .tags(tags)
                .images(new ArrayList<>())
                .build();
        return study;
    }

    private List<PostTag> addPostTag(PostTag postTag) {
        List<PostTag> tags = new ArrayList<>();
        tags.add(postTag);
        return tags;
    }

    private Tag getTag() {
        Tag java = Tag.builder()
                .name("JAVA")
                .build();
        tagRepository.save(java);
        return tagRepository.findTagByName("JAVA").get();
    }

//    @Test
//    @DisplayName("Question 게시물을 수정한다.")
//    void updateQuestion() throws Exception {
//        Tag tag = getTag();
//        PostTag postTag = createPostTag(tag);
//        List<PostTag> tags = addPostTag(postTag);
//        Question question = createQuestion(tags);
//
//        postTag.changePost(question);
//        Question saveQuestion = postRepository.save(question);
//
//        MockMultipartFile image = new MockMultipartFile("images", "imagefile.jpeg", "image/jpeg", "<<jpeg data>>".getBytes());
//        MockMultipartHttpServletRequestBuilder builder =
//                MockMvcRequestBuilders.multipart("/community/question/"+ saveQuestion.getId()).file(image);
//        builder.with(new RequestPostProcessor() {
//            @Override
//            public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
//                request.setMethod("PATCH");
//                return request;
//            }
//        });

//        mockMvc.perform(builder
//                //.file(image)
//                .param("title", "change title")
//                .param("content", "change content")
//                .param("tags", "JAVA"))
//                .andExpect(status().isOk())
//                .andDo(document("{method-name}",
//                        requestParameters(
//                                parameterWithName("title").description("제목")
//                                ,parameterWithName("content").description("내용")
//                                ,parameterWithName("tags").description("태그(List)")
//                        )));
//
//        assertEquals("change title", saveQuestion.getTitle());
//        assertEquals( "change content", saveQuestion.getContent());
//        assertEquals("JAVA", saveQuestion.getPostTags().get(0).getTag().getName());
//    }

    private Question createQuestion(List<PostTag> tags) {
        Question question = Question.builder()
                .title("title")
                .content("content")
                .tags(tags)
                .images(new ArrayList<>())
                .build();
        return question;
    }

    private PostTag createPostTag(Tag tag) {
        return PostTag.builder()
                .tag(tag)
                .build();
    }

    @Test
    void deleteChat() throws Exception {
        Chat chat = createChat();
        postRepository.save(chat);
        assertNotNull(postRepository.findChatById(chat.getId()));

        mockMvc.perform(delete("/community/chat/" + chat.getId()))
                .andExpect(status().isOk())
                .andDo(document("{method-name}"));

        assertEquals(Optional.empty(), postRepository.findChatById(chat.getId()));
    }

    @Test
    void deleteStudy() throws Exception {
        Tag tag = getTag();
        PostTag postTag = createPostTag(tag);
        List<PostTag> postTagList = addPostTag(postTag);
        Study study = createStudy(postTagList);
        postRepository.save(study);
        assertNotNull(postRepository.findChatById(study.getId()));

        mockMvc.perform(delete("/community/study/" + study.getId()))
                .andExpect(status().isOk())
                .andDo(document("{method-name}"));

        assertEquals(Optional.empty(), postRepository.findChatById(study.getId()));
    }

    @Test
    void deleteQuestion() throws Exception {
        Tag tag = getTag();
        PostTag postTag = createPostTag(tag);
        List<PostTag> postTagList = addPostTag(postTag);
        Question question = createQuestion(postTagList);
        postRepository.save(question);
        assertNotNull(postRepository.findChatById(question.getId()));

        mockMvc.perform(delete("/community/question/" + question.getId()))
                .andExpect(status().isOk())
                .andDo(document("{method-name}"));

        assertEquals(Optional.empty(), postRepository.findChatById(question.getId()));
    }
}