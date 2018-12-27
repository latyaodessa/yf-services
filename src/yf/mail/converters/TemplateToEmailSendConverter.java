package yf.mail.converters;

import org.apache.commons.lang3.text.StrSubstitutor;
import yf.mail.dtos.EmailToSendDTO;
import yf.mail.entities.EmailTemplate;
import yf.user.entities.User;

import java.util.Map;

public class TemplateToEmailSendConverter {

    private String getReplacedTextFromPlaceholders(final String template,
                                                   final Map<String, String> placeholders) {
        return StrSubstitutor.replace(template,
                placeholders);
    }

    public void templateToEmailSendDto(final EmailTemplate template,
                                       final EmailToSendDTO dto,
                                       final User userToRegister) {

        dto.setSubject(template.getSubject());
        dto.setFrom(template.getSender());

        dto.setText(getReplacedTextFromPlaceholders(template.getText(),
                dto.getPlaceHolders()));
        dto.setHtml(getReplacedTextFromPlaceholders(template.getHtml(),
                dto.getPlaceHolders()));
        dto.getTo()
                .add(userToRegister.getEmail());
    }
}
