package com.sip.ams.controllers;

import com.sip.ams.entities.User;
import com.sip.ams.repositories.RoleRepository;
import com.sip.ams.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashSet;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("user")
public class UserController {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserController(UserRepository userRepository,
                          RoleRepository roleRepository,
                          BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @PostMapping("register")
    public User register(@RequestBody User user){
        user.setRoles(new HashSet<>(Arrays.asList(roleRepository.findByRole("ROLE_USER"))));
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

}
