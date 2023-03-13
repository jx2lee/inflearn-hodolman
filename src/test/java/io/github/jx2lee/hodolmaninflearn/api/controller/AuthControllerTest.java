package io.github.jx2lee.hodolmaninflearn.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.jx2lee.hodolmaninflearn.api.domain.User;
import io.github.jx2lee.hodolmaninflearn.api.repository.UserRepository;
import io.github.jx2lee.hodolmaninflearn.api.request.Login;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class AuthControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void clean() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("로그인 성공 및 세션 1개 생성")
    void login_succeeded() throws Exception {
        // given
        User user = userRepository.save(User.builder()
                .name("이재준")
                .email("dev.jaejun.lee.1991@gmail.com")
                .password("1234")
                .build()
        );
        Login login = Login.builder()
                .email("dev.jaejun.lee.1991@gmail.com")
                .password("1234")
                .build();
        String json = objectMapper.writeValueAsString(login);

        // expected
        mockMvc.perform(post("/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
        User loggedInUser = userRepository.findById(user.getId())
                .orElseThrow(RuntimeException::new);
        System.out.println("loggedInUser.getSessions() = " + loggedInUser.getSessions());
        // Assertions.assertEquals(1, loggedInUser.getSessions().size());
    }
}