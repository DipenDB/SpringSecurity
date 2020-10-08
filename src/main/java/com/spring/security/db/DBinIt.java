package com.spring.security.db;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;

//@Component
public class DBinIt implements CommandLineRunner {
    PasswordEncoder passwordEncoder;
    UserRepository userRepository;

    public DBinIt(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        User user = new User("user",passwordEncoder.encode("user"),"USER","ROLE_USER");
        User admin = new User("admin",passwordEncoder.encode("admin"),"ADMIN","ACCESS_TEST1,ACCESS_TEST2,ACCESS_TEST3,ROLE_ADMIN");
        User manager = new User("manager",passwordEncoder.encode("manager"),"MANAGER","ACCESS_TEST1,ROLE_MANAGER");

        List<User> users = Arrays.asList(user,admin,manager);
        this.userRepository.saveAll(users);


    }
}
