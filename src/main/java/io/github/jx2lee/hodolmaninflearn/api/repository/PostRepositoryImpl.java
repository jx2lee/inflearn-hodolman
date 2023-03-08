package io.github.jx2lee.hodolmaninflearn.api.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import io.github.jx2lee.hodolmaninflearn.api.domain.Post;
import io.github.jx2lee.hodolmaninflearn.api.request.PostSearch;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static io.github.jx2lee.hodolmaninflearn.api.domain.QPost.post;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Post> getList(PostSearch postSearch) {
        return jpaQueryFactory.selectFrom(post)
                .limit(postSearch.getSize())
                .offset(postSearch.getOffset())
                .orderBy(post.id.desc())
                .fetch();
    }
}
