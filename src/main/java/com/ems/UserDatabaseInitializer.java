package com.ems;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.ems.entities.Role;
import com.ems.entities.UserEntity;
import com.ems.repos.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserDatabaseInitializer implements CommandLineRunner {

    private final UserRepository userRepo;

    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        // Check if the user table is empty
        if (userRepo.count() == 0) {
            // Create a default admin user
            UserEntity admin = new UserEntity();
            admin.setEmail("admin@gmail.com");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setRole(Role.ADMIN);

            // Save the user to the database
            userRepo.save(admin);

            System.out.println("Default admin user created.");
        } else {
            System.out.println("Users already exist. Skipping admin creation.");
        }
    }
}
