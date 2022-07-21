package com.devu.backend.config.s3;

import com.devu.backend.entity.Image;
import com.devu.backend.entity.post.Chat;
import com.devu.backend.repository.post.PostRepository;
import io.findify.s3mock.S3Mock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
@Import(AwsS3MockConfig.class)
class S3UploaderTest {

    @Autowired
    private S3Mock s3Mock;

    @Autowired
    private S3Uploader s3Uploader;

    @Autowired
    private PostRepository postRepository;

    @AfterEach
    public void tearDown() {
        s3Mock.stop();
    }

    @Test
    @Transactional
    void upload() throws Exception{
        String path = "icon.jpg";
        String contentType = "jpg";
        String dirName = "static";
        MockMultipartFile file = new MockMultipartFile("icon", path, contentType,
                new FileInputStream(new File("src/test/resources/icon.jpg")));
        Chat chat = Chat.builder()
                .id(1000L)
                .title("test")
                .images(new ArrayList<>())
                .build();
        postRepository.save(chat);

        Image image = s3Uploader.upload(file, dirName);

        assertThat(image.getPath()).contains(path);
        assertThat(image.getPath()).contains(dirName);
    }

}