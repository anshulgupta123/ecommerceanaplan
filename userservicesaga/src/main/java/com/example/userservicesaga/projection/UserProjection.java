package com.example.userservicesaga.projection;

import com.example.commonservicesaga.modals.UserDto;
import com.example.commonservicesaga.query.GetUserPaymentDetailsQuery;
import com.example.userservicesaga.entity.User;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserProjection {


    @Autowired
    UserRepository userRepository;

    @QueryHandler
    public UserDto getUserPaymentDetails(GetUserPaymentDetailsQuery query) {

        Optional<User> user = userRepository.findById(Long.valueOf(query.getUserId()));
        User userFromRepo = user.get();
        UserDto userDto = new UserDto();
        userDto.setUserId(userFromRepo.getUserId());
        userDto.setAddress(userFromRepo.getAddress());
        userDto.setEmail(userFromRepo.getEmail());
        userDto.setFirstName(userFromRepo.getFirstName());
        userDto.setLastName(userFromRepo.getLastName());
        return userDto;
    }
}
