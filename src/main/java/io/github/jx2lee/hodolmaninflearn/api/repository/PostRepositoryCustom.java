package io.github.jx2lee.hodolmaninflearn.api.repository;

import io.github.jx2lee.hodolmaninflearn.api.domain.Post;
import io.github.jx2lee.hodolmaninflearn.api.request.PostSearch;

import java.util.List;

public interface PostRepositoryCustom {
    List<Post> getList(PostSearch postSearch);
}
