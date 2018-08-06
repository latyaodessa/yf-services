package yf.kafka;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("kafka")
@Produces(MediaType.APPLICATION_JSON)
@Stateless
public class KafkaRestImpl {

    @Inject
    private KafkaProducerTest kafkaProducerTest;

    @GET
    @Path("test")
    public void test() {
        kafkaProducerTest.runProducer();
    }
}
