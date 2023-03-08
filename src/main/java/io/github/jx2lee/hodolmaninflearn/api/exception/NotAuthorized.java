package io.github.jx2lee.hodolmaninflearn.api.exception;

public class NotAuthorized extends HodolException {

    private static final String MESSAGE = "인증되지 않은 사용자입니다.";

    public NotAuthorized() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 401;
    }
}
