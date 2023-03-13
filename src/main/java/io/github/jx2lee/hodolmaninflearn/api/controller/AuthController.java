package io.github.jx2lee.hodolmaninflearn.api.controller;

import io.github.jx2lee.hodolmaninflearn.api.domain.User;
import io.github.jx2lee.hodolmaninflearn.api.exception.InvalidSigninInformation;
import io.github.jx2lee.hodolmaninflearn.api.repository.UserRepository;
import io.github.jx2lee.hodolmaninflearn.api.request.Login;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {
    private final UserRepository userRepository;

    @PostMapping("/auth/login")
    public User login(@RequestBody Login login) {
        // json id:password
        log.info(">>> login, {}", login.toString());

        // DB Check
        User user = userRepository.findByEmailAndPassword(login.getEmail(), login.getPassword())
                .orElseThrow(() -> new InvalidSigninInformation());

        // response token

        return user;
    }
}
