package io.github.jx2lee.hodolmaninflearn.api.exception;

import java.util.HashMap;
import java.util.Map;

public abstract class HodolException extends RuntimeException {
    // 상위 Exception 을 생성하고 이를 상속받는 새로운 exception 추가하는 방식

    public abstract int getStatusCode();

    public final Map<String, String> validation = new HashMap<>();

    public HodolException(String message) {
        super(message);
    }

    public void addValidation(String fieldName, String message) {
        validation.put(fieldName, message);
    }

    public Map<String, String> getValidation() {
        return validation;
    }
}
