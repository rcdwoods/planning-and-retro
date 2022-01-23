package com.scrum.planningandretro.service;

import com.scrum.planningandretro.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Page<User> retrieveAll(Pageable pageable);
    Optional<User> retrieveById(Long id);
    User registerUser(User user) throws IllegalAccessException;
}
