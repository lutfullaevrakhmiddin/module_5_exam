package company.service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

public class RegisterService {
    public static String sendCodeToEmail(String email) {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "sandbox.smtp.mailtrap.io");
        properties.put("mail.smtp.port", "2525");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        String username = "dbbdc522c504a9";
        String password = "f8bb1dbd82981c";

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        int random = new Random().nextInt(10_000, 100_000);
        Message message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress("blabla@gmail.com"));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(email));
            message.setSubject("verification code");
            message.setContent("code : " + random, "text/html");

            CompletableFuture.runAsync(() -> {
                try {
                    Transport.send(message);
                } catch (MessagingException e) {
                }
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return random + "";
    }
}
