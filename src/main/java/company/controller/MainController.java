package company.controller;

import company.enums.Role;
import company.model.User;
import company.service.RegisterService;
import company.service.UserService;

import java.util.Optional;

import static company.utils.Util.*;
import static company.utils.Util.getString;

public class MainController {
    private static UserService userService = UserService.getInstance();
    private static AdminController adminController = AdminController.getInstance();
    private static CustomerController customerController = CustomerController.getInstance();

    public void start() {
        boolean b = true;
        while (b) {
            System.out.println("""
                    1. kirish
                    2. registratsiya
                    0. chiqish""");
            System.out.printf("%n");
            int choose = getInteger("tanlang");

            switch (choose) {
                case 1 -> signIn();
                case 2 -> signUp();
                default -> b = false;
            }
        }
    }

    private void signIn() {
        System.out.println(ANSI_BRIGHT_GREEN + "-------> Kirish <-------" + ANSI_RESET);

        String username = getString("Username kiriting");
        String password = getString("Parol kiriting");

        Optional<User> optionalUser = UserService.signIn(username, password);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (user.getRole().equals(Role.ADMIN)) {
                adminController.adminMenu();
            } else {
                customerController.customerMenu();
            }
        } else {
            System.err.println("xatolik !!!");
        }
    }

    private void signUp() {
        System.out.println(ANSI_BRIGHT_GREEN + "-------> Ro'yxatdan o'tish <-------" + ANSI_RESET);

        String email = getString("Emailingizni kiriting: ");

        String verificationCode = RegisterService.sendCodeToEmail(email);
        System.out.println("Biz " + email + " manziliga kod jo'natdik");

        String enteredCode = getString("Emailga kelgan kodni kiriting");

        if (!verificationCode.equals(enteredCode)) {
            System.err.println("❌ Kod noto‘g‘ri! Ro‘yxatdan o‘tish bekor qilindi.");
            return;
        }

        String name = getString("Ismingizni kiriting");
        String username = getString("Username yarating");
        String password = getString("Parol yarating");
        boolean success = UserService.saveNewUser(name, username, email, password);

        if (success) {
            System.out.println(ANSI_BRIGHT_GREEN + "✅ Muvaffaqiyatli ro'yxatdan o'tdingiz!" + ANSI_RESET);
            System.out.println("Endi login va parol bilan tizimga kirishingiz mumkin.");
        } else {
            System.err.println("❌ Ro'yxatdan o'tishda xatolik yuz berdi. Keyinroq urinib ko'ring.");
        }
    }
}
