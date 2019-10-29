package org.hyperskill.webquizengine.controller;

import org.hyperskill.webquizengine.dto.UserDto;
import org.hyperskill.webquizengine.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
public class AuthController {
    private final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final UserService service;

    @Autowired
    public AuthController(UserService service) {
        this.service = service;
    }

    @PostMapping(path = "/register", consumes = APPLICATION_JSON_VALUE)
    public UserDto register(@Valid @RequestBody UserDto userDto) {
        logger.info("A new user '{}' wants to register", userDto.getEmail());
        try {
            Long id = service.registerNewUser(userDto.getEmail(), userDto.getPassword());
            userDto.setId(id);
            return userDto;
        } catch (DataIntegrityViolationException e) {
            logger.warn("Duplicate email: {}", userDto.getEmail());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "The email is already taken by another user", e);
        }
    }
}