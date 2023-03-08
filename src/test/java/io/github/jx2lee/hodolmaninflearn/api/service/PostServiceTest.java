package io.github.jx2lee.hodolmaninflearn.api.service;

import io.github.jx2lee.hodolmaninflearn.api.domain.Post;
import io.github.jx2lee.hodolmaninflearn.api.exception.PostNotFound;
import io.github.jx2lee.hodolmaninflearn.api.repository.PostRepository;
import io.github.jx2lee.hodolmaninflearn.api.request.PostCreate;
import io.github.jx2lee.hodolmaninflearn.api.request.PostEdit;
import io.github.jx2lee.hodolmaninflearn.api.request.PostSearch;
import io.github.jx2lee.hodolmaninflearn.api.response.PostResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PostServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void clean() {
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("게시글 작성 시 저장된 게시글은 1개이고 내용을 검증한다.")
    void test1() {
        // given
        PostCreate postCreate = PostCreate.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();

        // when
        postService.write(postCreate);

        // then
        assertEquals(1L, postRepository.count());

        Post post = postRepository.findAll().get(0);
        assertEquals("제목입니다.", post.getTitle());
        assertEquals("내용입니다.", post.getContent());
    }

    @Test
    @DisplayName("게시글 조회 시 제목을 10글자를 넘기지 않는다.")
    void test2() {
        // given
        Post requestPost = Post.builder()
                .title("foo")
                .content("bar")
                .build();
        postRepository.save(requestPost);

        // when
        PostResponse post = postService.get(requestPost.getId());

        // then
        assertNotNull(post);
        assertEquals(1L, postRepository.count());
        assertEquals("foo", post.getTitle());
        assertEquals("bar", post.getContent());
    }

    @Test
    @DisplayName("게시글 다중 조회 시 findAll 로 검증한다.")
    void test3() {
        // given
        postRepository.saveAll(List.of(
                Post.builder()
                        .title("foo1")
                        .content("bar1")
                        .build(),
                Post.builder()
                        .title("foo2")
                        .content("bar2")
                        .build()
                )
        );

        // when
        List<PostResponse> posts = postService.getListV1();

        // then
        assertEquals(2L, posts.size());
    }

    @Test
    @DisplayName("게시글 다중 조회 시 Pageable 을 이용한다.")
    void test4() {
        // given
        List<Post> requestPosts = IntStream.range(1, 31)
                .mapToObj(i -> Post.builder()
                        .title("제목 - " + i)
                        .content("내용 - " + i)
                        .build())
                .collect(Collectors.toList());
        postRepository.saveAll(requestPosts);

        Pageable pageable = PageRequest.of(0, 5, Sort.Direction.DESC, "id");
        // when
        List<PostResponse> posts = postService.getListV2(pageable);

        // then
        assertEquals(5, posts.size());
        assertEquals("제목 - 30", posts.get(0).getTitle());
        assertEquals("제목 - 26", posts.get(4).getTitle());
    }

    @Test
    @DisplayName("게시글 다중 조회 시 queryDsl 을 이용한다.")
    void test5() {
        // given
        List<Post> requestPosts = IntStream.range(0, 20)
                .mapToObj(i -> Post.builder()
                        .title("제목 - " + i)
                        .content("내용 - " + i)
                        .build())
                .collect(Collectors.toList());
        postRepository.saveAll(requestPosts);

        PostSearch postSearch = PostSearch.builder()
                .page(1)
                .size(10)
                .build();
        // when
        List<PostResponse> posts = postService.getListV3(postSearch);

        // then
        assertEquals(10, posts.size());
        assertEquals("제목 - 19", posts.get(0).getTitle());
        assertEquals("제목 - 15", posts.get(4).getTitle());
    }

    @Test
    @DisplayName("게시글 수정 후 제목을 검증한다.")
    void test6() {
        // given
        Post post = Post.builder()
                .title("foo")
                .content("bar")
                .build();
        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("bar")
                .build();

        // when
        postService.edit(post.getId(), postEdit);

        // then
        Post changedPost = postRepository.findById(post.getId())
                .orElseThrow(() -> new RuntimeException("게시글이 존재하지 않습니다. id=" + post.getId()));
        assertEquals("bar", changedPost.getTitle());
    }

    @Test
    @DisplayName("게시글 삭제 후 조회하면 PostNotFound exception 이 발생한다.")
    void test7() {
        // given
        Post post = Post.builder()
                .title("foo")
                .content("bar")
                .build();
        postRepository.save(post);

        // when
        postService.delete(post.getId());

        // then
        assertThrows(PostNotFound.class, () -> {
            postRepository.findById(post.getId())
                    .orElseThrow(PostNotFound::new);
        });
    }

    @Test
    @DisplayName("게시글 조회 시 실패하는 경우 PostNotFound exception 이 발생한다.")
    void test8() {
        // given
        Post requestPost = Post.builder()
                .title("foo")
                .content("bar")
                .build();
        postRepository.save(requestPost);

        // expected
        Assertions.assertThrows(PostNotFound.class, () -> {
            postService.get(requestPost.getId() + 1L);
        });
    }
}