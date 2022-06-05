package com.toyproject.book.springboot.service.user;

import com.toyproject.book.springboot.config.auth.dto.SessionUser;
import com.toyproject.book.springboot.domian.user.Role;
import com.toyproject.book.springboot.domian.user.User;
import com.toyproject.book.springboot.domian.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public void updateUserAuth(SessionUser sessionUser){
        Optional<User> user = userRepository.findByEmail(sessionUser.getEmail());
        User savedUser = user.orElseThrow(()-> {
            throw new IllegalArgumentException("해당 사용자가 존재하지 않습니다. email="+sessionUser.getEmail());
        });
        log.info("savedUser : {}, {}", savedUser.getName(), savedUser.getEmail());
        savedUser.updateRole();
    }

}
