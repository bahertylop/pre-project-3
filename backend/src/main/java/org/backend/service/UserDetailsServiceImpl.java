package org.backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.backend.model.User;
import org.backend.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String chatId) throws UsernameNotFoundException {
        Optional<User> userOp = userRepository.getUserByChatId(Long.parseLong(chatId));
        if (userOp.isEmpty()) {
            log.error("user with chatId: {} not found", chatId);
            throw new UsernameNotFoundException("User with username(chatId): " + chatId + " not found");
        }

        return userOp.get();
    }
}
