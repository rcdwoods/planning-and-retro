package com.scrum.planningandretro.converter;

import com.scrum.planningandretro.model.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserConverter {
    io.swagger.model.User convertToDto(User user);
    List<io.swagger.model.User> convertToDto(List<User> users);
    User convertToModel(io.swagger.model.User userDto);
}
