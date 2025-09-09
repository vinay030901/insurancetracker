package com.sundefined.insurancetracker.service;

import com.sundefined.insurancetracker.model.Policy;
import com.sundefined.insurancetracker.model.PolicyEvent;
import com.sundefined.insurancetracker.model.User;
import com.sundefined.insurancetracker.model.UserRequestDto;
import com.sundefined.insurancetracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final PolicyRedisService policyRedisService;

    public void attachUserToPolicy(Policy event,String userId){
        User user = userRepository.findByUserId(userId);

        if(user.getPolicyIds()==null){
            user.setPolicyIds(new ArrayList<>());
            user.getPolicyIds().add(event.getPolicyId());
        }
        else {
            if(!user.getPolicyIds().contains(event.getPolicyId()))
                user.getPolicyIds().add(event.getPolicyId());
        }
        policyRedisService.setUser(user);
        userRepository.save(user);
    }
    public User getUser(UserRequestDto userRequestDto){
        Optional<User> user = userRepository.findByUserIdAndPassword(userRequestDto.getUserId(),userRequestDto.getPassword());
        if(user.isEmpty()){
            User u = User.builder()
                    .userId(userRequestDto.getUserId())
                    .password(userRequestDto.getPassword())
                    .build();
            return userRepository.save(u);
        }
        policyRedisService.setUser(user.get());
        return user.get();
    }
}
