package vk.parser;

import java.io.IOException;
import java.util.List;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;

import vk.parser.dto.PostAttachmentDTO;

public class AttachmentJsonDeserializer extends JsonDeserializer<List<PostAttachmentDTO>> {

	   @Override
	    public List<PostAttachmentDTO> deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		   System.out.println(jp);
		   
	        return null;
	    }

	    

}
