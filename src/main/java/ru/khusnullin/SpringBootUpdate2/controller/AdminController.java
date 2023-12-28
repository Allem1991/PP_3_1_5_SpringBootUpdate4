package ru.khusnullin.SpringBootUpdate2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import ru.khusnullin.SpringBootUpdate2.model.User;
import ru.khusnullin.SpringBootUpdate2.service.RoleService;
import ru.khusnullin.SpringBootUpdate2.service.UserService;
import ru.khusnullin.SpringBootUpdate2.utils.RoleConverter;
import ru.khusnullin.SpringBootUpdate2.utils.UserErrorResponse;
import ru.khusnullin.SpringBootUpdate2.utils.UserNotFoundException;
import ru.khusnullin.SpringBootUpdate2.utils.UserNotValidException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/admin/users")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addCustomFormatter(new RoleConverter(roleService));
    }


    @GetMapping()
    public ResponseEntity<List<User>> printAllUsers() {
        return ResponseEntity.ok(userService.listUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable ("id") long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody @Valid User user, BindingResult bindingResult) {
        checkValidErrors(bindingResult);
        userService.addUser(user);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable ("id") long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PatchMapping
    public ResponseEntity<HttpStatus> updateUser(@RequestBody @Valid User user,  BindingResult bindingResult) {
        checkValidErrors(bindingResult);
        userService.updateUser(user);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ExceptionHandler
    private ResponseEntity<UserErrorResponse> handleException (UserNotFoundException exception) {
        return new ResponseEntity<>(
                new UserErrorResponse("User not found", System.currentTimeMillis()),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    private ResponseEntity<UserErrorResponse> handleException (UserNotValidException exception) {
        return new ResponseEntity<>(
                new UserErrorResponse(exception.getMessage(), System.currentTimeMillis()),
                HttpStatus.BAD_REQUEST);
    }

    private void checkValidErrors(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error: errors) {
                errorMessage.append(error.getField()).append(" - ").append(error.getDefaultMessage()).append(";");
            }
            throw new UserNotValidException(errorMessage.toString());
        }
    }

}
