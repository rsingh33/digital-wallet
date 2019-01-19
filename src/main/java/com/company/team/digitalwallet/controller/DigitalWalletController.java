package com.company.team.digitalwallet.controller;



import com.company.team.digitalwallet.entity.Role;
import com.company.team.digitalwallet.entity.User;
import com.company.team.digitalwallet.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;


@RestController
public class DigitalWalletController {


    public static final Logger logger = LoggerFactory.getLogger(DigitalWalletController.class);

    @Autowired
    private UserRepository usersRepository;
    ArrayList<User> users = new ArrayList<>();

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @RequestMapping(value = "/users/", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> addUser(@RequestBody User user) {
        logger.info("Creating User ", user);

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Random random = new Random();
        String pin = random.nextInt((10000 - 999) + 999) + "";
        user.setPin(pin);

        Set<Role> roles = new HashSet<>();
        Role role = new Role();
        role.setRole("ROLE_USER");
        role.setRoleId(user.getId());
        roles.add(role);

        user.setRoles(roles);
        user.setActive(1);
        user.setUserSince(timestamp);
        user.setBalance(0);

        usersRepository.save(user);

        users.add(user);


        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/users/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getUser(@PathVariable("id") int id) {
        logger.info("Fetching User with id {}", id);

        User user = usersRepository.getOne(id);

        if (user == null) {
            logger.error("User with id {} not found.", id);
            return new ResponseEntity("User with id " + id
                    + " not found", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(user, HttpStatus.OK);
    }


    @RequestMapping(value = "/users/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteUser(@PathVariable("id") int id) {

        logger.info("Fetching & Deleting User with id {}", id);


        System.out.println("Before deletion total users are: " + users.size());
        if (usersRepository.getOne(id) != null) {
            usersRepository.deleteById(id);
        } else {
            logger.error("Unable to delete. User with id {} not found.", id);
            return new ResponseEntity("Unable to delete. User with id " + id + " not found.",
                    HttpStatus.NOT_FOUND);
        }
        System.out.println("After deletion total users are: " + users.size());

        return new ResponseEntity<User>(HttpStatus.NO_CONTENT);

    }


}
