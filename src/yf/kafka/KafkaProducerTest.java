package yf.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import yf.publication.PublicationService;
import yf.publication.dtos.PublicationElasticDTO;

import javax.inject.Inject;
import java.util.Properties;
import java.util.concurrent.Future;
import java.util.logging.Logger;

public class KafkaProducerTest {

    private static final Logger LOG = Logger.getLogger(KafkaProducerTest.class.getName());

    @Inject
    private PublicationService publicationService;

    private final static String TOPIC = "test-topic";
    private final static String BOOTSTRAP_SERVERS =
            "server.local:9092";

    private Producer<String, PublicationElasticDTO> createProducer() {
        Properties props = new Properties();
        setupBootstrapAndSerializers(props);
        setupBatchingAndCompression(props);
        setupRetriesInFlightTimeout(props);

        return new KafkaProducer<>(props);
    }

    private void setupBootstrapAndSerializers(Properties props) {
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                KafkaProducerTest.BOOTSTRAP_SERVERS);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "KafkaProducerTest");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class.getName());


        //Custom Serializer - config "value.serializer"
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                PublicationSerializer.class.getName());

    }

    private void setupRetriesInFlightTimeout(Properties props) {
        //Only one in-flight messages per Kafka broker connection
        // - max.in.flight.requests.per.connection (default 5)
        props.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION,
                1);
        //Set the number of retries - retries
        props.put(ProducerConfig.RETRIES_CONFIG, 3);

        //Request timeout - request.timeout.ms
//        props.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, 35_000);

        //Only retry after one second.
        props.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG, 1_000);
    }

    private void setupBatchingAndCompression(
            final Properties props) {
        //Linger up to 100 ms before sending batch if size not met
        props.put(ProducerConfig.LINGER_MS_CONFIG, 100);

        //Batch up to 64K buffer sizes.
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16_384 * 4);

        //Use Snappy compression for batch compression.
        props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "snappy");
    }

    public void runProducer() {
        final Producer<String, PublicationElasticDTO> producer = createProducer();
        try {
            final ProducerRecord<String, PublicationElasticDTO> record =
                    new ProducerRecord<>(TOPIC, "1", publicationService.getPublicationById("30058553"));

            try {
                final Future<RecordMetadata> future = producer.send(record);
                RecordMetadata metadata = future.get();
            } catch (Exception e) {
                LOG.severe(e.getMessage());
            }
        } finally

        {
            producer.flush();
            producer.close();
        }
    }

}
