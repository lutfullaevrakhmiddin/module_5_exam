package company.service;

import company.enums.Role;
import company.model.User;
import company.repository.UserRepository;
import company.utils.Util;

import java.util.Optional;
import java.util.UUID;

public class UserService {
    private static UserService userService;
    private static UserRepository userRepository = UserRepository.getInstance();

    public static UserService getInstance() {
        if (userService == null) userService = new UserService();
        return userService;
    }

    private UserService() {
    }

    public static boolean saveNewUser(String name, String username, String email, String password) {
        User user = new User(UUID.randomUUID(), name, username, email, password, Role.CUSTOMER);
        return userRepository.writeNewCustomer(user);
    }

    public static Optional<User> signIn(String username, String password) {
        Optional<User> userByUsername = userRepository.getUserByUsername(username);
        if (userByUsername.isPresent() && userByUsername.get().getPassword().equals(password)) {
            Util.activeUser = userByUsername.get();
            return userByUsername;
        } else return Optional.empty();
    }
}
