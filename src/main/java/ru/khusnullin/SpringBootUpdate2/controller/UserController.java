package ru.khusnullin.SpringBootUpdate2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.khusnullin.SpringBootUpdate2.model.User;
import ru.khusnullin.SpringBootUpdate2.service.UserService;

import java.security.Principal;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/current")
    public ResponseEntity<User> printUser(Principal principal) {
        User user = userService.getUserByEmail(principal.getName());
        return ResponseEntity.ok(user);
    }

}
