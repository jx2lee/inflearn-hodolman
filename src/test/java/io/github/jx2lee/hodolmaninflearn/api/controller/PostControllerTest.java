package io.github.jx2lee.hodolmaninflearn.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.jx2lee.hodolmaninflearn.api.domain.Post;
import io.github.jx2lee.hodolmaninflearn.api.repository.PostRepository;
import io.github.jx2lee.hodolmaninflearn.api.request.PostCreate;
import io.github.jx2lee.hodolmaninflearn.api.request.PostEdit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
// @WebMvcTest -> AutoConfigureMockMvc 로 MockMvc 생성자 주입이 가능
class PostControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void clean() {
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("글 작성 요청 시 빈 값을 출력합니다.")
    void test1() throws Exception {
        // given
        PostCreate postCreate = PostCreate.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();

        String json = objectMapper.writeValueAsString(postCreate);

        // expected: when + then
        mockMvc.perform(post("/posts?authorization=jx2lee")
                        .contentType(APPLICATION_JSON)
                        // .content("{\"title\": \"제목입니다.\", \"content\": \"내용입니다.\"}")
                        .content(json)
                )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(""))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("글 작성 요청 시 title 값은 필수다.")
    void test2() throws Exception {
        // given
        PostCreate postCreate = PostCreate.builder()
                .content("내용입니다.")
                .build();

        String json = objectMapper.writeValueAsString(postCreate);

        // expected
        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("400"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("잘못된 요청입니다."))
                .andExpect(MockMvcResultMatchers.jsonPath("$.validation.title").value("타이틀을 입력하세요."))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("글 작성 요청 시 DB 에 값을 저장합니다.")
    void JUNIT_4_이전에는_한글로_이렇게_작성했지만_DisplayName_이_생기면서_굳이_이렇게_안해도_된다_영어로_실무에서_한다고_함() throws Exception {
        // given
        PostCreate postCreate = PostCreate.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();

        String json = objectMapper.writeValueAsString(postCreate);

        // when
        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());

        // then
        assertEquals(1L, postRepository.count());

        Post post = postRepository.findAll().get(0);
        assertEquals("제목입니다.", post.getTitle());
        assertEquals("내용입니다.", post.getContent());
    }

    @Test
    @DisplayName("/posts/{postId} 글 단건 조회")
    void test4() throws Exception {
        // given
        Post post = Post.builder()
                .title("foo")
                .content("bar")
                .build();
        postRepository.save(post);

        // expected
        mockMvc.perform(get("/posts/{postId}", post.getId())
                        .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(post.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(post.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").value(post.getContent()))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("/posts/{postId} 글 단건 조회 시 제목을 10글자로 제한한다.")
    void test5() throws Exception {
        // given
        Post post = Post.builder()
                .title("123456789012345")
                .content("bar")
                .build();
        postRepository.save(post);
        String expectedTitle = "1234567890";

        // expected
        mockMvc.perform(get("/posts/{postId}", post.getId())
                        .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(expectedTitle))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("/posts 글 여러개 조회 테스트")
    void test6() throws Exception {
        // given
        Post post1 = postRepository.save(Post.builder()
                .title("foo1")
                .content("bar1")
                .build()
        );

        Post post2 = postRepository.save(Post.builder()
                .title("foo2")
                .content("bar2")
                .build()
        );

        // expected
        mockMvc.perform(get("/posts-v1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(2)))
                .andExpect(jsonPath("$[0].id").value(post1.getId()))
                .andExpect(jsonPath("$[0].title").value("foo1"))
                .andExpect(jsonPath("$[0].content").value("bar1"))
                .andExpect(jsonPath("$[1].id").value(post2.getId()))
                .andExpect(jsonPath("$[1].title").value("foo2"))
                .andExpect(jsonPath("$[1].content").value("bar2"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("/posts 글 여러개 조회 페이징 테스트")
    void test7() throws Exception {
        // given
        List<Post> requestPosts = IntStream.range(1, 31)
                .mapToObj(i -> Post.builder()
                        .title("제목 - " + i)
                        .content("내용 - " + i)
                        .build())
                .collect(Collectors.toList());
        postRepository.saveAll(requestPosts);

        // expected
        mockMvc.perform(get("/posts-v2?page=1&sort=id,desc")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(5)))
                .andExpect(jsonPath("$[0].title").value("제목 - 30"))
                .andExpect(jsonPath("$[0].content").value("내용 - 30"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("/posts 글 여러개 조회 페이징 테스트: queryDsl")
    void test8() throws Exception {
        // given
        List<Post> requestPosts = IntStream.range(0, 20)
                .mapToObj(i -> Post.builder()
                        .title("제목 - " + i)
                        .content("내용 - " + i)
                        .build())
                .collect(Collectors.toList());
        postRepository.saveAll(requestPosts);

        // expected
        mockMvc.perform(get("/posts-v3?page=1&size=10")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(10)))
                .andExpect(jsonPath("$[0].title").value("제목 - 19"))
                .andExpect(jsonPath("$[0].content").value("내용 - 19"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("/posts/{postId}: 글 수정: 제목 수정")
    void test9() throws Exception {
        // given
        Post post = Post.builder()
                .title("foo")
                .content("bar")
                .build();
        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("bar")
                .content("bar")
                .build();

        // expected
        mockMvc.perform(patch("/posts/{postId}", post.getId())
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postEdit)))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("/posts/{postId}: 글 삭제")
    void test10() throws Exception {
        // given
        Post post = Post.builder()
                .title("foo")
                .content("bar")
                .build();
        postRepository.save(post);

        // expected
        mockMvc.perform(delete("/posts/{postId}", post.getId())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("제목에 바보 가 포함되면 invalidException 이 발생한다.")
    void test11() throws Exception {
        // given
        Post post = Post.builder()
                .title("바보 foo")
                .content("bar")
                .build();
        postRepository.save(post);

        String json = objectMapper.writeValueAsString(post);

        // when
        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }
}