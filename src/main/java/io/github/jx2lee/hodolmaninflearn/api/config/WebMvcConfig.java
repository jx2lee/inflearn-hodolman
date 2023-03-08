package io.github.jx2lee.hodolmaninflearn.api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    // Interceptor 를 사용하는 것보다 UserSession 을 받는 경우에만 인증할 수 있도록 argument resolver 를 사용하는 편이 현재 프로젝트에 적합하다.
    // 따라서 주석처리
    // @Override
    // public void addInterceptors(InterceptorRegistry registry) {
    //     registry.addInterceptor(new AuthInterceptor())
    //             .excludePathPatterns("/bar");
    // }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new AuthResolver());
    }
}
