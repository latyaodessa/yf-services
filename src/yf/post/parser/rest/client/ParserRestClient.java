package yf.post.parser.rest.client;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.GenericType;
import yf.core.JNDIPropertyHelper;
import yf.post.parser.dto.PostDTO;

import java.util.List;

public class ParserRestClient {

    private static final String PARSER_URI = new JNDIPropertyHelper().lookup("yf.parser.uri");

    public List<PostDTO> parseAllPages(long groupid,
                                       int firstpage,
                                       int lastpage) {
        return Client.create()
                .resource(PARSER_URI + "trigger/vkparser")
                .path(String.valueOf(groupid))
                .path(String.valueOf(firstpage))
                .path(String.valueOf(lastpage))
                .get(new GenericType<List<PostDTO>>() {
                });
    }
}
