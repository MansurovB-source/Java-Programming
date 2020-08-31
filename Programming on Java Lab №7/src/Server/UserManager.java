package Server;

import Common.User;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by IntelliJ IDEA.
 *
 * @author Behruz Mansurov
 */
public class UserManager {
    private final DataBaseManager dataBaseManager;
    private User currentUser;
    private String receiver;

    UserManager(DataBaseManager dataBaseManager) {
        this.dataBaseManager = dataBaseManager;
    }

    User isRegistered(String login) {
        return dataBaseManager.isRegitered(login);
    }

    User signIn(String login, String password) {
        String pass = hashPassword(password);
        return dataBaseManager.singIn(login, pass);
    }

    User singUp(String login, String password) {
        String pass = password;
        String hashPassword;
        User user = null;
        if (pass == null) {
            pass = generatePassword();
            hashPassword = hashPassword(pass);
            if (sendMessage(login, pass)) {
                user = new User(login, hashPassword);
            }
        } else {
            hashPassword = hashPassword(pass);
            user = new User(login, hashPassword);
        }
        if (dataBaseManager.singUp(user)) {
            return user;
        }
        return null;
    }

    private String generatePassword() {
        Random rnd = new Random(System.currentTimeMillis());
        final int minLength = 9;
        final int maxLength = 13;
        final int minValue = 97;
        final int maxValue = 122;
        final int length = minLength + rnd.nextInt(maxLength - minLength + 1);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append((char) (minValue + rnd.nextInt(maxValue - minValue + 1)));
        }
        return String.valueOf(sb);
    }

    private String hashPassword(String password) {
        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            byte[] messageDigest = sha.digest(password.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }

            return hashtext;
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    private boolean sendMessage(String login, String password) {
        String from = "briar.meklit@99cows.com";
        String host = "mail.0hcow.com";
        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.host", host);
        Session session = Session.getDefaultInstance(properties);
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(login));
            message.setSubject("Registration in application \"Lab - 7\"");
            message.setText("Registration succeed. Your password: " + password);
            Transport.send(message);
            return true;
        } catch (MessagingException e) {
            System.out.println("Ошибка отправки сообщения: " + e.getMessage());
            return false;
        }
    }
}
