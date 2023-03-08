package io.github.jx2lee.hodolmaninflearn.api.controller;

import io.github.jx2lee.hodolmaninflearn.api.config.data.UserSession;
import io.github.jx2lee.hodolmaninflearn.api.request.PostCreate;
import io.github.jx2lee.hodolmaninflearn.api.request.PostEdit;
import io.github.jx2lee.hodolmaninflearn.api.request.PostSearch;
import io.github.jx2lee.hodolmaninflearn.api.response.PostResponse;
import io.github.jx2lee.hodolmaninflearn.api.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/foo")
    public long foo(UserSession userSession) {
        log.info(">>> {}", userSession.id);
        return userSession.id;
    }
    @GetMapping("/bar")
    public String bar() {
        return "bar";
    }

    @PostMapping("/posts")
    public void post(@RequestBody @Valid PostCreate request) {
        // Request class
        /*
         * case1. POST 요청 데이터 전체 수신
         * case2. POST 요청 시 primary_id 만 수신
         client 에서 POST 요청 시 데이터를 수신 받음
         * case3. 응답 필요 없음 -> client 에서 요청 context 를 잘 관리하는 경우
         bad case: 꼭 이렇게 응답을 내릴겁니다 fix
         -> server 에서 유연히 대응 -> 코드를 잘 짜자
         -> 일괄적으로 처리하는 케이스는 드물다 -> 잘 관리하는 형태가 Nice!
         */
        request.validate();
        postService.write(request);
    }

    @GetMapping("/posts/{postId}")
    public PostResponse get(@PathVariable(name = "postId") Long id) {
        // Response class
        return postService.get(id);
    }

    @GetMapping("/posts-v1")
    public List<PostResponse> getListV1() {
        return postService.getListV1();
    }

    @GetMapping("/posts-v2")
    // public List<PostResponse> getListV2(@RequestParam int page) {
    public List<PostResponse> getListV2(Pageable pageable) {
        return postService.getListV2(pageable);
    }

    @GetMapping("/posts-v3")
    public List<PostResponse> getListV3(@ModelAttribute PostSearch postSearch) {
        return postService.getListV3(postSearch);
    }

    @PatchMapping("/posts/{postId}")
    public void edit(@PathVariable Long postId, @RequestBody PostEdit request) {
        postService.edit(postId, request);
    }

    @DeleteMapping("/posts/{postId}")
    public void delete(@PathVariable Long postId) {
        postService.delete(postId);
    }
}
