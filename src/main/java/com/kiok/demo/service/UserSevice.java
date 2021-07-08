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

import java.util.*;
import java.util.stream.Collectors;

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

    public void sendMessage(User user){
        if (!StringUtils.isEmpty(user.getEmail())) {
            String message = String.format(
                    "Hello, %s! \n" +
                            "Welcome to KIOK. Please, visit next link: http://localhost:8081/activate/%s",
                    user.getUsername(),
                    user.getActivationCode()
            );

            mailSender.sendMail(user.getEmail(), "Activation code", message);
        }
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

        sendMessage(user);

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

    public List<User> findAll() {
        return userRepos.findAll();
    }

    public void userSave(User user, String username, Map<String, String> form) {
        user.setUsername(username);
        //из перечисления в строку
        Set<String> roles = Arrays.stream(Role.values())
                .map(Role::name)
                .collect(Collectors.toSet());

        user.getRoles().clear();

        for (String key : form.keySet()) {
            if (roles.contains(key)) {
                user.getRoles().add(Role.valueOf(key));
            }
        }

        userRepos.save(user);
    }

    public void updateUsersProfile(User user, String password, String email) {
        String userEmail = user.getEmail();
        boolean isEmailChange = ((email != null && !email.equals(userEmail)) ||
            userEmail != null && !userEmail.equals(email));

        if (isEmailChange){
            user.setEmail(email);

            if (!StringUtils.isEmpty(email)){
                user.setActivationCode(UUID.randomUUID().toString());
            }
        }
        if (!StringUtils.isEmpty(password))
            user.setPassword(password);

        userRepos.save(user);
        if (isEmailChange)
            sendMessage(user);
    }
}