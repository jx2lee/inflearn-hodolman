package io.github.jx2lee.hodolmaninflearn.api.repository;

import io.github.jx2lee.hodolmaninflearn.api.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {
}
