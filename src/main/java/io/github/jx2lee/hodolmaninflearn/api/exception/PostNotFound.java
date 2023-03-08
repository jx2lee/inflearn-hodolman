package io.github.jx2lee.hodolmaninflearn.api.exception;

public class PostNotFound extends HodolException {

    private static final String MESSAGE = "존재하지 않는 게시글 입니다.";

    public PostNotFound() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 404;
    }
}
