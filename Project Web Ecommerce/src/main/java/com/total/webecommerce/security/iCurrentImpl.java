package com.total.webecommerce.security;

import com.total.webecommerce.entity.User;
import com.total.webecommerce.exception.BadResquestException;
import com.total.webecommerce.respository.OfUser.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class iCurrentImpl implements iCurrent{
    @Autowired
    private UserRepository userRepository;
    @Override
    public User getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByEmail(authentication.getName()).orElseThrow(
                ()->{
                    throw new BadResquestException("Not found User with Email " +authentication.getName());
                }
        );

    }
}
