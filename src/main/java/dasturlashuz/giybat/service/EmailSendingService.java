package dasturlashuz.giybat.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailSendingService {
    @Value("${spring.mail.username}")
    private String fromAccount;
    @Autowired
    private JavaMailSender mailSender;


    public void sendRegistrationEmail(String email, Integer profileId) {
        String subject = "Complete Registration";

        String body = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Title</title>\n" +
                "    <style>\n" +
                "        a {\n" +
                "            padding: 10px 30px;\n" +
                "            display: inline-block;\n" +
                "        }\n" +
                "\n" +
                "\n" +
                "        .button-link {\n" +
                "            text-decoration: none;\n" +
                "            color: white;\n" +
                "            background-color: indianred;\n" +
                "        }\n" +
                "\n" +
                "        .button-link:hover {\n" +
                "            background-color: #dd4444;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "<h1>Complete Registration</h1>\n" +
                "<p>\n" +
                "    Please click to button link for completing registration:<a class=\"button-link\"\n" +
                "        href=\"http://localhost:8081/api/v1/auth/verification/%d\" target=\"_blank\">Click there</a>\n" +
                "</p>\n" +
                "</body>\n" +
                "</html>";
        body = String.format(body, profileId);

        sendMimeEmail(email, subject, body);
    }


    public void sendMimeEmail(String email, String subject, String body) {
        try {


            MimeMessage msg = mailSender.createMimeMessage();
            msg.setFrom(fromAccount);

            MimeMessageHelper helper = new MimeMessageHelper(msg, true);
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(body, true);
            mailSender.send(msg);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}

