package yf.submission.services;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import yf.core.JNDIPropertyHelper;
import yf.mail.entities.EmailLogs;

import javax.ejb.Stateless;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;
import java.util.logging.Logger;

@Stateless
public class StorageService {

    private static final Logger LOG = Logger.getLogger(StorageService.class.getName());

    private static final String STORAGE_URI = new JNDIPropertyHelper().lookup("yf.storage");


//    public Response clean(final Map<String, String> userUuids) {
//
//        ClientResponse response = Client.create()
//                .resource(STORAGE_URI + "rest/clean")
//                .type(MediaType.APPLICATION_JSON)
//                .post(Map.class,
//                        userUuids);
//
//        if (response.getStatus() != 200) {
//            LOG.severe("Failed : HTTP error code : " + response.getStatus() + " error " + response.getClient()
//                    .getMessageBodyWorkers());
//            return Response.status(500)
//                    .entity(dto)
//                    .build();
//        }
//
//        EmailLogs emailLogs = new EmailLogs(dto);
//        em.persist(emailLogs);
//
//        return Response.ok()
//                .entity(dto)
//                .build();
//
//    }

}
