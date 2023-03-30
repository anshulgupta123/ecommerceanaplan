package com.example.userservicesaga.controller;

import com.example.commonservicesaga.modals.User;
import com.example.commonservicesaga.modals.UserDto;
import com.example.commonservicesaga.query.GetUserPaymentDetailsQuery;
import com.example.userservicesaga.exception.InvalidCredentialsException;
import com.example.userservicesaga.service.UserService;
import com.example.userservicesaga.utility.UrlConstants;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private transient QueryGateway queryGateway;

    @Autowired
    UserService userService;

    public UserController(QueryGateway queryGateway) {
        this.queryGateway = queryGateway;
    }

    @GetMapping("{userId}")
    public User getUserPaymentDetails(@PathVariable String userId) {
        GetUserPaymentDetailsQuery getUserPaymentDetailsQuery
                = new GetUserPaymentDetailsQuery(userId);
        User user =
                queryGateway.query(getUserPaymentDetailsQuery,
                        ResponseTypes.instanceOf(User.class)).join();

        return user;
    }

    @PostMapping(UrlConstants.USER_LOGIN)
    public ResponseEntity<Object> loginUser(@RequestBody UserDto userDto) throws InvalidCredentialsException {
        log.info("Request for loginUser of UserController:{}", userDto);
        return new ResponseEntity<>(userService.userLogin(userDto), HttpStatus.OK);
    }


    @PostMapping(UrlConstants.ADD_USER)
    public ResponseEntity<Object> addUser(@RequestBody UserDto userDto) throws InvalidCredentialsException {
        log.info("Request for addUser of UserController:{}", userDto);
        return new ResponseEntity<>(userService.addUser(userDto), HttpStatus.OK);
    }

    @PutMapping(UrlConstants.UPDATE_USER)
    public ResponseEntity<Object> updatedUser(@RequestBody UserDto userDto) throws InvalidCredentialsException {
        log.info("Request for updatedUser of UserController:{}", userDto);
        return new ResponseEntity<>(userService.updateUser(userDto), HttpStatus.OK);
    }

    @GetMapping(UrlConstants.GET_ALL_USERS)
    public ResponseEntity<Object> getAllUsers() throws InvalidCredentialsException {
        log.info("Request for getAllUsers of UserController");
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    @GetMapping(UrlConstants.GET_USER_BY_ID)
    public ResponseEntity<Object> getUserById(@RequestParam Long userId) throws InvalidCredentialsException {
        log.info("Request for getUserById of UserController userId :{}", userId);
        return new ResponseEntity<>(userService.getUserById(userId), HttpStatus.OK);
    }

    @DeleteMapping(UrlConstants.DELETE_USER)
    public ResponseEntity<Object> deleteUserById(@RequestParam Long userId) throws InvalidCredentialsException {
        log.info("Request for deleteUserById of UserController userId :{}", userId);
        return new ResponseEntity<>(userService.deleteUser(userId), HttpStatus.OK);
    }
}
