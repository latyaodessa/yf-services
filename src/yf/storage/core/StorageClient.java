package yf.storage.core;

import com.backblaze.b2.client.B2StorageClient;
import com.backblaze.b2.client.webApiHttpClient.B2StorageHttpClientBuilder;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.util.logging.Logger;

@Singleton
@Startup
public class StorageClient {

    private static final String ACCOUNT_ID = "b0e712168300";
    private static final String APP_KEY = "002e882d72dcd720004bf5e2ce2dec740da06492e5";
    private static final String USER_AGENT = "youngfolks";

    private static final Logger LOG = Logger.getLogger(StorageClient.class.getName());
    private B2StorageClient client;

    @PostConstruct
    void init() {
        try {
            client = B2StorageHttpClientBuilder.builder(ACCOUNT_ID, APP_KEY, USER_AGENT).build();
        } catch (Exception e) {
            LOG.severe("STORAGE EXCEPTION " + e.getMessage());
        }

    }

    public B2StorageClient getClient() {
        return client;
    }
}
