package com.example.userservicesaga.service;

import com.example.commonservicesaga.modals.UserDto;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    public Object userLogin(UserDto userDto);


    public Object addUser(UserDto userDto);

    public Object updateUser(UserDto userDto);

    public Object getAllUsers();

    public Object deleteUser(Long userId);

    public Object getUserById(Long UserId);

}


