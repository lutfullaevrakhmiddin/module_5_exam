package company;

import company.model.User;

import java.util.UUID;

public class Main {
    public static void main(String[] args) {
        User user = new User(UUID.randomUUID(), "lutfullayevrahmiddin", "Rahmiddin", 22);
        System.out.println(user);
//        there is another change in 15.12.2025
        // twelveth row
    }
}