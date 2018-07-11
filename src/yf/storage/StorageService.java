package yf.storage;

import com.backblaze.b2.client.exceptions.B2Exception;
import com.backblaze.b2.client.structures.B2Bucket;
import com.backblaze.b2.client.structures.B2GetUploadUrlRequest;
import com.backblaze.b2.client.structures.B2UploadUrlResponse;
import yf.storage.common.BacketsTypesEnum;
import yf.storage.core.StorageClient;

import javax.inject.Inject;
import java.util.logging.Logger;

public class StorageService {

    private static final Logger LOG = Logger.getLogger(StorageService.class.getName());

    @Inject
    private StorageClient client;

    public B2UploadUrlResponse getUploadUrlRequest(final BacketsTypesEnum bucketType) {


        try {
            final B2Bucket bucket = client.getClient().getBucketOrNullByName(bucketType.toString());
            if(bucket == null) { return null;}
            return client.getClient().getUploadUrl(B2GetUploadUrlRequest.builder(bucket.getBucketId()).build());
        } catch (B2Exception e) {
            LOG.severe(e.getMessage());
            return null;
        }
    }
}
