package yf.mail.services;

import java.util.logging.Logger;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;

import yf.core.JNDIPropertyHelper;
import yf.mail.dtos.EmailToSendDTO;
import yf.mail.entities.EmailLogs;
import yf.user.dto.VerificationTypesEnum;
import yf.user.entities.User;
import yf.user.entities.Verifications;
import yf.user.services.VerificationService;

public class EmailService {

    private static final Logger LOG = Logger.getLogger(EmailService.class.getName());

    private static final String MAIL_SENDER_URI = new JNDIPropertyHelper().lookup("yf.mail.sender");

    @Inject
    private VerificationService verificationService;
    @Inject
    private EmailGenerator emailGenerator;

    @PersistenceContext
    private EntityManager em;

    public Response sendEmail(final EmailToSendDTO dto) {
        ClientResponse response = Client.create()
                .resource(MAIL_SENDER_URI + "rest/send")
                .type(MediaType.APPLICATION_JSON)
                .post(ClientResponse.class,
                        dto);

        if (response.getStatus() != 200) {
            LOG.severe("Failed : HTTP error code : " + response.getStatus() + " error " + response.getClient()
                    .getMessageBodyWorkers());
            return Response.status(500)
                    .entity(dto)
                    .build();
        }

        EmailLogs emailLogs = new EmailLogs(dto);
        em.persist(emailLogs);

        return Response.ok()
                .entity(dto)
                .build();

    }

    public void sendVerificationEmail(final User userToRegister) {
        final Verifications verifications = verificationService.createEmailVerification(userToRegister,
                VerificationTypesEnum.EMAIL);
        final EmailToSendDTO emailToSendDTO = emailGenerator.generateVerifyEmail(verifications,
                userToRegister);
        sendEmail(emailToSendDTO);
    }

    public void sendResetPasswordEmail(final User user) {
        final Verifications verifications = verificationService.createEmailVerification(user,
                VerificationTypesEnum.RESET_PASSWORD);
        final EmailToSendDTO emailToSendDTO = emailGenerator.generateResetaPasswordEmail(verifications,
                user);
        sendEmail(emailToSendDTO);
    }

}
