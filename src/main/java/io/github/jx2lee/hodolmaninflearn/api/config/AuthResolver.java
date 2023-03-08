package io.github.jx2lee.hodolmaninflearn.api.config;

import io.github.jx2lee.hodolmaninflearn.api.config.data.UserSession;
import io.github.jx2lee.hodolmaninflearn.api.exception.NotAuthorized;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class AuthResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(UserSession.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String accessToken = webRequest.getHeader("authorization");
        if (accessToken == null || accessToken.equals("")) {
            throw new NotAuthorized();
        }

        // check db user
        // ...

        return new UserSession(1L);
    }
}
