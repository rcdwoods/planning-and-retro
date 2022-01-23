package com.scrum.planningandretro.resource;

import com.scrum.planningandretro.converter.UserConverter;
import com.scrum.planningandretro.model.User;
import com.scrum.planningandretro.service.UserService;
import io.swagger.api.UsersApi;
import io.swagger.model.UsersPaginated;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/users")
public class UserResource implements UsersApi {

    private UserService userService;
    private UserConverter userConverter;

    public UserResource(UserService userService, UserConverter userConverter) {
        this.userService = userService;
        this.userConverter = userConverter;
    }

    @Override
    @GetMapping
    public ResponseEntity<UsersPaginated> retrieveUsers(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "per_page", defaultValue = "5") Integer perPage
    ) {
        PageRequest pageRequest = PageRequest.of(page, perPage);
        Page<User> usersFound = userService.retrieveAll(pageRequest);
        List<io.swagger.model.User> usersConverted = userConverter.convertToDto(usersFound.getContent());

        UsersPaginated usersPaginated = new UsersPaginated();
        usersPaginated.setContent(usersConverted);
        usersPaginated.setPage(pageRequest.getPageNumber());
        usersPaginated.setPerPage(pageRequest.getPageSize());

        return ResponseEntity.ok(usersPaginated);
    }

}
