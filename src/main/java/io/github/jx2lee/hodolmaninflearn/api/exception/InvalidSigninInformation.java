package io.github.jx2lee.hodolmaninflearn.api.exception;

public class InvalidSigninInformation extends HodolException {
    private static final String MESSAGE = "아이디/비밀번호가 일치하지 않습니다";
    public InvalidSigninInformation() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
