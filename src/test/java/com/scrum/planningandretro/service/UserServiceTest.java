package com.scrum.planningandretro.service;

import com.scrum.planningandretro.model.User;
import com.scrum.planningandretro.repository.UserRepository;
import com.scrum.planningandretro.service.impl.UserServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ContextConfiguration(classes = {UserServiceImpl.class})
@ExtendWith(SpringExtension.class)
class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    private Pageable defaultPageable = PageRequest.of(0, 5);

    @BeforeEach
    public void setup() {
        List<User> users = List.of(
                new User(1L, "Richard", "richard@scrum.com"),
                new User(2L, "Leonardo", "leonardo@scrum.com")
        );

        Mockito.when(userRepository.findAll(defaultPageable)).thenReturn(new PageImpl(users, defaultPageable, 2));
        Mockito.when(userRepository.save(Mockito.any())).thenAnswer(i -> i.getArgument(0));
    }

    @Test
    public void mustReturnAllUsersPaginated() {
        Page<User> users = userService.retrieveAll(defaultPageable);

        Assertions.assertThat(users.getContent()).hasSize(2);
        Assertions.assertThat(users.getContent().get(0).getEmail()).isEqualTo("richard@scrum.com");
        Assertions.assertThat(users.getContent().get(1).getEmail()).isEqualTo("leonardo@scrum.com");
    }

    @Test
    public void mustRegisterUserWhenHisEmailIsNotAlreadyRegistered() throws IllegalAccessException {
        User user = new User(3L, "Alfred", "alfred@scrum.com");
        User registeredUser = userService.registerUser(user);

        Assertions.assertThat(registeredUser).isEqualTo(user);
    }

    @Test
    public void mustNotRegisterUserAndThrowExceptionWhenHisEmailAlreadyIsRegistered() {
        User user = new User(3L, "Alfred", "richard@scrum.com");

        Mockito.when(userRepository.existsByEmail(user.getEmail())).thenReturn(true);

        assertThrows(IllegalAccessException.class, () -> userService.registerUser(user));
    }

    @Test
    public void mustReturnSpecificUserById() {
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(new User(1L, "Richard", "richard@scrum.com")));

        Optional<User> userFound = userService.retrieveById(1L);

        Assertions.assertThat(userFound).isPresent();
        Assertions.assertThat(userFound.get().getEmail()).isEqualTo("richard@scrum.com");
    }

}
