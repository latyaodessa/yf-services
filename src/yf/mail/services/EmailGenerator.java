package yf.mail.services;

import yf.core.JNDIPropertyHelper;
import yf.mail.converters.TemplateToEmailSendConverter;
import yf.mail.dtos.EmailToSendDTO;
import yf.mail.dtos.LanguagesEnum;
import yf.mail.dtos.MailTemplateNamesEnum;
import yf.mail.entities.EmailTemplate;
import yf.user.entities.User;
import yf.user.entities.Verifications;

import javax.inject.Inject;

public class EmailGenerator {

    private static final String DOMAIN = new JNDIPropertyHelper().lookup("yf.domain");


    @Inject
    private EmailDAO emailDAO;
    @Inject
    private TemplateToEmailSendConverter converter;


    public EmailToSendDTO generateVerifyEmail(final Verifications verifications, final User userToRegister) {
        final EmailTemplate emailTemplate = emailDAO.getTemplateByNameLanguage(MailTemplateNamesEnum.VERIFICATION, LanguagesEnum.RU);
        EmailToSendDTO dto = new EmailToSendDTO();
        dto.getPlaceHolders().put("link", DOMAIN.concat("verify/").concat(verifications.getVerification()));

        converter.templateToEmailSendDto(emailTemplate, dto, userToRegister);
        return dto;
    }

    public EmailToSendDTO generateResetaPasswordEmail(final Verifications verifications, final User userToRegister) {
        final EmailTemplate emailTemplate = emailDAO.getTemplateByNameLanguage(MailTemplateNamesEnum.RESET_PASSWORD, LanguagesEnum.RU);
        EmailToSendDTO dto = new EmailToSendDTO();
        dto.getPlaceHolders().put("link", DOMAIN.concat("reset/").concat(verifications.getVerification()));

        converter.templateToEmailSendDto(emailTemplate, dto, userToRegister);
        return dto;
    }
}
