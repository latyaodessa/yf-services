package yf.mail.services;

import yf.mail.dtos.LanguagesEnum;
import yf.mail.dtos.MailTemplateNamesEnum;
import yf.mail.entities.EmailTemplate;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.logging.Logger;

public class EmailDAO {

    private static final Logger LOG = Logger.getLogger(EmailDAO.class.getName());

    @PersistenceContext
    private EntityManager em;

    public EmailTemplate getTemplateByNameLanguage(final MailTemplateNamesEnum name,
                                                   final LanguagesEnum language) {

        TypedQuery<EmailTemplate> query = em.createNamedQuery(EmailTemplate.QUERY_GET_TEMPLATE_BY_NAME_LANGUAGE,
                EmailTemplate.class)
                .setParameter("name",
                        name)
                .setParameter("language",
                        language);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            LOG.severe("MAIL TEMPLATE: " + e);
            return null;
        }
    }
}
