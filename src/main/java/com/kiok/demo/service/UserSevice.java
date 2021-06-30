package com.kiok.demo.service;

import com.kiok.demo.models.Role;
import com.kiok.demo.models.User;
import com.kiok.demo.repo.UserRepos;
import org.springframework.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.UUID;

@Service
public class UserSevice implements UserDetailsService {
    @Autowired
    private UserRepos userRepos;

    @Autowired
    private MailSender mailSender;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepos.findByUsername(username);
    }

    public boolean addUser(User user) {
        User userFromDb = userRepos.findByUsername(user.getUsername());

        if (userFromDb != null) {
            return false;
        }

        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        user.setActivationCode(UUID.randomUUID().toString());

        userRepos.save(user);

        if (!StringUtils.isEmpty(user.getEmail())) {
            String message = String.format(
                    "Hello, %s! \n" +
                            "Welcome to Sweater. Please, visit next link: http://localhost:8081/activate/%s",
                    user.getUsername(),
                    user.getActivationCode()
            );

            mailSender.sendMail(user.getEmail(), "Activation code", message);
        }

        return true;
    }

    public boolean activateUser(String code) {
        User user = userRepos.findByActivationCode(code);

        if (user == null) {
            return false;
        }

        user.setActivationCode(null);

        userRepos.save(user);

        return true;
    }
}