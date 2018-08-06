package yf.kafka;

import org.apache.kafka.common.serialization.Serializer;
import org.codehaus.jackson.map.ObjectMapper;
import yf.publication.dtos.PublicationElasticDTO;

import java.io.IOException;
import java.util.Map;


public class PublicationSerializer implements Serializer<PublicationElasticDTO> {
    @Override
    public void configure(Map<String, ?> map, boolean b) {

    }

    @Override
    public byte[] serialize(String s, PublicationElasticDTO publicationElasticDTO) {
        try {
            return new ObjectMapper().writeValueAsBytes(publicationElasticDTO);
        } catch (IOException e) {
            return new byte[0];
        }
    }

    @Override
    public void close() {

    }
}
