package io.github.jx2lee.hodolmaninflearn.api.request;

import io.github.jx2lee.hodolmaninflearn.api.exception.InvalidRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
public class PostCreate {

    @NotBlank(message = "타이틀을 입력하세요.")
    private String title;

    @NotBlank(message = "컨텐츠를 입력해주세요.")
    private String content;

    /**
     * Builder
     *  - 가독성
     *  - 불변성
     *  - 필요한 값만 받을 수 있다. overloading 조건
    **/

    @Builder
    public PostCreate(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void validate() {
        if (title.contains("바보")) {
            throw new InvalidRequest("title", "제목에 '바보'를 포함할 수 없습니다.");
        }
    }
}
