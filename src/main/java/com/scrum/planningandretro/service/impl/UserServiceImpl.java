package com.scrum.planningandretro.service.impl;

import com.scrum.planningandretro.model.User;
import com.scrum.planningandretro.repository.UserRepository;
import com.scrum.planningandretro.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Page<User> retrieveAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public Optional<User> retrieveById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public User registerUser(User user) throws IllegalAccessException {
        validateUserRegistration(user);
        return userRepository.save(user);
    }

    public void validateUserRegistration(User user) throws IllegalAccessException {
        Boolean existsByEmail = userRepository.existsByEmail(user.getEmail());
        if (existsByEmail)
            throw new IllegalAccessException("Email already exists");
    }
}
