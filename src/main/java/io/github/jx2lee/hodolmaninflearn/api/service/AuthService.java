package io.github.jx2lee.hodolmaninflearn.api.service;

import io.github.jx2lee.hodolmaninflearn.api.domain.User;
import io.github.jx2lee.hodolmaninflearn.api.exception.InvalidSigninInformation;
import io.github.jx2lee.hodolmaninflearn.api.repository.UserRepository;
import io.github.jx2lee.hodolmaninflearn.api.request.Login;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;

    @Transactional
    public void signin(Login login) {
        User user = userRepository.findByEmailAndPassword(login.getEmail(), login.getPassword())
                .orElseThrow(() -> new InvalidSigninInformation());
        user.addSession();
    }
}
