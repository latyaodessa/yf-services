package yf.mail.rest;

import yf.mail.dtos.EmailToSendDTO;
import yf.mail.services.EmailService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/email")
@Produces(MediaType.APPLICATION_JSON)
@Stateless
public class MailRestImpl {

    @Inject
    private EmailService emailService;

    @POST
    @Path("send/custom")
    public Response getPostDetailsDTO(final EmailToSendDTO emailToSendDTO) {
        return emailService.sendEmail(emailToSendDTO);
    }

}
