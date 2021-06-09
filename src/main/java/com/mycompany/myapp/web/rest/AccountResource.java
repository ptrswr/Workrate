package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.repository.UserRepository;
import com.mycompany.myapp.service.UserService;
import com.mycompany.myapp.service.dto.AdminUserDTO;
import com.mycompany.myapp.service.dto.UserDTO;
import java.security.Principal;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing the current user's account.
 */
@CrossOrigin("*")
@RestController
@RequestMapping("/api")
public class AccountResource {

    private static class AccountResourceException extends RuntimeException {

        private static final long serialVersionUID = 1L;

        private AccountResourceException(String message) {
            super(message);
        }
    }

    private final Logger log = LoggerFactory.getLogger(AccountResource.class);

    private final UserService userService;
    private final UserRepository userRepository;

    public AccountResource(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @GetMapping("/current/user")
    @SuppressWarnings("unchecked")
    public User getCurrentUser() {
        return userRepository.findCurrentUser();
    }

    /**
     * {@code GET  /account} : get the current user.
     *
     * @param principal the current user; resolves to {@code null} if not authenticated.
     * @return the current user.
     * @throws AccountResourceException {@code 500 (Internal Server Error)} if the user couldn't be returned.
     */
    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/account")
    @SuppressWarnings("unchecked")
    public AdminUserDTO getAccount(Principal principal) {
        if (principal instanceof AbstractAuthenticationToken) {
            AdminUserDTO user = userService.getUserFromAuthentication((AbstractAuthenticationToken) principal);
            Optional<User> user_fromDB = userRepository.findOneByLogin(user.getLogin());
            user_fromDB.ifPresent(value -> user.setCalendar(value.getCalendar()));
            return user;
        } else {
            throw new AccountResourceException("User could not be found");
        }
    }
}
