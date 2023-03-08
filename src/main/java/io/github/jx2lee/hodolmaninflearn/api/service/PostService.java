package io.github.jx2lee.hodolmaninflearn.api.service;

import io.github.jx2lee.hodolmaninflearn.api.domain.Post;
import io.github.jx2lee.hodolmaninflearn.api.domain.PostEditor;
import io.github.jx2lee.hodolmaninflearn.api.exception.PostNotFound;
import io.github.jx2lee.hodolmaninflearn.api.repository.PostRepository;
import io.github.jx2lee.hodolmaninflearn.api.request.PostCreate;
import io.github.jx2lee.hodolmaninflearn.api.request.PostEdit;
import io.github.jx2lee.hodolmaninflearn.api.request.PostSearch;
import io.github.jx2lee.hodolmaninflearn.api.response.PostResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public void write(PostCreate postCreate) {
        Post post = Post.builder()
                .title(postCreate.getTitle())
                .content(postCreate.getContent())
                .build();

        postRepository.save(post);
        // return post; // case1
        // return post.getId(); // case2
    }

    public PostResponse get(Long id) {
        // 요구사항 발생 시 entity 에는 서비스 정책을 절대 생성하지 않는다.
        // 응답 class 를 분리한다.
        Post post = postRepository.findById(id)
                .orElseThrow(PostNotFound::new);

        PostResponse response = PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .build();

        return response;
    }

    public List<PostResponse> getListV1() {
        return postRepository.findAll().stream()
                .map(PostResponse::new)
                .collect(Collectors.toList());
    }

    public List<PostResponse> getListV2(Pageable pageable) {
        // Pageable pageable = PageRequest.of(page, 5, Sort.by(DESC, "id"));
        return postRepository.findAll(pageable).stream()
                .map(PostResponse::new)
                .collect(Collectors.toList());
    }

    public List<PostResponse> getListV3(PostSearch postSearch) {
        // Pageable pageable = PageRequest.of(page, 5, Sort.by(DESC, "id"));
        return postRepository.getList(postSearch).stream()
                .map(PostResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void edit(Long postId, PostEdit postEdit) {
        Post post = postRepository.findById(postId)
                .orElseThrow(PostNotFound::new);

        PostEditor.PostEditorBuilder postEditorBuilder = post.toEditor();

        PostEditor postEditor = postEditorBuilder.title(postEdit.getTitle())
                .content(postEdit.getContent()).build();

        post.edit(postEditor);
    }

    public void delete(Long id) {
        // postRepository.deleteById(id);
        Post post = postRepository.findById(id)
                .orElseThrow(PostNotFound::new);
        postRepository.delete(post);
    }
}
