package ru.skillbox.monolithicapp.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.monolithicapp.entity.User;
import ru.skillbox.monolithicapp.entity.UserRole;
import ru.skillbox.monolithicapp.exception.PasswordDoestMatchException;
import ru.skillbox.monolithicapp.exception.UserAlreadyExistException;
import ru.skillbox.monolithicapp.model.*;
import ru.skillbox.monolithicapp.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("login")
    public ResponseEntity<UserRoles> logIn(HttpServletRequest request,
                                           HttpServletResponse response,
                                           @RequestBody LogInView logInView) {
        User user = userService.logIn(request, response, logInView);
        return ok(new UserRoles(
                user.getRoles().stream()
                        .map(UserRole::getAuthority)
                        .collect(Collectors.toSet())));
    }

    @PostMapping("logout")
    public ResponseEntity<Void> logOut(HttpServletRequest request,
                                       HttpServletResponse response) {
        userService.logOut(request, response);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("register")
    public void register(@RequestBody UserView registrationData)
            throws UserAlreadyExistException, PasswordDoestMatchException {
        userService.register(registrationData);
    }

    @GetMapping("roles")
    public ResponseEntity<UserRoles> getUserRoles(@AuthenticationPrincipal User user) {
        if (user == null) {
            return ok(new UserRoles(Collections.singleton(EUserRole.ROLE_ANONYMOUS.name())));
        }

        return ok(new UserRoles(
                user.getRoles().stream()
                        .map(UserRole::getAuthority)
                        .collect(Collectors.toSet())));
    }

    @PostMapping("{id}/password/change")
    public ResponseEntity<Void> changeUserPassword(@PathVariable int id, @RequestBody PasswordView passwordView) {
        userService.changePassword(id, passwordView.getPassword());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("{id}/delete")
    public ResponseEntity<Void> deleteUser(@PathVariable int id) {
        userService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("all")
    public ResponseEntity<List<UserViewForAdmin>> getAllUsers() {
        return ResponseEntity.ok(userService.getUsers());
    }

    @AllArgsConstructor
    @Data
    protected static class UserRoles {
        private Set<String> roles;
    }

}