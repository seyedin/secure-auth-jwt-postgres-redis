package com.secureauth.service;

import com.secureauth.entity.Role;
import com.secureauth.entity.User;
import com.secureauth.entity.enums.RoleType;
import com.secureauth.exception.CustomException;
import com.secureauth.exception.ErrorCode;
import com.secureauth.repository.RoleRepository;
import com.secureauth.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * Manages user-related operations, including authentication and registration.
 */
@Service
public class UserService implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Loads user details by username for authentication.
     *
     * @param username the username
     * @return the UserDetails
     * @throws UsernameNotFoundException if the user is not found
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("Loading user: {}", username);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.getRoles()
        );
    }

    /**
     * Registers a new user with the given details.
     *
     * @param username the username
     * @param password the password
     * @return the registered User
     * @throws CustomException if the username already exists
     */
    public User signup(String username, String password) {
        log.info("Registering new user: {}", username);
        if (userRepository.existsByUsername(username)) {
            throw new CustomException("Username already exists", ErrorCode.ALREADY_EXIST.getCode());
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));

        Role userRole = roleRepository.findByName(RoleType.USER)
                .orElseGet(() -> {
                    Role newRole = new Role();
                    newRole.setName(RoleType.USER);
                    return roleRepository.save(newRole);
                });

        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        user.setRoles(roles);

        return userRepository.save(user);
    }

    /**
     * Authenticates a user with the given credentials.
     *
     * @param username the username
     * @param password the password
     * @return the authenticated User
     * @throws CustomException if credentials are invalid
     */
    public User signIn(String username, String password) {
        log.info("Authenticating user: {}", username);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException("Invalid username or password",
                        ErrorCode.INVALID_EMAIL_OR_PASSWORD.getCode()));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new CustomException("Invalid username or password",
                    ErrorCode.INVALID_EMAIL_OR_PASSWORD.getCode());
        }
        return user;
    }
}