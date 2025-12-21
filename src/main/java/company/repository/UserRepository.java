package company.repository;

import company.enums.Role;
import company.model.User;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Optional;
import java.util.UUID;

public class UserRepository {
    private static UserRepository userRepository;

    public static UserRepository getInstance() {
        if (userRepository == null) userRepository = new UserRepository();
        return userRepository;
    }

    private UserRepository() {
    }

    public boolean writeNewCustomer(User user) {
        String filePath = "src/main/java/company/data/users.txt";


        String line = String.format("%s|%s|%s|%s|%s|%s%n",
                user.getId(),
                user.getName(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                user.getRole()
        );

        try {

            Files.createDirectories(Paths.get("src/main/java/company/data"));


            Files.writeString(Paths.get(filePath), line,
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            return true;

        } catch (IOException e) {
            return false;
        }
    }

    public Optional<User> getUserByUsername(String username) {
        String filePath = "src/main/java/company/data/users.txt";

        if (!Files.exists(Paths.get(filePath))) {
            return Optional.empty();
        }

        try (var lines = Files.lines(Paths.get(filePath))) {

            for (String line : lines.toList()) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] parts = line.split("\\|");
                if (parts.length != 6) {
                    continue;
                }

                String currentUsername = parts[2].trim();

                if (currentUsername.equals(username)) {
                    try {
                        User user = new User();
                        user.setId(UUID.fromString(parts[0].trim()));
                        user.setName(parts[1].trim());
                        user.setUsername(currentUsername);
                        user.setEmail(parts[3].trim());
                        user.setPassword(parts[4].trim());
                        user.setRole(Role.valueOf(parts[5].trim()));

                        return Optional.of(user);

                    } catch (Exception e) {
                    }
                }
            }
            return Optional.empty();
        } catch (IOException e) {
            return Optional.empty();
        }
    }
}
