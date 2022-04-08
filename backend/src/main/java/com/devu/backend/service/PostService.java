package com.devu.backend.service;

import com.devu.backend.common.exception.UserNotFoundException;
import com.devu.backend.controller.post.RequestCreateDto;
import com.devu.backend.entity.post.*;
import com.devu.backend.repository.PostRepository;
import com.devu.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public Chat createChat(RequestCreateDto requestCreateDto) {
        Chat chat = Chat.builder()
                .user(
                        userRepository.getByUsername(requestCreateDto.getUsername())
                                .orElseThrow(UserNotFoundException::new)
                )
                .title(requestCreateDto.getTitle())
                .content(requestCreateDto.getContent())
                .build();

        return postRepository.save(chat);
    }

    public Study createStudy(RequestCreateDto requestPostDto) {
        Study study = Study.builder()
                .user(
                        userRepository.getByUsername(requestPostDto.getUsername())
                                .orElseThrow(UserNotFoundException::new)
                )
                .title(requestPostDto.getTitle())
                .content(requestPostDto.getContent())
                .studyStatus(StudyStatus.ACTIVE)
                .build();

        return postRepository.save(study);
    }

    public Question createQuestion(RequestCreateDto requestPostDto) {
        Question question = Question.builder()
                .user(
                        userRepository.getByUsername(requestPostDto.getUsername())
                                .orElseThrow(UserNotFoundException::new)
                )
                .title(requestPostDto.getTitle())
                .content(requestPostDto.getContent())
                .qnaStatus(QuestionStatus.UNSOLVED)
                .build();

        return postRepository.save(question);
    }
}
