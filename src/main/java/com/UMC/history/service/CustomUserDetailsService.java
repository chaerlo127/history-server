package com.UMC.history.service;


import com.UMC.history.DTO.UserDTO;
import com.UMC.history.entity.strongEntity.UserEntity;
import com.UMC.history.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        System.out.println(21);
        UserEntity findUser =userRepository.findByUserId(userId);
        Optional<UserEntity> userEntity= Optional.ofNullable(findUser);
        System.out.println(22);
        return userEntity
                .map(this::createUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException(userId + " -> 데이터베이스에서 찾을 수 없습니다."));
        /*
        System.out.println(22);
        if (findUser==null){
            System.out.println(23);
            throw new UsernameNotFoundException(userId + " -> 데이터베이스에서 찾을 수 없습니다.");
        }
        System.out.println(24);
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(findUser.getAuthority().toString());
        System.out.println(25);
        return new User(findUser.getUserId(), findUser.getPassword(), Collections.singleton(grantedAuthority));*/
    }

    // DB 에 User 값이 존재한다면 UserDetails 객체로 만들어서 리턴
    private UserDetails createUserDetails(UserEntity userEntity) {
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(userEntity.getAuthority().toString());

        return new User(
                String.valueOf(userEntity.getUserId()),
                userEntity.getPassword(),
                Collections.singleton(grantedAuthority)
        );
    }
}