package company.model;


import company.enums.Role;
import lombok.*;

import java.util.UUID;


@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private UUID id;
    private String name;
    private String username;
    private String email;
    private String password;
    private Role role;
}
