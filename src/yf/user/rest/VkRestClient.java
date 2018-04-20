package yf.user.rest;

import com.sun.jersey.api.client.Client;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import yf.core.JSONService;
import yf.user.dto.external.VKUserDTO;

import javax.inject.Inject;
import javax.ws.rs.core.MediaType;
import java.util.logging.Logger;

public class VkRestClient {

    private static final Logger LOG = Logger.getLogger(VkRestClient.class.getName());

    private static final String ID_FIELD = "id";
    private static final String FIRSTNAME_FIELD = "first_name";
    private static final String LASTNAME_FIELD = "last_name";

    private static final String TITLE_FIELD = "title";

    private static final String SEX_FIELD = "sex";
    private static final String MIDDLENAME_FIELD = "maiden_name";
    private static final String BDAY_FIELD = "bdate";
    private static final String CITY_FIELD = "city";
    private static final String COUNTRY_FIELD = "country";
    private static final String MOBILE_NUMBER_FIELD = "mobile_phone";
    private static final String HOME_NUMBER_FIELD = "home_phone";
    private static final String VERIIFIED_FIELD = "verified";


    private static final String VK_HOST = "https://api.vk.com/method/";
    private static final String VK_METHOD_GET_USERS = "users.get?";
    private static final String VK_USER_IDS = "user_ids=";
    private static final String VK_REQUIRED_FIELDS = "&fields=" + String.join(",", new String[]{
            SEX_FIELD,
            MIDDLENAME_FIELD,
            BDAY_FIELD, CITY_FIELD,
            COUNTRY_FIELD,
            MOBILE_NUMBER_FIELD,
            HOME_NUMBER_FIELD,
            VERIIFIED_FIELD});

    private static final String VK_NAME_CASE_NOM = "&name_case=Nom";
    private static final String VK_API_VERSION = "&v=5.74";

    @Inject
    private JSONService jsonService;


    public VKUserDTO getVKUserDetails(long userId) {

        String JSONdto = Client.create().
                resource(userDetailsURI(userId)).accept(MediaType.APPLICATION_JSON)
                .get(String.class);

        try {
            JSONObject response = new JSONObject(JSONdto);
            if (response.has("response")) {
                JSONObject jsonObject = new JSONObject(JSONdto).getJSONArray("response").getJSONObject(0);
                return generateVkUser(jsonObject);
            }
            if (response.has("error")) {
                LOG.severe("VK LOGIN EXCEPTION: " + response);
                return null;
            }
        } catch (JSONException e) {
            LOG.severe("VK PARSE EXCEPTION: " + e + JSONdto);
        }
        return null;
    }

    private VKUserDTO generateVkUser(final JSONObject jsonObject) {
        VKUserDTO dto = new VKUserDTO();

        dto.setId(jsonService.getLongFromJsonObject(jsonObject, ID_FIELD));
        dto.setFirstName(jsonService.getStringFromJsonObject(jsonObject, FIRSTNAME_FIELD));
        dto.setLastName(jsonService.getStringFromJsonObject(jsonObject, LASTNAME_FIELD));
        dto.setSex(jsonService.getStringFromJsonObject(jsonObject, SEX_FIELD));
        dto.setBdate(jsonService.getStringFromJsonObject(jsonObject, BDAY_FIELD));
        dto.setVerified(jsonService.getIntegerFromJsonObject(jsonObject, VERIIFIED_FIELD) != 0);

        JSONObject counryData = jsonService.getJsonObjectFromJsonObject(jsonObject, COUNTRY_FIELD);
        if (counryData != null) {
            dto.setCountry(jsonService.getStringFromJsonObject(counryData, TITLE_FIELD));
        }

        JSONObject cityData = jsonService.getJsonObjectFromJsonObject(jsonObject, CITY_FIELD);
        if (cityData != null) {
            dto.setCity(jsonService.getStringFromJsonObject(cityData, TITLE_FIELD));
        }
        return dto;
    }


    private String userDetailsURI(long userId) {
        return VK_HOST + VK_METHOD_GET_USERS + VK_USER_IDS + userId + VK_REQUIRED_FIELDS + VK_NAME_CASE_NOM + VK_API_VERSION;
    }
}
