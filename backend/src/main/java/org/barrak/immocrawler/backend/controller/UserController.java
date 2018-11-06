package org.barrak.immocrawler.backend.controller;

import org.apache.commons.validator.routines.EmailValidator;
import org.barrak.immocrawler.backend.model.User;
import org.barrak.immocrawler.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostMapping("/sign-up")
    public void signUp(@RequestBody User user) {
        if (!EmailValidator.getInstance(true).isValid(user.getEmail())) {
            throw new IllegalArgumentException("Invalid email.");
        }

        if (StringUtils.isEmpty(user.getPassword())) {
            throw new IllegalArgumentException("Invalid password.");
        }

        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userService.createUser(user);
    }

    @PostMapping("/confirm")
    public void confirm(@RequestBody String secret) {

    }
}
