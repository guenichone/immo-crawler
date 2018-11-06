package org.barrak.immocrawler.backend.service.impl;

import org.apache.commons.validator.routines.EmailValidator;
import org.barrak.immocrawler.backend.model.User;
import org.barrak.immocrawler.backend.service.UserService;
import org.barrak.immocrawler.database.model.UserDocument;
import org.barrak.immocrawler.database.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collections;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void createUser(User user) {
        UserDocument userDocument = new UserDocument(user.getEmail(), user.getPassword(), user.getFirstName(), user.getLastName());

        userRepository.save(userDocument);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDocument user = userRepository.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }

        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), Collections.emptyList());
    }
}
