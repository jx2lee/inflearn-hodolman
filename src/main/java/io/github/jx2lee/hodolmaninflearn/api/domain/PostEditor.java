package io.github.jx2lee.hodolmaninflearn.api.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PostEditor {
    // 변경할 필드만 나열
    private final String title;
    private final String content;

    @Builder
    public PostEditor(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
