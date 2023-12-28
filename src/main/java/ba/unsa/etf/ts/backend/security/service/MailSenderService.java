package ba.unsa.etf.ts.backend.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
public class MailSenderService {
    @Autowired
    JavaMailSender mailSender;

    @Autowired
    private Environment environment;

    public void sendEmail(String toEmail, String subject, String body) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(toEmail);
        simpleMailMessage.setFrom(environment.getProperty("mail.from"));
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(body);

        mailSender.send(simpleMailMessage);
    }

    public void constructResetTokenEmail( String token, String email) {
        String url = environment.getProperty("reset_password.url") + token;
        String body = "Poštovani, \nPrimili ste ovaj e-mail jer ste zatražili obnovu lozinke za Vaš račun na aplikaciji Centar za podršku. \n" +
                "\n" +
                "Kliknite na sljedeći link kako biste pristupili obrascu za obnovu lozinke:\n" +
                "\n" +
                 url + "\n" +
                "\n" +
                "Ako niste zatražili obnovu lozinke, ignorišite ovaj e-mail.\n" +
                "\n" +
                "Napomena: Link za obnovu lozinke ističe nakon sat vremena.\n" +
                "\n" +
                "Hvala Vam na povjerenju.\n" +
                "\n" +
                "S poštovanjem,\n" +
                "Tim za podršku";
        String subject = "Obnova lozinke - Centar za podršku";

        sendEmail(email, subject, body);
    }
}
