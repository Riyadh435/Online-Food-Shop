package com.example.e_shop.EmailSender;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;
import java.util.Random;

public class Email {
    public int send(String to, String from, String subject, String text, String verificationCode) {
        String username = "shopmine847@gmail.com"; // Update with your Gmail
        String password = "update your pass"; // Update with your Gmail password

        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(text + "\n\nVerification Code: " + verificationCode); // Include verification code in email body

            Transport.send(message);
            return 1;
        } catch (MessagingException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int sendEmail(String to, String from, String subject, String text) {
        String username = "shopmine847@gmail.com"; // Update with your Gmail username
        String password = "fola qewy tatt ovsx"; // Update with your Gmail password

        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(text);


            Transport.send(message);
            return 1;
        } catch (MessagingException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public String generateVerificationCode() {
        Random random = new Random();
        int code = 10000 + random.nextInt(90000); // Generate random 5-digit code
        return String.valueOf(code);
    }
}



