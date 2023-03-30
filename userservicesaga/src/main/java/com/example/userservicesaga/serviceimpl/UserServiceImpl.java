package com.example.userservicesaga.serviceimpl;

import com.example.commonservicesaga.modals.UserDto;
import com.example.userservicesaga.dto.Response;
import com.example.userservicesaga.entity.User;
import com.example.userservicesaga.exception.InvalidCredentialsException;
import com.example.userservicesaga.exception.UserException;
import com.example.userservicesaga.projection.UserRepository;
import com.example.userservicesaga.service.JwtTokenService;
import com.example.userservicesaga.service.UserService;
import com.example.userservicesaga.utility.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    Environment env;

    @Autowired
    UserRepository userRepository;

    @Autowired
    JwtTokenService jwtTokenService;

    @Override
    public Object userLogin(UserDto userDto) {
        try {
            log.info("Inside userLogin of UserServiceImpl");
            if (loginUserValidation(userDto)) {
                log.info("Getting Invalid data in request");
                throw new InvalidCredentialsException(env.getProperty(Constants.INVALID_DATA));
            }
            User registredUser = userExistByEmailAndPassword(userDto.getEmail(), userDto.getPassword());
            if (registredUser == null) {
                log.info("Invalid Credentials");
                throw new InvalidCredentialsException(env.getProperty(Constants.INVALID_CREDENTIALS));
            }
            Map<String, String> map = jwtTokenService.generateToken(registredUser);
            return new Response<>(env.getProperty(Constants.SUCCESS_CODE), env.getProperty(Constants.USER_LOGGINED_SUCESSFULLY), map);
        } catch (Exception e) {
            String errorMessage = null;
            if (e instanceof InvalidCredentialsException) {
                errorMessage = ((InvalidCredentialsException) e).getMessage();
            } else {
                errorMessage = MessageFormat.format("Exception caught in userLogin of UserServiceImpl:{0}", e.getMessage());
            }
            log.error(errorMessage);
            throw new UserException(errorMessage);
        }
    }

    @Override
    public Object addUser(UserDto userDto) {
        try {
            log.info("Inside addUser of UserServiceImpl");
            if (addUserValidation(userDto)) {
                log.info("Getting Invalid data in request");
                throw new UserException(env.getProperty(Constants.INVALID_DATA));
            }
            if (userExistByEmail(userDto.getEmail())) {
                log.info("User already exist");
                throw new UserException(env.getProperty(Constants.USER_ALREADY_EXIST));
            }
            User user = new User();
            log.warn("Not saving email in encoded format");
            user.setEmail(userDto.getEmail());
            user.setPassword(userDto.getPassword());
            user.setAddress(userDto.getAddress());
            user.setPassword(userDto.getPassword());
            user.setFirstName(userDto.getFirstName());
            user.setLastName(userDto.getLastName());
            User savedUser = userRepository.save(user);
            log.debug("User id after saving the user is :{}", savedUser.getUserId());
            return new Response<>(env.getProperty(Constants.SUCCESS_CODE), env.getProperty(Constants.USER_SAVED_SUCCESSFULLY));

        } catch (Exception e) {
            String errorMessage = null;
            if (e instanceof UserException) {
                errorMessage = ((UserException) e).getMessage();
            } else {
                errorMessage = MessageFormat.format("Exception caught in addUser of UserServiceImpl:{0}", e.getMessage());
            }
            log.error(errorMessage);
            throw new UserException(errorMessage);
        }
    }

    @Override
    public Object updateUser(UserDto userDto) {
        try {
            log.info("Inside updateUser of UserServiceImpl");
            if (userDto.getUserId() == null) {
                log.info("Getting user id as null in request data");
                throw new UserException(env.getProperty(Constants.KINDLY_PROVIDE_USER_ID));
            }
            Optional<User> existedUser = userExistById(userDto.getUserId());
            if (existedUser.isEmpty()) {
                log.info("User does not exist");
                throw new UserException(env.getProperty(Constants.USER_DOES_NOT_EXIST));
            }
            User user = existedUser.get();
            user.setUserId(userDto.getUserId());
            user.setEmail(userDto.getEmail() != null ? userDto.getEmail() : user.getEmail());
            user.setPassword(userDto.getPassword() != null ? userDto.getPassword() : userDto.getPassword());
            user.setFirstName(userDto.getFirstName() != null ? userDto.getFirstName() : user.getFirstName());
            user.setLastName(userDto.getLastName() != null ? userDto.getLastName() : user.getLastName());
            user.setAddress(userDto.getAddress() != null ? userDto.getAddress() : userDto.getAddress());
            User updatedUser = userRepository.save(user);
            return new Response<>(updatedUser, env.getProperty(Constants.SUCCESS_CODE), env.getProperty(Constants.USER_UPDATED_SUCCESSFULLY));

        } catch (Exception e) {
            String errorMessage = null;
            if (e instanceof UserException) {
                errorMessage = ((UserException) e).getMessage();
            } else {
                errorMessage = MessageFormat.format("Exception caught in updateUser of UserServiceImpl:{0}", e.getMessage());
            }
            log.error(errorMessage);
            throw new UserException(errorMessage);
        }
    }

    @Override
    public Object getAllUsers() {
        try {
            log.info("Inside getAllUsers of UserServiceImpl");
            List<UserDto> userDtoList = new ArrayList<>();
            List<User> userList = userRepository.findAll();
            for (User user : userList) {
                userDtoList.add(populateUserData(user));
            }
            return new Response<>(userDtoList, env.getProperty(Constants.SUCCESS_CODE), env.getProperty(Constants.USER_FETCHED_SUCCESSFULLY));

        } catch (Exception e) {
            String errorMessage = null;
            if (e instanceof UserException) {
                errorMessage = ((UserException) e).getMessage();
            } else {
                errorMessage = MessageFormat.format("Exception caught in getAllUsers of UserServiceImpl:{0}", e.getMessage());
            }
            log.error(errorMessage);
            throw new UserException(errorMessage);
        }
    }

    @Override
    public Object deleteUser(Long userId) {
        try {
            log.info("Inside deleteUser of UserServiceImpl");
            if (userId == null) {
                throw new UserException(env.getProperty(Constants.KINDLY_PROVIDE_USER_ID));
            }
            Optional<User> existedUser = userExistById(userId);
            if (existedUser.isEmpty()) {
                throw new UserException(env.getProperty(Constants.USER_DOES_NOT_EXIST));
            }
            userRepository.deleteById(userId);
            return new Response<>(env.getProperty(Constants.SUCCESS_CODE), env.getProperty(Constants.USER_DELETED_SUCCESSFULLY));

        } catch (Exception e) {
            String errorMessage = null;
            if (e instanceof UserException) {
                errorMessage = ((UserException) e).getMessage();
            } else {
                errorMessage = MessageFormat.format("Exception caught in deleteUser of USErServiceImpl:{0}", e.getMessage());
            }
            log.error(errorMessage);
            throw new UserException(errorMessage);
        }
    }

    @Override
    public Object getUserById(Long userId) {
        try {
            log.info("Inside getUserById of UserServiceImpl");
            if (userId == null) {
                throw new UserException(env.getProperty(Constants.KINDLY_PROVIDE_USER_ID));
            }
            Optional<User> existedUser = userExistById(userId);
            if (existedUser.isEmpty()) {
                throw new UserException(env.getProperty(Constants.USER_DOES_NOT_EXIST));
            }
            return new Response<>(populateUserData(existedUser.get()), env.getProperty(Constants.SUCCESS_CODE), env.getProperty(Constants.USER_FETCHED_SUCCESSFULLY));
        } catch (Exception e) {
            String errorMessage = null;
            if (e instanceof UserException) {
                errorMessage = ((UserException) e).getMessage();
            } else {
                errorMessage = MessageFormat.format("Exception caught in getUserById of UserServiceImpl:{0}", e.getMessage());
            }
            log.error(errorMessage);
            throw new UserException(errorMessage);
        }
    }

    public boolean loginUserValidation(UserDto userDto) {
        log.info("Inside loginUserValidation");
        boolean flag = false;
        if (userDto.getEmail() == null || userDto.getEmail().isEmpty() || userDto.getPassword() == null || userDto.getPassword().isEmpty()) {
            flag = true;
        }
        return flag;
    }

    public User userExistByEmailAndPassword(String userEmail, String userPassword) throws Exception {
        log.info("Inside userExistByEmailAndPassword Of UserServiceImpl");
        User user = userRepository.findByEmailAndPassword(userEmail, userPassword);
        return user;
    }

    public boolean addUserValidation(UserDto userDto) {
        log.info("Inside addUserValidation");
        boolean flag = false;
        if (userDto.getFirstName() == null ||
                userDto.getFirstName() == null || userDto.getLastName()==null || userDto.getFirstName().isEmpty() || userDto.getEmail() == null
                || userDto.getEmail().isBlank() || userDto.getEmail().isEmpty() || userDto.getPassword() == null
                || userDto.getPassword().isEmpty() || userDto.getAddress()==null || userDto.getAddress().isEmpty()) {
            flag = true;
        }
        return flag;
    }


    public boolean userExistByEmail(String userEmail) throws Exception {
        log.info("Inside userExistByEmail Of UserServiceImpl");
        boolean flag = false;
        User user = userRepository.findByEmail(userEmail);
        if (user != null) {
            flag = true;
        }
        return flag;
    }

    public Optional<User> userExistById(Long userId) throws Exception {
        log.info("Inside userExistById of UserServiceImpl");
        return userRepository.findById(userId);
    }

    public UserDto populateUserData(User user) throws Exception {
        log.info("Inside populateUserData of UserServiceImpl");
        UserDto userDto = new UserDto();
        userDto.setUserId(user.getUserId());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setEmail(user.getEmail());
        userDto.setPassword(user.getPassword());
        userDto.setAddress(user.getAddress());
        return userDto;
    }
}

