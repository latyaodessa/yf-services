package yf.submission.services;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import yf.core.JNDIPropertyHelper;

import javax.ejb.Stateless;
import javax.ws.rs.core.MediaType;
import java.util.Map;
import java.util.logging.Logger;

@Stateless
public class StorageService {

    private static final Logger LOG = Logger.getLogger(StorageService.class.getName());

    private static final String STORAGE_URI = new JNDIPropertyHelper().lookup("yf.storage");


    public void clean(final Map<String, Long> userUuids) {

        ClientResponse response = Client.create()
                .resource(STORAGE_URI + "storage/clean")
                .type(MediaType.APPLICATION_JSON)
                .post(ClientResponse.class,
                        userUuids);


        if (response.getStatus() != 200) {
            LOG.severe("Failed to clean submissions: HTTP error code : " + response.getStatus() + " error " + response.getClient()
                    .getMessageBodyWorkers());
        }

    }

}
