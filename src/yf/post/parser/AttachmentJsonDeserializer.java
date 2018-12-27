package yf.post.parser;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import yf.post.parser.dto.PostAttachmentDTO;

import java.io.IOException;
import java.util.List;

public class AttachmentJsonDeserializer extends JsonDeserializer<List<PostAttachmentDTO>> {

    @Override
    public List<PostAttachmentDTO> deserialize(JsonParser jp,
                                               DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        System.out.println(jp);

        return null;
    }

}
