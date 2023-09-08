package com.targinou.taskmanagement.user;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final ThreadLocal<Integer> currentUserId = new ThreadLocal<>();

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void setCurrentUserId(Integer userId) {
        currentUserId.set(userId);
    }

    public Optional<User> getCurrentUser() {
        return Optional.ofNullable(currentUserId.get()).flatMap(userRepository::findById);
    }

}
