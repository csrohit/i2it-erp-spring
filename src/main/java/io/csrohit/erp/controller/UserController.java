package io.csrohit.erp.controller;

import io.csrohit.erp.controller.util.JwtUtil;
import io.csrohit.erp.model.AuthenticationRequest;
import io.csrohit.erp.model.AuthenticationResponse;
import io.csrohit.erp.model.ErrorResponse;
import io.csrohit.erp.model.User;
import io.csrohit.erp.service.MyUserDetailsService;
import io.csrohit.erp.service.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);
    @Autowired
    UserService userService;

    @Autowired
    MyUserDetailsService myUserDetailsService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        return new ResponseEntity<>(userService.createUser(user), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect username and password");
        }

        final UserDetails userDetails = myUserDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }

    @GetMapping("")
    public Page<User> getAllUsersSortSearch(
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size,
            @RequestParam(value = "sort", required = false, defaultValue = "name,asc") String sort,
            @RequestParam(value = "search", required = false) String search) {
        return this.userService.findAllUsers(page, size, sort, search);
    }

    @DeleteMapping("{id}")
    @ApiOperation(value = "Delete user with given id", nickname = "deleteUserById")
    @ApiResponses({
            @ApiResponse(code = 200, message = "User deleted"),
            @ApiResponse(code = 400, message = "User does not exist", response = ErrorResponse.class)
    })
    public ResponseEntity<?> deleteUserById(@PathVariable Integer id) {
        try {
            userService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            LOG.error("Failed to delete user with id={}", id, e);
            return new ResponseEntity<>(new ErrorResponse("User does not exist"), HttpStatus.NOT_FOUND);
        }
    }
}
